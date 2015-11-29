package com.tp.holdem;

import java.util.ArrayList;
import java.util.Collections;
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
	private List<Player> playersWithHiddenCards;
	private int counter = 0;
	private int numPlayers = 0;
	private int lastToBet = 0;
	private int turnPlayer = 0;
	private int betPlayer = 0;
	private boolean bidingOver = false;
	private boolean flopTime = false;
	private int handsCounter = 0;
	private int bidingCount = 0;
	private boolean turnTime = false;
	private boolean riverTime = false;
	private boolean waitingForPlayerResponse = false;
	private Deck deck = null;
	private Table table = null;
	private boolean bidingTime = false;
	private int startingSmallBlindAmount = 20;
	private int smallBlindAmount = Integer.valueOf(startingSmallBlindAmount);
	private int bigBlindAmount = smallBlindAmount*2;
	private boolean newHand = false;
	private SampleResponse response;
	
	public KryoServer(int playersCount,int botsCount, String limitType) throws Exception {

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
        	  handleReceived(object); 
          }
          
          public synchronized void connected(Connection con){
        	  handleConnected(con);
          }

          public synchronized void disconnected(Connection con){
        	  handleDisconnected(con);
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
					initiateNewHand();
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
						bidingCount++;
						if(bidingCount-1==1) flopTime = true;
						else if(bidingCount-1==2) turnTime = true;
						else if(bidingCount-1==3) riverTime = true;
						else if(bidingCount-1==4) timeToCheckWinner();
					}
				}
				
				for(int i=0; i<players.size(); i++){
					playersWithHiddenCards.get(i).setAllProperties(players.get(i));
				}
				
				response = new SampleResponse("R", playersWithHiddenCards);
				server.sendToAllTCP(response);
				//flop - 3 karty na stol
				if(flopTime){
					flopTime = false;
					table.addCard(deck.drawCard());
					table.addCard(deck.drawCard());
					table.addCard(deck.drawCard());
					setFirstAndLastToBet();
					bidingTime = true;
					bidingOver = false;
				}
				//turn
				if(turnTime){
					turnTime = false;
					table.addCard(deck.drawCard());
					setFirstAndLastToBet();
					bidingTime = true;
					bidingOver = false;
				}
				//river
				if(riverTime){
					riverTime = false;
					table.addCard(deck.drawCard());
					setFirstAndLastToBet();
					bidingTime = true;
					bidingOver = false;
				}
				//TableInfo.cardsOnTable=table.getCardList();
				response = new SampleResponse("T", table);
				server.sendToAllTCP(response);
			}
		}
	}

	
	
	private void timeToCheckWinner() {
		sendBetResponse(-1);
		List<HandRank> hands = new ArrayList<HandRank>();
		List<Player> winners = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(!players.get(i).isFolded() && players.get(i).isInGame()){
				hands.add(HandOperations.findHandRank(players.get(i).getNumber(), players.get(i).getHand(), table.getCardList()));
			}
		}
		HandRankComparator handComparator = new HandRankComparator();
		Collections.sort(hands, handComparator);
		if(handComparator.compare(hands.get(hands.size()-1), hands.get(hands.size()-2))==1){
			winners.add(players.get(hands.get(hands.size()-1).getPlayerNumber()));
		} else {
			winners.add(players.get(hands.get(hands.size()-1).getPlayerNumber()));
			winners.add(players.get(hands.get(hands.size()-2).getPlayerNumber()));
			if(hands.size()>=3){
				for(int i=hands.size()-3; i>=0 ; i--){
					if(handComparator.compare(hands.get(i), hands.get(hands.size()-2))==0){
						winners.add(players.get(hands.get(i).getPlayerNumber()));
					}
				}
			}
		}
		for(int i=0; i<winners.size(); i++){
			players.get(winners.get(i).getNumber())
				.setChipsAmount(players.get(winners.get(i).getNumber()).getChipsAmount() + table.getPot()/winners.size());
		}
		if(winners.size()==1){
			//OW - one winner
			response = new SampleResponse("OW", winners.get(0).getNumber());
		}
		else{
			//MW - multiple winners
			response = new SampleResponse("MW");
		}
		server.sendToAllTCP(response);
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		resetAfterRound();
	}

	private void resetAfterRound() {
		handsCounter++;
		if(handsCounter%10==0){
			smallBlindAmount+=startingSmallBlindAmount;
			bigBlindAmount=smallBlindAmount*2;
		}
		newHand = true;
	}

	private void setFirstAndLastToBet() {
		if(bidingCount==1){
			betPlayer = (turnPlayer+2)%numPlayers;
			lastToBet = (numPlayers+betPlayer-1)%numPlayers;
		}
		else{
			betPlayer = Integer.valueOf(turnPlayer);
			int counter = Integer.valueOf(betPlayer);
			int helper = 0;
			while(helper<numPlayers){
				if(players.get(counter%numPlayers).isFolded() || players.get(counter%numPlayers).isAllIn() || !players.get(counter%numPlayers).isInGame()){
					betPlayer=(betPlayer+1)%numPlayers;
					counter++;
					helper++;
				}	
				else break;
			}
			lastToBet = (numPlayers+betPlayer-1)%numPlayers;
			counter = Integer.valueOf(lastToBet);
			helper = 0;
			while(helper<numPlayers){
				if(counter==-1) counter=9;
				if(players.get(counter).isFolded() || players.get(counter).isAllIn() || !players.get(counter).isInGame()){
					lastToBet=(counter-1)%numPlayers;
				}	
				else break;
			}
		}
	}

	private void sendBetResponse(int numberToBet) {
		response = new SampleResponse("B", numberToBet);
		server.sendToAllTCP(response);
		waitingForPlayerResponse = true;
	}

    
    private void setBetPlayer() {
		betPlayer=(betPlayer+1)%numPlayers;
		for(int i=betPlayer%numPlayers; i<players.size();i++){
			if(players.get(i).isFolded() || !players.get(i).isInGame() || players.get(i).isAllIn()){
				betPlayer=(betPlayer+1)%numPlayers;
			}
			else break;
		}
		waitingForPlayerResponse = false;
	}
    
    private void handleConnected(Connection con) {
    	players.add(new Player(numPlayers));
    	players.get(numPlayers).setChipsAmount(1500);
    	players.get(numPlayers).setConnectionId(con.getID());
    	response = new SampleResponse("N", numPlayers);
    	server.sendToTCP(con.getID(), response);
    	numPlayers++;
    	if(numPlayers==2){
    		playersWithHiddenCards = new ArrayList<Player>(players.size());
    		for(int i=0; i<players.size(); i++){
				playersWithHiddenCards.add(new Player());
			}
    		newHand=true;
    		gameStarted=true;
		}
    	else{
    		response = new SampleResponse("W", "Waiting for all players");
    		server.sendToTCP(con.getID(), response);
		}
    }
    

	private void handleDisconnected(Connection con) {
		for(Player player : players){
    		  if(player.getConnectionId()==con.getID()) player.setInGame(false);
    	  }
	}
	

	private void handleReceived(Object object) {
		if (object instanceof SampleRequest) {
    	  SampleRequest request = (SampleRequest) object;
		  if(request.getTAG().equals("B")){
			  if(bidingTime && !bidingOver){
    			  if(request.getNumber()==betPlayer){
    				  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()+request.getBetAmount());
    				  players.get(request.getNumber()).setChipsAmount(players.get(request.getNumber()).getChipsAmount()-request.getBetAmount());
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
	
    private void initiateNewHand() {
		deck = new Deck();
		table = new Table();
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
		setFirstAndLastToBet();
	}
	
	public static void main(String[] args) {
		try {
			new KryoServer(3,0,"no-limit");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}