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
	private PokerTable pokerTable = null;
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
	  this.botStrategy=botStrategy;    

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
      kryo.register(PokerTable.class);
      
      
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
					if(botsCount>0)
					{
						if(betPlayer > playersCount-1) // bots turn
						{
							players.get(betPlayer).getStrategy().whatDoIDo(this,players.get(betPlayer).getHand());
						}
					}
					if(!waitingForPlayerResponse && !bidingOver){
						if(everyoneFoldedExceptBetPlayer()){
							timeToCheckWinner();
						}
						else sendBetResponse(betPlayer);
					}
					else{
						//timer check for timeout gonna be here
					}
					if(bidingOver){
						maxBetOnTable = 0;
						for(int i=0; i<players.size();i++){
							players.get(i).setBetAmountThisRound(0);
						}
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
					pokerTable.addCard(deck.drawCard());
					pokerTable.addCard(deck.drawCard());
					pokerTable.addCard(deck.drawCard());
					bidingTime = true;
					bidingOver = false;
					setFirstAndLastToBet();
				}
				//turn
				if(turnTime){
					turnTime = false;
					pokerTable.addCard(deck.drawCard());
					bidingTime = true;
					bidingOver = false;
					setFirstAndLastToBet();
				}
				//river
				if(riverTime){
					riverTime = false;
					pokerTable.addCard(deck.drawCard());
					bidingTime = true;
					bidingOver = false;
					setFirstAndLastToBet();
				}
				//TableInfo.cardsOnTable=table.getCardList();
				response = new SampleResponse("T", pokerTable);
				server.sendToAllTCP(response);
			}
		}
	}

	
	
	private boolean everyoneFoldedExceptBetPlayer() {
		int countFolded = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).isFolded()) countFolded++;
		}
		return countFolded==players.size()-1;
	}

	private void timeToCheckWinner() {
		List<HandRank> hands = new ArrayList<HandRank>();
		List<Player> winners = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(!players.get(i).isFolded() && players.get(i).isInGame()){
				hands.add(HandOperations.findHandRank(players.get(i).getNumber(), players.get(i).getHand(), pokerTable.getCardList()));
			}
		}
		HandRankComparator handComparator = new HandRankComparator();
		Collections.sort(hands, handComparator);
		if(hands.size()>1){
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
		}
		else{
			winners.add(players.get(hands.get(0).getPlayerNumber()));
		}
		for(int i=0; i<winners.size(); i++){
			players.get(winners.get(i).getNumber())
				.setChipsAmount(players.get(winners.get(i).getNumber()).getChipsAmount() + pokerTable.getPot()/winners.size());
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
		for(int i=0; i<players.size();i++){
			if(players.get(i).getChipsAmount()==0){
				players.get(i).setInGame(false);
			}
			players.get(i).setBetAmount(0);
			players.get(i).setFolded(false);
			players.get(i).setAllIn(false);
			players.get(i).setHasBigBlind(false);
			players.get(i).setHasDealerButton(false);
			players.get(i).setHasSmallBlind(false);
			players.get(i).clearHand();
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
				betPlayer = Integer.valueOf(turnPlayer%numPlayers);
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
				setPreviousAsLastToBet();
			}
			else bidingOver = true;
		}
	}

	private void setPreviousAsLastToBet() {
		int counter;
		int helper;
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

	private boolean everybodyAllIn() {
		int countAllIn = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).isAllIn()) countAllIn++;
		}
		return countAllIn==players.size();
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
    			players.add(new Bot(numPlayers,"Bot"+String.valueOf(i),botStrategy));
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
	
	//changed to public for bot to send request
	public void handleReceived(Object object) {
		if (object instanceof SampleRequest) {
    	  SampleRequest request = (SampleRequest) object;
		  if(bidingTime && !bidingOver){
			  if(request.getNumber()==betPlayer){
				  if(request.getTAG().equals("BET")){
					  if(request.getBetAmount()>=maxBetOnTable){
						  players.get(request.getNumber()).setBetAmountThisRound(players.get(request.getNumber()).getBetAmountThisRound()+request.getBetAmount());
	    				  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()+request.getBetAmount());
	    				  players.get(request.getNumber()).setChipsAmount(players.get(request.getNumber()).getChipsAmount()-request.getBetAmount());
	    				  pokerTable.setPot(pokerTable.getPot()+request.getBetAmount());
	    				  if(request.getBetAmount()>maxBetOnTable) maxBetOnTable = Integer.valueOf(request.getBetAmount());
	    				  if(players.get(request.getNumber()).getChipsAmount()==0) players.get(request.getNumber()).setAllIn(true);
					  }
				  }
				  else if(request.getTAG().equals("CALL")){
					  int requestBetAmount = maxBetOnTable - players.get(request.getNumber()).getBetAmountThisRound();
    				  pokerTable.setPot(pokerTable.getPot()+requestBetAmount);
					  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()+requestBetAmount);
					  players.get(request.getNumber()).setBetAmountThisRound(maxBetOnTable);
    				  players.get(request.getNumber()).setChipsAmount(players.get(request.getNumber()).getChipsAmount()-requestBetAmount);
					  if(players.get(request.getNumber()).getChipsAmount()==0) players.get(request.getNumber()).setAllIn(true);
				  }
				  else if(request.getTAG().equals("FOLD")){
					  players.get(request.getNumber()).setFolded(true);
				  }
				  else if(request.getTAG().equals("CHECK")){

				  }
				  else if(request.getTAG().equals("ALLIN")){
    				  if(players.get(request.getNumber()).getChipsAmount()>maxBetOnTable){
    					  maxBetOnTable = players.get(request.getNumber()).getChipsAmount()+players.get(request.getNumber()).getBetAmount();
    					  setPreviousAsLastToBet();
    				  }
					  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()
							  +players.get(request.getNumber()).getChipsAmount());
					  players.get(request.getNumber()).setBetAmountThisRound(players.get(request.getNumber()).getBetAmountThisRound()
							  +players.get(request.getNumber()).getChipsAmount());
    				  pokerTable.setPot(pokerTable.getPot()+players.get(request.getNumber()).getChipsAmount());
    				  players.get(request.getNumber()).setChipsAmount(0);
    				  players.get(request.getNumber()).setAllIn(true);
				  }
				  else if(request.getTAG().equals("RAISE")){
					  int requestBetAmount = maxBetOnTable - players.get(request.getNumber()).getBetAmountThisRound() + request.getBetAmount();
					  if(players.get(request.getNumber()).getBetAmountThisRound()+requestBetAmount>maxBetOnTable){
						  maxBetOnTable = Integer.valueOf(players.get(request.getNumber()).getBetAmountThisRound()+requestBetAmount);
						  setPreviousAsLastToBet();
						  players.get(request.getNumber()).setBetAmount(players.get(request.getNumber()).getBetAmount()+requestBetAmount);
						  players.get(request.getNumber()).setBetAmountThisRound(players.get(request.getNumber()).getBetAmountThisRound()+requestBetAmount);
	    				  players.get(request.getNumber()).setChipsAmount(players.get(request.getNumber()).getChipsAmount()-requestBetAmount);
	    				  pokerTable.setPot(pokerTable.getPot()+requestBetAmount);
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
		pokerTable = new PokerTable();
		pokerTable.setBigBlindAmount(bigBlindAmount);
		pokerTable.setSmallBlindAmount(smallBlindAmount);
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
			players.get((turnPlayer+1)%numPlayers).setBetAmountThisRound(smallBlindAmount);
		}
		else if(players.get((turnPlayer+1)%numPlayers).getChipsAmount()==smallBlindAmount){
			players.get((turnPlayer+1)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+1)%numPlayers).setBetAmount(smallBlindAmount);
			players.get((turnPlayer+1)%numPlayers).setBetAmountThisRound(smallBlindAmount);
			players.get((turnPlayer+1)%numPlayers).setAllIn(true);
		}
		else{
			players.get((turnPlayer+1)%numPlayers).setBetAmount(players.get((turnPlayer+1)%numPlayers).getChipsAmount());
			players.get((turnPlayer+1)%numPlayers).setBetAmountThisRound(players.get((turnPlayer+1)%numPlayers).getChipsAmount());
			players.get((turnPlayer+1)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+1)%numPlayers).setAllIn(true);
		}
		pokerTable.setPot(pokerTable.getPot()+players.get((turnPlayer+1)%numPlayers).getBetAmount());
		players.get((turnPlayer+2)%numPlayers).setHasBigBlind(true);
		if(players.get((turnPlayer+2)%numPlayers).getChipsAmount()>bigBlindAmount){
			players.get((turnPlayer+2)%numPlayers).setBetAmount(bigBlindAmount);
			players.get((turnPlayer+2)%numPlayers).setBetAmountThisRound(bigBlindAmount);
			players.get((turnPlayer+2)%numPlayers).setChipsAmount(players.get((turnPlayer+2)%numPlayers).getChipsAmount()-bigBlindAmount);
		}
		else if(players.get((turnPlayer+2)%numPlayers).getChipsAmount()==bigBlindAmount)
		{
			players.get((turnPlayer+2)%numPlayers).setBetAmount(bigBlindAmount);
			players.get((turnPlayer+2)%numPlayers).setBetAmountThisRound(bigBlindAmount);
			players.get((turnPlayer+2)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+2)%numPlayers).setAllIn(true);
		}
		else{
			players.get((turnPlayer+2)%numPlayers).setBetAmount(players.get((turnPlayer+2)%numPlayers).getChipsAmount());
			players.get((turnPlayer+2)%numPlayers).setBetAmountThisRound(players.get((turnPlayer+2)%numPlayers).getChipsAmount());
			players.get((turnPlayer+2)%numPlayers).setChipsAmount(0);
			players.get((turnPlayer+2)%numPlayers).setAllIn(true);
		}
		pokerTable.setPot(pokerTable.getPot()+players.get((turnPlayer+2)%numPlayers).getBetAmount());
		response = new SampleResponse("T", pokerTable);
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
	
	public int getMaxBetOnTable()
	{
		return maxBetOnTable;
	}
	
	public boolean getFlopTime()
	{
		return flopTime;
	}
	
	public boolean getTurnTime()
	{
		return turnTime;
	}
	
	public boolean getRiverTime()
	{
		return riverTime;
	}
	
	public PokerTable getTable()
	{
		return pokerTable;
	}
	
	public int getBetPlayer()
	{
		return betPlayer;
	}
	
	public static void main(String[] args) {
		try {
			new KryoServer(2, 0, "no-limit",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}