package ServerClientTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.BetComparator;
import com.tp.holdem.Bot;
import com.tp.holdem.Deck;
import com.tp.holdem.HandOperations;
import com.tp.holdem.HandRank;
import com.tp.holdem.HandRankComparator;
import com.tp.holdem.Player;
import com.tp.holdem.PokerTable;
import com.tp.holdem.SampleRequest;
import com.tp.holdem.SampleResponse;
import com.tp.holdem.Strategy;

public class FakeServer{
	
	private volatile boolean gameStarted = false;
	private List<Player> players;
	private int numPlayers = 0;
	private int lastToBet = 0;
	private int turnPlayer = 0;
	private int betPlayer = 0;
	private boolean bidingOver = false;
	private int bidingCount = 0;
	private Deck deck = null;
	private PokerTable pokerTable = null;
	private boolean bidingTime = false;
	private int smallBlindAmount = 20;
	private int bigBlindAmount = 40;
	private int maxBetOnTable = 0;
	private boolean newHand = false;
	public FakeServer(List<Player> players) throws Exception {
		this.players = players;
		this.numPlayers = players.size();
    };

	public boolean everyoneFoldedExceptBetPlayer() {
		int countFolded = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).isFolded()) countFolded++;
		}
		return countFolded==players.size()-1;
	}
	
	public void timeToCheckWinner() {
		List<HandRank> hands = new ArrayList<HandRank>();
		List<Player> winners = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(!players.get(i).isFolded() && players.get(i).isInGame()){
				hands.add(HandOperations.findHandRank(players.get(i).getNumber(), players.get(i).getHand(), pokerTable.getCardList()));
				players.get(i).setHandRank(HandOperations.findHandRank(players.get(i).getNumber(), players.get(i).getHand(), pokerTable.getCardList()));
			}
		}
		HandRankComparator handComparator = new HandRankComparator();
		Collections.sort(hands, handComparator);
		if(hands.size()>1){
			for(int i=hands.size()-1; i>0; i--){
				if(players.get(hands.get(i).getPlayerNumber()).getBetAmountThisRound()==maxBetOnTable){
					if(handComparator.compare(hands.get(i), hands.get(i-1))==1){
						for(int j=0; j<i; j++){
							hands.remove(0);
						}
						break;
					}
				}
			}
		}
		int currentPotNumber = 0;
		for(int i=0; i<hands.size(); i++){
			winners.add(0, players.get(hands.get(i).getPlayerNumber()));
		}
		Collections.sort(winners, new BetComparator());
		for(int i=0; i<winners.size(); i++){
			winners.get(i).setFromWhichPot(currentPotNumber);
			if(i<winners.size()-1){
				if(winners.get(i).getBetAmount()!=winners.get(i+1).getBetAmount()) currentPotNumber++;
			}
		}
		if(winners.size()==1){
			//OW - one winner
			players.get(winners.get(0).getNumber())
				.setChipsAmount(players.get(winners.get(0).getNumber()).getChipsAmount() + pokerTable.getPot());
		}
		else{
			//MW - multiple winners
			int howManyInPreviousPots = 0;
			for(int i=0; i<=currentPotNumber; i++){
				int howManyPeopleInSamePot = howManyPeopleInSamePot(i);
				howManyInPreviousPots+=howManyPeopleInSamePot;
				int howToSplit = 0;
				for(int j=howManyInPreviousPots-howManyPeopleInSamePot; j<howManyInPreviousPots; j++){
					if(j<winners.size()-1){
						if(handComparator.compare(winners.get(j).getHandRank(), winners.get(j+1).getHandRank())==1){
							howToSplit++;
							break;
						} else {
							howToSplit++;
						}
					} else {
						if(handComparator.compare(winners.get(j).getHandRank(), winners.get(j-1).getHandRank())!=1){
							howToSplit++;
						}
					}
				}
				int howMuchPerOne = findAmountChipsForAllInPot(i, winners.get(howManyInPreviousPots-1).getBetAmount())/howToSplit;
				for(int j=howManyInPreviousPots-howManyPeopleInSamePot; j<howManyInPreviousPots-howManyPeopleInSamePot+howToSplit; j++)
				{
					players.get(winners.get(j).getNumber())
						.setChipsAmount(players.get(winners.get(j).getNumber()).getChipsAmount() + howMuchPerOne);
				}
			}
		}
	}

	public int findAmountChipsForAllInPot(int potNumber, int betAmount) {
		int wholeAmount = 0;
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getFromWhichPot()>=potNumber || players.get(i).getFromWhichPot()==-1)
			{
				if(players.get(i).getBetAmount()<betAmount){
					wholeAmount+=players.get(i).getBetAmount();
					players.get(i).setBetAmount(0);
				} else {
					wholeAmount+=betAmount;
					players.get(i).setBetAmount(players.get(i).getBetAmount()-betAmount);
				}
			}
		}
		return wholeAmount;
	}

	public int howManyPeopleInSamePot(int fromWhichPot) {
		int amount = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).isInGame() && !players.get(i).isFolded()){
				if(players.get(i).getFromWhichPot()==fromWhichPot) amount++;
			}
		}
		return amount;
	}

	public void resetAfterRound() {
		if(!notEnoughPlayers()){
			maxBetOnTable = Integer.valueOf(bigBlindAmount);
			newHand = true;	
		}
		else{
			gameStarted = false;
		}
	}

	public boolean notEnoughPlayers() {
		int playersInGameCount = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).isInGame()) playersInGameCount++;
		}
		return playersInGameCount<=1;
	}

	public void setFirstAndLastToBet() {
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

	public void setPreviousAsLastToBet() {
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

	public boolean everybodyAllIn() {
		int countAllIn = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).isAllIn()) countAllIn++;
		}
		return countAllIn==players.size();
	}
    
	public void setNextAsBetPlayer() {
		betPlayer=(betPlayer+1)%numPlayers;
		for(int i=betPlayer%numPlayers; i<players.size();i++){
			if(players.get(i).isFolded() || !players.get(i).isInGame() || players.get(i).isAllIn()){
				betPlayer=(betPlayer+1)%numPlayers;
			}
			else break;
		}
	}

	//changed to public for bot to send request
	public void handleReceived(Object object) {
		if (object instanceof SampleRequest) {
    	  SampleRequest request = (SampleRequest) object;
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
	  }
    }
	
	public void initiateNewHand() {
		deck = new Deck();
		pokerTable = new PokerTable();
		pokerTable.setBigBlindAmount(bigBlindAmount);
		pokerTable.setSmallBlindAmount(smallBlindAmount);
		deck.dealCards(2, players);
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
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getBetAmountThisRound()>maxBetOnTable){
				maxBetOnTable = players.get(i).getBetAmountThisRound();
			}
		}
		pokerTable.setPot(pokerTable.getPot()+players.get((turnPlayer+2)%numPlayers).getBetAmount());
		turnPlayer++;
		bidingTime = true;
		bidingOver = false;
		bidingCount = 1;
		setFirstAndLastToBet();
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getLastToBet() {
		return lastToBet;
	}


	public int getTurnPlayer() {
		return turnPlayer;
	}

	public void setTurnPlayer(int turnPlayer) {
		this.turnPlayer = turnPlayer;
	}

	public int getBetPlayer() {
		return betPlayer;
	}

	public void setBetPlayer(int betPlayer) {
		this.betPlayer = betPlayer;
	}

	public boolean isBidingOver() {
		return bidingOver;
	}

	public int getBidingCount() {
		return bidingCount;
	}

	public void setBidingCount(int bidingCount) {
		this.bidingCount = bidingCount;
	}

	public Deck getDeck() {
		return deck;
	}

	public PokerTable getPokerTable() {
		return pokerTable;
	}

	public void setPokerTable(PokerTable pokerTable) {
		this.pokerTable = pokerTable;
	}

	public boolean isBidingTime() {
		return bidingTime;
	}

	public int getMaxBetOnTable() {
		return maxBetOnTable;
	}

	public void setMaxBetOnTable(int maxBetOnTable) {
		this.maxBetOnTable = maxBetOnTable;
	}

	public boolean isNewHand() {
		return newHand;
	}

	public void setNewHand(boolean newHand) {
		this.newHand = newHand;
	}
}
