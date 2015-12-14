package GeneralTests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tp.holdem.BetComparator;
import com.tp.holdem.Card;
import com.tp.holdem.HandOperations;
import com.tp.holdem.HandRank;
import com.tp.holdem.HandRankComparator;
import com.tp.holdem.HandRankingEnum;
import com.tp.holdem.Player;

public class ComparatorsTests {

	List<Card> cards, tableCards;
	List<HandRank> hands;
	HandRankComparator comp;
	List<Player> players;
	HandRank hand1, hand2;
	List<HandRankingEnum> allRanks;
	
    @Before
    public void setUp(){
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		hands = new ArrayList<HandRank>();
		comp = new HandRankComparator();
		players = new ArrayList<Player>();
		hand1 = new HandRank();
		hand2 = new HandRank();
		allRanks = new ArrayList<HandRankingEnum>();
		allRanks.add(HandRankingEnum.HIGH_CARD);
		allRanks.add(HandRankingEnum.PAIR);
		allRanks.add(HandRankingEnum.TWO_PAIR);
		allRanks.add(HandRankingEnum.THREE_OF_A_KIND);
		allRanks.add(HandRankingEnum.STRAIGHT);
		allRanks.add(HandRankingEnum.FLUSH);
		allRanks.add(HandRankingEnum.FULL_HOUSE);
		allRanks.add(HandRankingEnum.FOUR_OF_A_KIND);
		allRanks.add(HandRankingEnum.STRAIGHT_FLUSH);
		allRanks.add(HandRankingEnum.ROYAL_FLUSH);
 	}
	  
	@After
	public void tearDown(){
		hands = null;
		allRanks = null;
		comp = null;
		players = null;
		cards = null;
		tableCards = null;
		hand1 = null;
		hand2 = null;
	}
	
	@Test
	public final void testCompareTwoHands(){
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("5", "Spade"));
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("7", "Spade"));
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
		assertEquals(-1, comp.compare(hands.get(0), hands.get(1) ));
		assertEquals(1, comp.compare(hands.get(1), hands.get(0) ));
		assertEquals(0, comp.compare(hands.get(0), hands.get(0) ));
	}
	@Test                                        
	public final void testSortingByHandPower() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Spade"));
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
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
		assertEquals(0, hands.get(0).getPlayerNumber());
		assertEquals(2, hands.get(1).getPlayerNumber());
		assertEquals(3, hands.get(2).getPlayerNumber());
		assertEquals(1, hands.get(3).getPlayerNumber());
    } 
		
	@Test                                        
    public final void testSortingByBet() {          
		Player player0 = new Player(0);
		player0.setBetAmount(500);
		Player player1 = new Player(1);
		player1.setBetAmount(600);
		Player player2 = new Player(2);
		player2.setBetAmount(1000);
		Player player3 = new Player(3);
		player3.setBetAmount(600);
		players.add(player0);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		hands.add(new HandRank(0, HandRankingEnum.ROYAL_FLUSH, new ArrayList<Card>()));
		hands.add(new HandRank(1, HandRankingEnum.FLUSH, new ArrayList<Card>()));
		hands.add(new HandRank(2, HandRankingEnum.STRAIGHT, new ArrayList<Card>()));
		hands.add(new HandRank(3, HandRankingEnum.FOUR_OF_A_KIND, new ArrayList<Card>()));
		Collections.sort(hands, new HandRankComparator());
		List<Player> winners = new ArrayList<Player>();
		for(int i=0; i<hands.size();i++){
			winners.add(players.get(hands.get(i).getPlayerNumber()));
		}
		Collections.sort(winners, new BetComparator());
		assertEquals(500, winners.get(0).getBetAmount());
		assertEquals(600, winners.get(1).getBetAmount());
		assertEquals(600, winners.get(2).getBetAmount());
		assertEquals(1000, winners.get(3).getBetAmount());
    } 
	
	@Test                                        
    public final void testCompareHandsOnlyByNames() {     
		for(int i=0; i<allRanks.size(); i++){
			hand1.setHand(allRanks.get(i));
			for(int j=0; j<allRanks.size(); j++){
				hand2.setHand(allRanks.get(j));
				if(i==j){
					continue;
				} else if(i<j){
					assertEquals(-1, comp.compare(hand1, hand2));
					assertEquals(1, comp.compare(hand2, hand1));
				} else {
					assertEquals(1, comp.compare(hand1, hand2));
					assertEquals(-1, comp.compare(hand2, hand1));
				}
			}
		}
    } 
	
	@Test                                        
    public final void testCompareHighCardByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.HIGH_CARD);
		hand2.setHand(HandRankingEnum.HIGH_CARD);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("5", "Heart"));
		cards1.add(new Card("8", "Club"));
		cards1.add(new Card("9", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Spade"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("8", "Club"));
		cards2.add(new Card("9", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Ace");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("10");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("10");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    } 
	
	@Test                                        
    public final void testCompareOnePairsByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.PAIR);
		hand2.setHand(HandRankingEnum.PAIR);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("5", "Heart"));
		cards1.add(new Card("8", "Club"));
		cards1.add(new Card("Jack", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Spade"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("8", "Club"));
		cards2.add(new Card("Jack", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Ace");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(3).setHonour("Ace");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareOnePairsByCardsMakingDeckSecond() {     
		hand1.setHand(HandRankingEnum.PAIR);
		hand2.setHand(HandRankingEnum.PAIR);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("5", "Heart"));
		cards1.add(new Card("8", "Club"));
		cards1.add(new Card("8", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Spade"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("8", "Club"));
		cards2.add(new Card("8", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("9");
		hand2.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("9");
		hand1.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Queen");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Queen");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareOnePairsByCardsMakingDeckThird() {     
		hand1.setHand(HandRankingEnum.PAIR);
		hand2.setHand(HandRankingEnum.PAIR);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("5", "Heart"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("8", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Spade"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("8", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour("6");
		hand2.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(2).setHonour("6");
		hand1.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Queen");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Queen");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareOnePairsByCardsMakingDeckFourth() {     
		hand1.setHand(HandRankingEnum.PAIR);
		hand2.setHand(HandRankingEnum.PAIR);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("2", "Heart"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("8", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Spade"));
		cards2.add(new Card("2", "Heart"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("8", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("4");
		hand2.getCardsThatMakeDeck().get(1).setHonour("4");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("4");
		hand1.getCardsThatMakeDeck().get(1).setHonour("4");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour("7");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(2).setHonour("7");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Queen");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Queen");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareTwoPairsByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.TWO_PAIR);
		hand2.setHand(HandRankingEnum.TWO_PAIR);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("8", "Heart"));
		cards1.add(new Card("8", "Club"));
		cards1.add(new Card("Jack", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Spade"));
		cards2.add(new Card("8", "Heart"));
		cards2.add(new Card("8", "Club"));
		cards2.add(new Card("Jack", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Ace");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(3).setHonour("Ace");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour("Queen");
		hand2.getCardsThatMakeDeck().get(1).setHonour("Queen");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("King");
		hand2.getCardsThatMakeDeck().get(3).setHonour("King");
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(2).setHonour("8");
		hand2.getCardsThatMakeDeck().get(1).setHonour("8");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareTwoPairsByCardsMakingDeckSecond() {     
		hand1.setHand(HandRankingEnum.TWO_PAIR);
		hand2.setHand(HandRankingEnum.TWO_PAIR);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", "Spade"));
		cards1.add(new Card("3", "Heart"));
		cards1.add(new Card("8", "Club"));
		cards1.add(new Card("Jack", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", "Spade"));
		cards2.add(new Card("3", "Heart"));
		cards2.add(new Card("8", "Club"));
		cards2.add(new Card("Jack", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Ace");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(3).setHonour("Ace");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("5");
		hand2.getCardsThatMakeDeck().get(1).setHonour("5");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("King");
		hand2.getCardsThatMakeDeck().get(3).setHonour("King");
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(0).setHonour("3");
		hand2.getCardsThatMakeDeck().get(1).setHonour("3");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(2).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareTwoPairsByCardsMakingDeckThird() {     
		hand1.setHand(HandRankingEnum.TWO_PAIR);
		hand2.setHand(HandRankingEnum.TWO_PAIR);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", "Spade"));
		cards1.add(new Card("3", "Heart"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("5", "Spade"));
		cards1.add(new Card("Jack", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", "Spade"));
		cards2.add(new Card("3", "Heart"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("5", "Spade"));
		cards2.add(new Card("Jack", "Spade"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("7");
		hand2.getCardsThatMakeDeck().get(2).setHonour("7");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("7");
		hand1.getCardsThatMakeDeck().get(2).setHonour("7");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("4");
		hand2.getCardsThatMakeDeck().get(1).setHonour("4");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("6");
		hand2.getCardsThatMakeDeck().get(2).setHonour("6");
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("7");
		hand2.getCardsThatMakeDeck().get(2).setHonour("7");
		hand2.getCardsThatMakeDeck().get(0).setHonour("3");
		hand2.getCardsThatMakeDeck().get(1).setHonour("3");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareThreeOfAKindByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.THREE_OF_A_KIND);
		hand2.setHand(HandRankingEnum.THREE_OF_A_KIND);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("8", "Heart"));
		cards1.add(new Card("Jack", "Club"));
		cards1.add(new Card("Jack", "Spade"));
		cards1.add(new Card("Jack", "Heart"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Spade"));
		cards2.add(new Card("8", "Heart"));
		cards2.add(new Card("Jack", "Club"));
		cards2.add(new Card("Jack", "Spade"));
		cards2.add(new Card("Jack", "Heart"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(2).setHonour("Ace");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(3).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(2).setHonour("Ace");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareThreeOfAKindByCardsMakingDeckSecond() {     
		hand1.setHand(HandRankingEnum.THREE_OF_A_KIND);
		hand2.setHand(HandRankingEnum.THREE_OF_A_KIND);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("4", "Spade"));
		cards1.add(new Card("4", "Heart"));
		cards1.add(new Card("4", "Club"));
		cards1.add(new Card("10", "Spade"));
		cards1.add(new Card("Queen", "Heart"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("4", "Spade"));
		cards2.add(new Card("4", "Heart"));
		cards2.add(new Card("4", "Club"));
		cards2.add(new Card("10", "Spade"));
		cards2.add(new Card("Queen", "Heart"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("6");
		hand2.getCardsThatMakeDeck().get(1).setHonour("6");
		hand2.getCardsThatMakeDeck().get(2).setHonour("6");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("6");
		hand1.getCardsThatMakeDeck().get(1).setHonour("6");
		hand1.getCardsThatMakeDeck().get(2).setHonour("6");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("Jack");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("Jack");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareThreeOfAKindByCardsMakingDeckThird() {     
		hand1.setHand(HandRankingEnum.THREE_OF_A_KIND);
		hand2.setHand(HandRankingEnum.THREE_OF_A_KIND);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("4", "Spade"));
		cards1.add(new Card("7", "Heart"));
		cards1.add(new Card("7", "Club"));
		cards1.add(new Card("7", "Spade"));
		cards1.add(new Card("Queen", "Heart"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("4", "Spade"));
		cards2.add(new Card("7", "Heart"));
		cards2.add(new Card("7", "Club"));
		cards2.add(new Card("7", "Spade"));
		cards2.add(new Card("Queen", "Heart"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("8");
		hand2.getCardsThatMakeDeck().get(2).setHonour("8");
		hand2.getCardsThatMakeDeck().get(3).setHonour("8");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("8");
		hand1.getCardsThatMakeDeck().get(2).setHonour("8");
		hand1.getCardsThatMakeDeck().get(3).setHonour("8");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("5");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("5");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareStraightByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.STRAIGHT);
		hand2.setHand(HandRankingEnum.STRAIGHT);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", "Spade"));
		cards1.add(new Card("4", "Heart"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("6", "Spade"));
		cards1.add(new Card("7", "Heart"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", "Spade"));
		cards2.add(new Card("4", "Heart"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("6", "Spade"));
		cards2.add(new Card("7", "Heart"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("8");
		hand2.getCardsThatMakeDeck().get(3).setHonour("7");
		hand2.getCardsThatMakeDeck().get(2).setHonour("6");
		hand2.getCardsThatMakeDeck().get(1).setHonour("5");
		hand2.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("8");
		hand1.getCardsThatMakeDeck().get(3).setHonour("7");
		hand1.getCardsThatMakeDeck().get(2).setHonour("6");
		hand1.getCardsThatMakeDeck().get(1).setHonour("5");
		hand1.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		
		hand2.getCardsThatMakeDeck().get(4).setHonour("5");
		hand2.getCardsThatMakeDeck().get(3).setHonour("4");
		hand2.getCardsThatMakeDeck().get(2).setHonour("3");
		hand2.getCardsThatMakeDeck().get(1).setHonour("2");
		hand2.getCardsThatMakeDeck().get(0).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(4).setHonour("6");
		hand1.getCardsThatMakeDeck().get(3).setHonour("5");
		hand1.getCardsThatMakeDeck().get(2).setHonour("4");
		hand1.getCardsThatMakeDeck().get(1).setHonour("3");
		hand1.getCardsThatMakeDeck().get(0).setHonour("2");
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareFlushByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.FLUSH);
		hand2.setHand(HandRankingEnum.FLUSH);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("5", "Spade"));
		cards1.add(new Card("7", "Spade"));
		cards1.add(new Card("9", "Spade"));
		cards1.add(new Card("10", "Spade"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Heart"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("7", "Heart"));
		cards2.add(new Card("9", "Heart"));
		cards2.add(new Card("10", "Heart"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Jack");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Jack");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("10");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("10");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour("8");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(2).setHonour("8");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("6");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("3");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareFullHouseByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.FULL_HOUSE);
		hand2.setHand(HandRankingEnum.FULL_HOUSE);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Club"));
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("5", "Spade"));
		cards1.add(new Card("5", "Club"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Club"));
		cards2.add(new Card("2", "Heart"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("5", "Club"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(2).setHonour("Jack");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(3).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(2).setHonour("Jack");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("10");
		hand2.getCardsThatMakeDeck().get(3).setHonour("10");
		hand2.getCardsThatMakeDeck().get(2).setHonour("10");
		hand2.getCardsThatMakeDeck().get(1).setHonour("8");
		hand2.getCardsThatMakeDeck().get(0).setHonour("8");
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("10");
		hand1.getCardsThatMakeDeck().get(3).setHonour("10");
		hand1.getCardsThatMakeDeck().get(2).setHonour("10");
		hand1.getCardsThatMakeDeck().get(1).setHonour("8");
		hand1.getCardsThatMakeDeck().get(0).setHonour("8");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("9");
		hand2.getCardsThatMakeDeck().get(0).setHonour("9");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("9");
		hand1.getCardsThatMakeDeck().get(0).setHonour("9");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareFullHouseByCardsMakingDeckSecond() {     
		hand1.setHand(HandRankingEnum.FULL_HOUSE);
		hand2.setHand(HandRankingEnum.FULL_HOUSE);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Club"));
		cards1.add(new Card("2", "Spade"));
		cards1.add(new Card("2", "Club"));
		cards1.add(new Card("Queen", "Spade"));
		cards1.add(new Card("Queen", "Club"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Club"));
		cards2.add(new Card("2", "Heart"));
		cards2.add(new Card("2", "Club"));
		cards2.add(new Card("Queen", "Heart"));
		cards2.add(new Card("Queen", "Club"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(1).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(2).setHonour("Jack");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(1).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(2).setHonour("Jack");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("10");
		hand2.getCardsThatMakeDeck().get(1).setHonour("10");
		hand2.getCardsThatMakeDeck().get(2).setHonour("10");
		hand2.getCardsThatMakeDeck().get(3).setHonour("King");
		hand2.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("10");
		hand1.getCardsThatMakeDeck().get(1).setHonour("10");
		hand1.getCardsThatMakeDeck().get(2).setHonour("10");
		hand1.getCardsThatMakeDeck().get(3).setHonour("King");
		hand1.getCardsThatMakeDeck().get(4).setHonour("King");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour("Ace");
		hand2.getCardsThatMakeDeck().get(0).setHonour("Ace");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(0).setHonour("Ace");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareFourOfAKindByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.FOUR_OF_A_KIND);
		hand2.setHand(HandRankingEnum.FOUR_OF_A_KIND);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", "Club"));
		cards1.add(new Card("5", "Spade"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("5", "Spade"));
		cards1.add(new Card("5", "Club"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", "Club"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("5", "Club"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(3).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(2).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(1).setHonour("Jack");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(3).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(2).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(1).setHonour("Jack");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareFourOfAKindByCardsMakingDeckSecond() {     
		hand1.setHand(HandRankingEnum.FOUR_OF_A_KIND);
		hand2.setHand(HandRankingEnum.FOUR_OF_A_KIND);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("5", "Spade"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("5", "Spade"));
		cards1.add(new Card("King", "Club"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("5", "Heart"));
		cards2.add(new Card("King", "Club"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(2).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(1).setHonour("Jack");
		hand2.getCardsThatMakeDeck().get(0).setHonour("Jack");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(2).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(1).setHonour("Jack");
		hand1.getCardsThatMakeDeck().get(0).setHonour("Jack");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("Ace");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("Ace");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareStraightFlushByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.STRAIGHT_FLUSH);
		hand2.setHand(HandRankingEnum.STRAIGHT_FLUSH);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", "Club"));
		cards1.add(new Card("4", "Club"));
		cards1.add(new Card("5", "Club"));
		cards1.add(new Card("6", "Club"));
		cards1.add(new Card("7", "Club"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", "Club"));
		cards2.add(new Card("4", "Club"));
		cards2.add(new Card("5", "Club"));
		cards2.add(new Card("6", "Club"));
		cards2.add(new Card("7", "Club"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour("8");
		hand2.getCardsThatMakeDeck().get(3).setHonour("7");
		hand2.getCardsThatMakeDeck().get(2).setHonour("6");
		hand2.getCardsThatMakeDeck().get(1).setHonour("5");
		hand2.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour("8");
		hand1.getCardsThatMakeDeck().get(3).setHonour("7");
		hand1.getCardsThatMakeDeck().get(2).setHonour("6");
		hand1.getCardsThatMakeDeck().get(1).setHonour("5");
		hand1.getCardsThatMakeDeck().get(0).setHonour("4");
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		
		hand2.getCardsThatMakeDeck().get(4).setHonour("5");
		hand2.getCardsThatMakeDeck().get(3).setHonour("4");
		hand2.getCardsThatMakeDeck().get(2).setHonour("3");
		hand2.getCardsThatMakeDeck().get(1).setHonour("2");
		hand2.getCardsThatMakeDeck().get(0).setHonour("Ace");
		hand1.getCardsThatMakeDeck().get(4).setHonour("6");
		hand1.getCardsThatMakeDeck().get(3).setHonour("5");
		hand1.getCardsThatMakeDeck().get(2).setHonour("4");
		hand1.getCardsThatMakeDeck().get(1).setHonour("3");
		hand1.getCardsThatMakeDeck().get(0).setHonour("2");
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
    }
	
	@Test                                        
    public final void testCompareRoyalFlushByCardsMakingDeck() {     
		hand1.setHand(HandRankingEnum.ROYAL_FLUSH);
		hand2.setHand(HandRankingEnum.ROYAL_FLUSH);
		List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("10", "Club"));
		cards1.add(new Card("Jack", "Club"));
		cards1.add(new Card("Queen", "Club"));
		cards1.add(new Card("King", "Club"));
		cards1.add(new Card("Ace", "Club"));
		List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("10", "Club"));
		cards2.add(new Card("Jack", "Club"));
		cards2.add(new Card("Queen", "Club"));
		cards2.add(new Card("King", "Club"));
		cards2.add(new Card("Ace", "Club"));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
    }
}
