package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryoServer implements Runnable {
	
	private int port = 54555;
	private Server server;
	public boolean running = true;
	private volatile boolean gameStarted = false;
	private List<Player> players = new ArrayList<Player>();
	private int counter = 0;
	private int numPlayers = 0;
	private int turnPlayer = 0;
	private Deck deck = null;
	private Table table = null;
	private int smallBlindAmount = 20;
	private int bigBlindAmount = 50;
	private boolean newHand = false;
	private SampleResponse response;
	
	public KryoServer() throws Exception {

      server = new Server();

      Thread gameThread = new Thread(this);

      Kryo kryo = server.getKryo();
      kryo.register(SampleResponse.class);
      kryo.register(SampleRequest.class);
      kryo.register(ArrayList.class);
      kryo.register(List.class);
      kryo.register(Player.class);
      kryo.register(Card.class);
      kryo.register(Deck.class);
      kryo.register(Table.class);
      server.addListener(new Listener() { 
          public synchronized void received (Connection connection, Object object) { 
        	  if(object instanceof SampleRequest){
        		  
        	  }
          } 
          public synchronized void connected(Connection con){
        	  players.add(new Player(numPlayers));
        	  players.get(numPlayers).setChipsAmount(1500);
        	  response = new SampleResponse("N", numPlayers);
        	  server.sendToTCP(con.getID(), response);
        	  numPlayers++;
  			  if(numPlayers==2){
  				  newHand=true;
  				  gameStarted=true;
  			  }
  			  //else{
  	      	//	  response = new SampleResponse("W", "Waiting for all players");
  	    	//	  server.sendToAllTCP(response);
  			 // }
          }
          public synchronized void disconnected(Connection con){
        	  numPlayers--;
          }
      });
      
      server.bind(port);
      server.start();
      gameThread.start();
      // keep server running
      while (running) {
         Thread.sleep(100);
      }
	}
	
	@Override
	public synchronized void run() {
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(gameStarted){
				if(newHand){
					if(deck==null) deck=new Deck();
					if(table==null)table=new Table();
					deck.dealCards(2, players);
					newHand=false;
					players.get(turnPlayer).setHasDealerButton(true);	
					players.get(turnPlayer+1).setHasSmallBlind(true);
					players.get(turnPlayer+1).setChipsAmount(players.get(turnPlayer+1).getChipsAmount()-smallBlindAmount);
					players.get(turnPlayer+1).setBetAmount(smallBlindAmount);
					if(numPlayers==2){
						players.get(turnPlayer).setHasBigBlind(true);
						players.get(turnPlayer).setBetAmount(bigBlindAmount);
						players.get(turnPlayer).setChipsAmount(players.get(turnPlayer).getChipsAmount()-bigBlindAmount);
					}else{
						players.get(turnPlayer+2).setHasBigBlind(true);
						players.get(turnPlayer+2).setBetAmount(bigBlindAmount);
						players.get(turnPlayer+2).setChipsAmount(players.get(turnPlayer+2).getChipsAmount()-bigBlindAmount);
					}

					response = new SampleResponse("R", players);
					server.sendToAllTCP(response);
					//flop - 3 karty na stol
					table.addCard(deck.drawCard());
					table.addCard(deck.drawCard());
					table.addCard(deck.drawCard());
					//turn
					table.addCard(deck.drawCard());
					//river
					table.addCard(deck.drawCard());
					//TableInfo.cardsOnTable=table.getCardList();
					response = new SampleResponse("C", table.getCardList(),false);
					server.sendToAllTCP(response);
				}
				
				
				
				
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new KryoServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

