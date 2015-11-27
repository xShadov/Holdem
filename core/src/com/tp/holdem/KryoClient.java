package com.tp.holdem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class KryoClient {

	private Client simulationClient;

    public KryoClient() {

        try {
           simulationClient = new Client();
           simulationClient.start();

           Kryo kryo = simulationClient.getKryo();
           kryo.register(SampleResponse.class);
           kryo.register(SampleRequest.class);
           kryo.register(ArrayList.class);
           kryo.register(List.class);
           kryo.register(Player.class);
           kryo.register(Card.class);
           kryo.register(Deck.class);
           kryo.register(Table.class);
           
           simulationClient.addListener(new Listener() {
              public synchronized void received(Connection connection, Object object) {
            	  if (object instanceof SampleResponse) {
            		  SampleResponse response = (SampleResponse) object;
            		  if(response.getTAG().equals("R")){
            			  Info.players=response.getPlayers();
            			  Info.changes=true;
            		  }
            		  if(response.getTAG().equals("N")){
            			  Info.yourNumber=response.getNumber();
            		  }
            		  if(response.getTAG().equals("C"))
            		  {
            			  TableInfo.cardsOnTable=response.getCards();
            			  TableInfo.changesInTable=true;
            		  }
            	  }
              }
           });
        } catch (Exception e) {
           e.printStackTrace();
        }

        new Thread("Connect") {
           public synchronized void run() {
              try {
                 simulationClient.connect(5000, "127.0.0.1", 54555);
                 while(true){
                	 try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                 }
              } catch (IOException ex) {
                 ex.printStackTrace();
                 System.exit(1);
              }

           }
        }.start();
     }
}

