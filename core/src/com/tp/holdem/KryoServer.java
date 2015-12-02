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
	private int playersCount; //ustalona ilosc graczy przy stole
	private int botsCount;
	private String limitType; // zasada stolu ("no-limit", "fixed-limit", "pot-limit")
	private Strategy botStrategy;
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
	private int maxBetOnTable = Integer.valueOf(bigBlindAmount);
	private boolean newHand = false;
	private SampleResponse response;
		
	public KryoServer(int playersCount,int botsCount, String limitType, Strategy botStrategy) throws Exception {
		
	  this.playersCount=playersCount;
	  this.botsCount=botsCount;
	  this.limitType=limitType;
	      

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
					if(!waitingForPlayerResponse && !bidingOver){
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
					bidingTime = true;
					bidingOver = false;
					setFirstAndLastToBet();
				}
				//turn
				if(turnTime){
					turnTime = false;
					table.addCard(deck.drawCard());
					bidingTime = true;
					bidingOver = false;
					setFirstAndLastToBet();
				}
				//river
				if(riverTime){
					riverTime = false;
					table.addCard(deck.drawCard());
					bidingTime = true;
					bidingOver = false;
					setFirstAndLastToBet();
				}
				//TableInfo.cardsOnTable=table.getCardList();
				response = new SampleResponse("T", table);
				server.sendToAllTCP(response);
			}
		}
	}

	
	
	private void timeToCheckWinner() {
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
		System.out.println("dochodzi do konca check winner");
		resetAfterRound();
	}

	private void resetAfterRound() {
		handsCounter++;
		if(handsCounter%10==0){
			smallBlindAmount+=startingSmallBlindAmount;
			bigBlindAmount=smallBlindAmount*2;
		}
		for(int i=0; i<players.size();i++){
			if(players.get(0).getChipsAmount()==0){
				players.get(0).setInGame(false);
			}
			players.get(i).setBetAmount(0);
			players.get(i).setHasBigBlind(false);
			players.get(i).setHasDealerButton(false);
			players.get(i).setHasSmallBlind(false);
			players.get(i).getHand().remove(1);
			players.get(i).getHand().remove(0);
		}
		if(!notEnoughPlayers()){
			maxBetOnTable = Integer.valueOf(bigBlindAmount);
			newHand = true;	
		}
		else{
			gameStarted = false;
			response = new SampleResponse("GO");
			server.sendToAllTCP(response);
		}
	}

	private boolean notEnoughPlayers() {
		int playersInGameCount = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).isInGame()) playersInGameCount++;
		}
		return playersInGameCount<=1;
	}

	private void setFirstAndLastToBet() {
		if(bidingCount==1){
			betPlayer = (turnPlayer+2)%numPlayers;
			lastToBet = (numPlayers+betPlayer-1)%numPlayers;
		}
		else{
			if(!everybodyAllIn()){
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
					if(players.get(counter).isFolded() || players.get(counter).isAllIn() || !players.get(counter).isInGame()){
						if(counter==0) counter=Integer.valueOf(numPlayers);
						lastToBet=(counter-1)%numPlayers;
					}	
					else break;
					counter--;
					helper++;
				}
			}
			else bidingOver = true;
		}
	}

	private boolean everybodyAllIn() {
		for(int i=0; i<players.size();i++){
			if(!players.get(i).isAllIn()) return false;
		}
		return true;
	}

	private void sendBetResponse(int numberToBet) {
		if(!players.get(numberToBet).isFolded() && !players.get(numberToBet).isAllIn() && players.get(numberToBet).isInGame()){
			response = new SampleResponse("B", numberToBet, maxBetOnTable);
			server.sendToAllTCP(response);
			waitingForPlayerResponse = true;
		}
	}

    
    private void setNextAsBetPlayer() {
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
    	if(numPlayers==playersCount){
    		for(int i=0; i<botsCount;i++)
    		{
    			players.add(new Bot(numPlayers,"Bot"+String.valueOf(numPlayers),botStrategy));
    			players.get(numPlayers).setChipsAmount(1500);
    			numPlayers++;
    		}
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
			  numPlayers--;
    		  if(player.getConnectionId()==con.getID()) players.remove(player);
    		  break;
    	  }
	}
	

	private void handleReceived(Object object) {
		if (object instanceof SampleRequest) {
    	  SampleRequest request = (SampleRequest) object;
		  if(bidingTime && !bidingOver){
			  if(request.getNumber()==betPlayer){
				  if(request.getTAG().equals("BET")){
					  if(request.getBetAmount()>=maxBetOnTable){
	    				  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()+request.getBetAmount());
	    				  players.get(request.getNumber()).setChipsAmount(players.get(request.getNumber()).getChipsAmount()-request.getBetAmount());
	    				  table.setPot(table.getPot()+request.getBetAmount());
	    				  if(request.getBetAmount()>maxBetOnTable) maxBetOnTable = Integer.valueOf(request.getBetAmount());
	    				  if(players.get(request.getNumber()).getChipsAmount()==0) players.get(request.getNumber()).setAllIn(true);
					  }
				  }
				  else if(request.getTAG().equals("CALL")){
    				  table.setPot(table.getPot()+(maxBetOnTable-players.get(request.getNumber()).getBetAmount()));
    				  players.get(request.getNumber()).setChipsAmount(players.get(request.getNumber()).getChipsAmount()-
    						  (maxBetOnTable-players.get(request.getNumber()).getBetAmount()));
					  players.get(request.getNumber()).setBetAmount(maxBetOnTable);
					  if(players.get(request.getNumber()).getChipsAmount()==0) players.get(request.getNumber()).setAllIn(true);
				  }
				  else if(request.getTAG().equals("FOLD")){
					  players.get(request.getNumber()).setFolded(true);
				  }
				  else if(request.getTAG().equals("CHECK")){

				  }
				  else if(request.getTAG().equals("ALLIN")){
    				  if(players.get(request.getNumber()).getChipsAmount()>maxBetOnTable) 
    					  maxBetOnTable = players.get(request.getNumber()).getChipsAmount()+players.get(request.getNumber()).getBetAmount();
					  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()
							  +players.get(request.getNumber()).getChipsAmount());
    				  players.get(request.getNumber()).setChipsAmount(0);
    				  table.setPot(table.getPot()+players.get(request.getNumber()).getBetAmount());
    				  players.get(request.getNumber()).setAllIn(true);
				  }
				  else if(request.getTAG().equals("RAISE")){
					  if(players.get(request.getNumber()).getBetAmount()+request.getBetAmount()>maxBetOnTable){
						  maxBetOnTable = Integer.valueOf(players.get(request.getNumber()).getBetAmount()+request.getBetAmount());
						  lastToBet = (numPlayers+request.getNumber()-1)%numPlayers;
						  int counter = Integer.valueOf(lastToBet);
						  int helper = 0;
						  while(helper<numPlayers){
							  if(players.get(counter).isFolded() || players.get(counter).isAllIn() || !players.get(counter).isInGame()){
								  if(counter==0) counter=Integer.valueOf(numPlayers);
								  lastToBet=(counter-1)%numPlayers;
							  }	
							  else break;
							  counter--;
							  helper++;
						  }
						  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()+request.getBetAmount());
	    				  players.get(request.getNumber()).setChipsAmount(players.get(request.getNumber()).getChipsAmount()-request.getBetAmount());
	    				  table.setPot(table.getPot()+request.getBetAmount());
	    				  if(players.get(request.getNumber()).getChipsAmount()==0) players.get(request.getNumber()).setAllIn(true);
					  }
				  }
				  if(request.getNumber()==lastToBet){
					  bidingOver = true;
					  waitingForPlayerResponse = false;
				  } else{
    				  setNextAsBetPlayer();
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
		if(players.get((turnPlayer+1)%numPlayers).getChipsAmount()>smallBlindAmount){
			players.get((turnPlayer+1)%numPlayers).setChipsAmount(players.get((turnPlayer+1)%numPlayers).getChipsAmount()-smallBlindAmount);
			players.get((turnPlayer+1)%numPlayers).setBetAmount(smallBlindAmount);
		}
		else if(players.get((turnPlayer+1)%numPlayers).getChipsAmount()==smallBlindAmount){
			players.get((turnPlayer+1)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+1)%numPlayers).setBetAmount(smallBlindAmount);
			players.get((turnPlayer+1)%numPlayers).setAllIn(true);
		}
		else{
			players.get((turnPlayer+1)%numPlayers).setBetAmount(players.get((turnPlayer+1)%numPlayers).getChipsAmount());
			players.get((turnPlayer+1)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+1)%numPlayers).setAllIn(true);
		}
		table.setPot(table.getPot()+players.get((turnPlayer+1)%numPlayers).getBetAmount());
		players.get((turnPlayer+2)%numPlayers).setHasBigBlind(true);
		if(players.get((turnPlayer+2)%numPlayers).getChipsAmount()>bigBlindAmount){
			players.get((turnPlayer+2)%numPlayers).setBetAmount(bigBlindAmount);
			players.get((turnPlayer+2)%numPlayers).setChipsAmount(players.get((turnPlayer+2)%numPlayers).getChipsAmount()-bigBlindAmount);
		}
		else if(players.get((turnPlayer+2)%numPlayers).getChipsAmount()==bigBlindAmount)
		{
			players.get((turnPlayer+2)%numPlayers).setBetAmount(bigBlindAmount);
			players.get((turnPlayer+2)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+2)%numPlayers).setAllIn(true);
		}
		else{
			players.get((turnPlayer+2)%numPlayers).setBetAmount(players.get((turnPlayer+2)%numPlayers).getChipsAmount());
			players.get((turnPlayer+2)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+2)%numPlayers).setAllIn(true);
		}
		table.setPot(table.getPot()+players.get((turnPlayer+2)%numPlayers).getBetAmount());
		response = new SampleResponse("T", table);
		server.sendToAllTCP(response);
		for(int i=0; i<players.size(); i++){
			playersWithHiddenCards.get(i).setAllProperties(players.get(i));
		}
		response = new SampleResponse("R", playersWithHiddenCards);
		server.sendToAllTCP(response);
		turnPlayer++;
		bidingTime = true;
		bidingOver = false;
		bidingCount = 1;
		setFirstAndLastToBet();
	}
	
	public static void main(String[] args) {
		try {
			new KryoServer(2, 0, "no-limit",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}