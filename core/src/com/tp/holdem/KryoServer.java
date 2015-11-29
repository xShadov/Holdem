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
	private List<Player> playersWithHiddenCards = new ArrayList<Player>();
	private int counter = 0;
	private int numPlayers = 0;
	private int turnPlayer = 0;
	private boolean flopTime = false;
	private boolean turnTime = false;
	private boolean riverTime = false;
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
        	  players.get(numPlayers).setConnectionId(con.getID());
        	  response = new SampleResponse("N", numPlayers);
        	  server.sendToTCP(con.getID(), response);
        	  numPlayers++;
  			  if(numPlayers==2){
  				  newHand=true;
  				  gameStarted=true;
  			  }
  			  else{
  	      		  response = new SampleResponse("W", "Waiting for all players");
  	      		  server.sendToTCP(con.getID(), response);
  			  }
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
					if(table==null) table=new Table();
					deck.dealCards(2, players);
					for(Player player : players){
						//HCD - hand cards dealt
						response = new SampleResponse("HCD", player.getHand(), false);
						server.sendToTCP(player.getConnectionId(), response);
					}
					newHand=false;
					players.get(turnPlayer%numPlayers).setHasDealerButton(true);	
					players.get((turnPlayer+1)%numPlayers).setHasSmallBlind(true);
					players.get((turnPlayer+1)%numPlayers).setChipsAmount(players.get((turnPlayer+1)%numPlayers).getChipsAmount()-smallBlindAmount);
					players.get((turnPlayer+1)%numPlayers).setBetAmount(smallBlindAmount);
					players.get((turnPlayer+2)%numPlayers).setHasBigBlind(true);
					players.get((turnPlayer+2)%numPlayers).setBetAmount(bigBlindAmount);
					players.get((turnPlayer+2)%numPlayers).setChipsAmount(players.get((turnPlayer+2)%numPlayers).getChipsAmount()-bigBlindAmount);
				}
				
				playersWithHiddenCards = new ArrayList<Player>(players);
				for(Player player : playersWithHiddenCards){
					player.setHand(null);
				}
				
				response = new SampleResponse("R", playersWithHiddenCards);
				server.sendToAllTCP(response);
				//flop - 3 karty na stol
				if(flopTime){
					flopTime = false;
					table.addCard(deck.drawCard());
					table.addCard(deck.drawCard());
					table.addCard(deck.drawCard());
				}
				//turn
				if(turnTime){
					turnTime = false;
					table.addCard(deck.drawCard());
				}
				//river
				if(riverTime){
					riverTime = false;
					table.addCard(deck.drawCard());
				}
				//TableInfo.cardsOnTable=table.getCardList();
				response = new SampleResponse("T", table);
				server.sendToAllTCP(response);
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

