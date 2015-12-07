package com.tp.holdem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CardTests {

	@Test                                        
    public final void testSetCardSuit() {          
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("Jack", "Spade"));
		cards.add(new Card("Jack", "Heart"));
		cards.add(new Card("Jack", "Diamond"));
		cards.add(new Card("Queen", "Spade"));
		cards.add(new Card("5", "Heart"));
		cards.add(new Card("Queen", "Diamond"));
		cards.add(new Card("4", "Spade"));
		cards.add(new Card("8", "Spade"));
		Collections.sort(cards, new CardComparator());
		//for(Card card : cards){
	//		System.out.println(card.getHonour());
	//	}
    } 
	
	@Test                                        
    public final void testHand() {          
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Spade"));
		List<Card> tableCards = new ArrayList<Card>();
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		System.out.println(HandOperations.findHandRank(0, cards, tableCards));
    } 
	
	@Test
	public final void compareSingleHand(){
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("5", "Spade"));
		List<Card> tableCards = new ArrayList<Card>();
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("7", "Spade"));
		List<HandRank> hands = new ArrayList<HandRank>();
		hands.add(HandOperations.findHandRank(0, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card("Queen", "Heart"));
		cards.add(new Card("King", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("Ace", "Heart"));
		tableCards.add(new Card("10", "Heart"));
		tableCards.add(new Card("Jack", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("7", "Spade"));
		hands.add(HandOperations.findHandRank(1, cards, tableCards));
		HandRankComparator comp = new HandRankComparator();
		if(comp.compare(hands.get(1), hands.get(1))==0){
			System.out.println(1);
		}
		else System.out.println(0);
	}
	@Test                                        
    public final void handCompare() {          
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Spade"));
		List<Card> tableCards = new ArrayList<Card>();
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		List<HandRank> hands = new ArrayList<HandRank>();
		
		hands.add(HandOperations.findHandRank(0, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card("10", "Heart"));
		cards.add(new Card("Jack", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", "Heart"));
		tableCards.add(new Card("4", "Heart"));
		tableCards.add(new Card("Ace", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		hands.add(HandOperations.findHandRank(1, cards, tableCards));

		cards = new ArrayList<Card>();
		cards.add(new Card("10", "Heart"));
		cards.add(new Card("Jack", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", "Heart"));
		tableCards.add(new Card("4", "Heart"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("9", "Spade"));
		hands.add(HandOperations.findHandRank(2, cards, tableCards));
		
		cards = new ArrayList<Card>();
		cards.add(new Card("10", "Heart"));
		cards.add(new Card("Jack", "Heart"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", "Heart"));
		tableCards.add(new Card("4", "Heart"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("9", "Spade"));
		hands.add(HandOperations.findHandRank(3, cards, tableCards));
		
		Collections.sort(hands, new HandRankComparator());
		for(HandRank hand : hands){
			System.out.println(hand);
		}
    } 
	
	@Test                                        
    public final void testSorting() {          
		List<HandRank> hands = new ArrayList<HandRank>();
		List<Player> players = new ArrayList<Player>();
		Player player0 = new Player(0);
		player0.setBetAmount(500);
		Player player1 = new Player(1);
		player1.setBetAmount(600);
		Player player2 = new Player(2);
		player2.setBetAmount(1000);
		Player player3 = new Player(3);
		player3.setBetAmount(600);
		players.add(player0); players.add(player1); players.add(player2);players.add(player3);
		hands.add(new HandRank(0, HandRankingEnum.ROYAL_FLUSH, new ArrayList<Card>()));
		hands.add(new HandRank(1, HandRankingEnum.FLUSH, new ArrayList<Card>()));
		hands.add(new HandRank(2, HandRankingEnum.STRAIGHT, new ArrayList<Card>()));
		hands.add(new HandRank(3, HandRankingEnum.FOUR_OF_A_KIND, new ArrayList<Card>()));
		Collections.sort(hands, new HandRankComparator());
		for(int i=0; i<hands.size();i++){
			System.out.println(hands.get(i));
		}
		List<Player> winners = new ArrayList<Player>();
		for(int i=0; i<hands.size();i++){
			winners.add(players.get(hands.get(i).getPlayerNumber()));
		}
		Collections.sort(winners, new BetComparator());
		for(int i=0; i<winners.size();i++){
			System.out.println(winners.get(i).getNumber()+" "+winners.get(i).getBetAmount());
		}
    } 
	
	List<Player> players = new ArrayList<Player>();
	@Test                                        
    public final void sidePotsTest() throws Exception { 
		Player player0 = new Player(0);
		player0.setBetAmount(100);
		player0.addCard(new Card("King", "Spade"));
		player0.addCard(new Card("Ace", "Spade"));
		Player player1 = new Player(1);
		player1.setBetAmount(200);
		player1.addCard(new Card("8", "Spade"));
		player1.addCard(new Card("3", "Club"));
		Player player2 = new Player(2);
		player2.setBetAmount(300);
		player2.addCard(new Card("3", "Hearts"));
		player2.addCard(new Card("4", "Hearts"));
		Player player3 = new Player(3);
		player3.setBetAmount(100);
		player3.addCard(new Card("King", "Spade"));
		player3.addCard(new Card("5", "Diamond"));
		Player player4 = new Player(4);
		player4.setBetAmount(200);
		player4.addCard(new Card("3", "Spade"));
		player4.addCard(new Card("3", "Club"));
		player0.setChipsAmount(0);
		player1.setChipsAmount(0);
		player2.setChipsAmount(0);
		player3.setChipsAmount(0);
		player4.setChipsAmount(0);
		List<Card> pokerTable = new ArrayList<Card>();
		pokerTable.add(new Card("Jack", "Spade"));
		pokerTable.add(new Card("Queen", "Spade"));
		pokerTable.add(new Card("9", "Spade"));
		pokerTable.add(new Card("10", "Spade"));
		pokerTable.add(new Card("3", "Diamond"));
		players.add(player0);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		List<HandRank> hands = new ArrayList<HandRank>();
		List<Player> winners = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			hands.add(HandOperations.findHandRank(players.get(i).getNumber(), players.get(i).getHand(), pokerTable));
			players.get(i).setHandRank(HandOperations.findHandRank(players.get(i).getNumber(), players.get(i).getHand(), pokerTable));
		}
		HandRankComparator handComparator = new HandRankComparator();
		Collections.sort(hands, handComparator);
		int maxBetOnTable = 300;
		if(hands.size()>1){
			for(int i=hands.size()-1; i>0; i--){
				if(players.get(hands.get(i).getPlayerNumber()).getBetAmount()==maxBetOnTable){
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
						if(handComparator.compare(winners.get(j).getHandRank(), winners.get(j-1).getHandRank())==-1){
							howToSplit++;
						}
					}
				}
				int howMuchPerOne = findAmountChipsForAllInPot(i, winners.get(howManyInPreviousPots-1).getBetAmount())/howToSplit;
				for(int j=howManyInPreviousPots-howManyPeopleInSamePot; j<howManyInPreviousPots-howManyPeopleInSamePot+howToSplit; j++){
					players.get(winners.get(j).getNumber())
						.setChipsAmount(players.get(winners.get(j).getNumber()).getChipsAmount() + howMuchPerOne);
				}
			}
		}
		for(int i=0; i<players.size(); i++){
			System.out.println(players.get(i).getNumber()+"   - - "+players.get(i).getChipsAmount());
		}
	}
	
	private int findAmountChipsForAllInPot(int potNumber, int betAmount) {
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

	private int howManyPeopleInSamePot(int fromWhichPot) {
		int amount = 0;
		for(int i=0; i<players.size();i++){
			if(players.get(i).getFromWhichPot()==fromWhichPot) amount++;
		}
		return amount;
	}
}
