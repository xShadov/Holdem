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
	private int lastToBet = 0;
	private int turnPlayer = 0;
	private int betPlayer = 0;
	private boolean bidingOver = false;
	private boolean flopTime = false;
	private int bidingCount = 0;
	private boolean turnTime = false;
	private boolean riverTime = false;
	private boolean waitingForPlayerResponse = false;
	private Deck deck = null;
	private Table table = null;
	private boolean bidingTime = false;
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
        	  if (object instanceof SampleRequest) {
	        	  SampleRequest request = (SampleRequest) object;
	    		  if(request.getTAG().equals("B")){
	    			  if(bidingTime && !bidingOver){
		    			  if(request.getNumber()==betPlayer){
		    				  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()+request.getBetAmount());
		    				  table.setPot(table.getPot()+players.get(request.getNumber()).getBetAmount());
		    				  if(request.getNumber()==lastToBet && table.getRaiseAmount()==0){
		    					  bidingOver = true;
		    					  waitingForPlayerResponse = false;
		    				  } else{
		        				  setBetPlayer();
		    				  }
		    			  }
	    			  }
	    		  }
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
        	  for(Player player : players){
        		  if(player.getConnectionId()==con.getID()) player.setInGame(false);
        	  }
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
					table.setBigBlindAmount(bigBlindAmount);
					table.setSmallBlindAmount(smallBlindAmount);
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
					table.setPot(table.getPot()+smallBlindAmount);
					players.get((turnPlayer+2)%numPlayers).setHasBigBlind(true);
					players.get((turnPlayer+2)%numPlayers).setBetAmount(bigBlindAmount);
					players.get((turnPlayer+2)%numPlayers).setChipsAmount(players.get((turnPlayer+2)%numPlayers).getChipsAmount()-bigBlindAmount);
					table.setPot(table.getPot()+bigBlindAmount);
					turnPlayer++;
					bidingTime = true;
					bidingOver = false;
					bidingCount = 1;
					betPlayer = (turnPlayer+3)%numPlayers;
					lastToBet = Math.abs(betPlayer - 1);
				}
				
				if(bidingTime){
					if(!waitingForPlayerResponse || !bidingOver){
						sendBetResponse(betPlayer);
					}
					else{
						//timer check for timeout gonna be here
					}
					if(bidingOver){
						bidingTime = false;
						if(bidingCount==1) flopTime = true;
						else if(bidingCount==2) turnTime = true;
						else if(bidingCount==3) riverTime = true;
						bidingCount++;
					}
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
					bidingTime = true;
					bidingOver = false;
				}
				//turn
				if(turnTime){
					turnTime = false;
					table.addCard(deck.drawCard());
					bidingTime = true;
					bidingOver = false;
				}
				//river
				if(riverTime){
					riverTime = false;
					table.addCard(deck.drawCard());
					bidingTime = true;
					bidingOver = false;
				}
				//TableInfo.cardsOnTable=table.getCardList();
				response = new SampleResponse("T", table);
				server.sendToAllTCP(response);
			}
		}
	}
	
	private void sendBetResponse(int numberToBet) {
		response = new SampleResponse("B", numberToBet);
		server.sendToAllTCP(response);
		waitingForPlayerResponse = true;
	}

    
    private void setBetPlayer() {
		for(int i=0; i<players.size();i++){
			players.get(i).setHisTurnToBet(false);
		}
		betPlayer=(betPlayer+1)%numPlayers;
		for(int i=0; i<players.size();i++){
			if(players.get(betPlayer).isFolded() || !players.get(betPlayer).isInGame() || players.get(betPlayer).isAllIn()){
				betPlayer=(betPlayer+1)%numPlayers;
			}
		}
		waitingForPlayerResponse = false;
	}
	
	public static void main(String[] args) {
		try {
			new KryoServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

