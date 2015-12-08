package GeneralTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tp.holdem.BetComparator;
import com.tp.holdem.Card;
import com.tp.holdem.Deck;
import com.tp.holdem.HandOperations;
import com.tp.holdem.HandRank;
import com.tp.holdem.HandRankComparator;
import com.tp.holdem.HandRankingEnum;
import com.tp.holdem.Player;

public class CardTests {
	
	List<Card> cards, tableCards;
	List<Player> players;
	Card card;
	
	 @Before
	    public void setUp(){
		 	card = new Card("6", "Spade");
			cards = new ArrayList<Card>();
			tableCards = new ArrayList<Card>();
			players = new ArrayList<Player>();
	 	}
	  
	    @After
	    public void tearDown(){
	    	card = null;
	    	players = null;
    		cards = null;
    		tableCards = null;
	    }
	
	    
    @Test                                       
    public final void testGetCardSuit() {            
    	  assertEquals("Getting card suit doesnt work", "Spade", card.getSuit() );
    }  
  
    @Test                                       
    public final void testGetCardHonour() {            
    	  assertEquals("Getting card honour doesnt work", "6", card.getHonour() );
    }  
    
    @Test                                        
    public final void testSetCardSuit() {            
    	  card.setSuit("Heart");
    	  assertEquals("Setting card suit doesnt work ", "Heart", card.getSuit() );
    } 
    
    @Test                                        
    public final void testSetCardHonour() {   
    	  card.setHonour("2");
    	  assertEquals("Setting card honour doesnt work", "2", card.getHonour() );
    }     
	    
	    
	@Test                                        
    public final void testCardsValue() {          
		cards.add(new Card("Jack", "Spade"));
		cards.add(new Card("Jack", "Heart"));
		cards.add(new Card("Jack", "Diamond"));
		cards.add(new Card("Queen", "Spade"));
		cards.add(new Card("5", "Heart"));
		cards.add(new Card("Queen", "Diamond"));
		cards.add(new Card("4", "Spade"));
		cards.add(new Card("8", "Spade"));
		assertEquals(11, cards.get(0).getValue());
		assertEquals(11, cards.get(1).getValue());
		assertEquals(11, cards.get(2).getValue());
		assertEquals(12, cards.get(3).getValue());
		assertEquals(5, cards.get(4).getValue());
		assertEquals(12, cards.get(5).getValue());
		assertEquals(4, cards.get(6).getValue());
		assertEquals(8, cards.get(7).getValue());
		cards.get(7).setValue(5);
		assertEquals(5, cards.get(7).getValue());
		cards.get(7).setHonour("Ace");
		assertEquals(14, cards.get(7).getValue());
	} 
	
	@Test                                        
    public final void testGetColorCard() {   
    	  Card card = new Card("Jack", "Spade");
    	  assertEquals("black", card.getColor());
    	  Card card2 = new Card("Jack", "Diamond");
    	  assertEquals("red", card2.getColor());
    	  Card card3 = new Card();
    	  card3.setSuit("Club");
    	  assertEquals("black", card3.getColor());
    }  
	
	@Test                                        
    public final void testCardCordination() {   
    	  Card card = new Card("Jack", "Spade");
    	  assertEquals(732, card.getxCordination());
    	  assertEquals(199, card.getyCordination());
    	  Card card2 = new Card();
    	  card2.setxCordination(150);
    	  card2.setyCordination(250);
    	  assertEquals(150, card2.getxCordination());
    	  assertEquals(250, card2.getyCordination());
    	  Card card3 = new Card("Queen", "Heart");
    	  assertEquals(805, card3.getxCordination());
    	  assertEquals(101, card3.getyCordination());
    	  card3.setHonour("Ace");
    	  assertEquals(2, card3.getxCordination());
    }  
	
	@Test                                        
    public final void testHighCard() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Spade"));
		tableCards.add(new Card("6", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("9", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.HIGH_CARD, HandOperations.findHandRank(0, cards, tableCards).getHand());
		
		cards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("4", "Spade"));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("8", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("8", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
    } 
	
	@Test                                        
    public final void testFullHouse() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("4", "Spade"));
		tableCards.add(new Card("8", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("8", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(0).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());
		
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("4", "Spade"));
		tableCards.add(new Card("4", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("8", "Spade"));
		tableCards.add(new Card("8", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(0).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());
    } 
	
	@Test                                        
    public final void testOnePair() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("4", "Spade"));
		tableCards.add(new Card("7", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("10", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
    } 
	
	@Test                                        
    public final void testTwoPair() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("4", "Spade"));
		tableCards.add(new Card("8", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("10", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.TWO_PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());
		assertEquals("King", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(4).getHonour());
		
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", "Diamond"));
		cards.add(new Card("4", "Spade"));
		tableCards.add(new Card("8", "Diamond"));
		tableCards.add(new Card("8", "Spade"));
		tableCards.add(new Card("9", "Diamond"));
		tableCards.add(new Card("9", "Club"));
		tableCards.add(new Card("10", "Spade"));
		assertEquals(HandRankingEnum.TWO_PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getHonour());
		assertEquals("9", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());
		assertEquals("10", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(4).getHonour());
    } 
	
	@Test                                        
    public final void testThreeOfAKind() {  
		cards.add(new Card("4", "Club"));
		cards.add(new Card("4", "Heart"));
		tableCards.add(new Card("4", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("10", "Spade"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.THREE_OF_A_KIND, HandOperations.findHandRank(1, cards, tableCards).getHand());
	} 
	
	@Test                                        
    public final void testFourOfAKind() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("4", "Spade"));
		tableCards.add(new Card("8", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("8", "Spade"));
		tableCards.add(new Card("8", "Club"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.FOUR_OF_A_KIND, HandOperations.findHandRank(1, cards, tableCards).getHand());
    } 
	
	@Test                                        
    public final void testFlush() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("4", "Spade"));
		tableCards.add(new Card("8", "Spade"));
		tableCards.add(new Card("8", "Spade"));
		tableCards.add(new Card("10", "Spade"));
		tableCards.add(new Card("3", "Spade"));
		tableCards.add(new Card("King", "Spade"));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("Spade", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());
		
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", "Diamond"));
		cards.add(new Card("5", "Spade"));
		tableCards.add(new Card("8", "Diamond"));
		tableCards.add(new Card("8", "Diamond"));
		tableCards.add(new Card("10", "Diamond"));
		tableCards.add(new Card("3", "Diamond"));
		tableCards.add(new Card("9", "Diamond"));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("Diamond", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());
		
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", "Club"));
		cards.add(new Card("5", "Club"));
		tableCards.add(new Card("8", "Diamond"));
		tableCards.add(new Card("8", "Club"));
		tableCards.add(new Card("10", "Club"));
		tableCards.add(new Card("3", "Club"));
		tableCards.add(new Card("9", "Club"));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("Club", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());
		
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("5", "Club"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("8", "Heart"));
		tableCards.add(new Card("10", "Heart"));
		tableCards.add(new Card("3", "Heart"));
		tableCards.add(new Card("9", "Heart"));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("Heart", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());
    } 
	
	@Test                                        
    public final void testRoyalFlush() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Club"));
		tableCards.add(new Card("4", "Club"));
		tableCards.add(new Card("Queen", "Club"));
		tableCards.add(new Card("10", "Club"));
		tableCards.add(new Card("Ace", "Club"));
		tableCards.add(new Card("King", "Club"));
		assertEquals(HandRankingEnum.ROYAL_FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
    } 
	
	@Test                                        
    public final void testStraight() {          
		cards.add(new Card("4", "Heart"));
		cards.add(new Card("Jack", "Club"));
		tableCards.add(new Card("4", "Club"));
		tableCards.add(new Card("Queen", "Heart"));
		tableCards.add(new Card("10", "Club"));
		tableCards.add(new Card("Ace", "Spade"));
		tableCards.add(new Card("King", "Heart"));
		assertEquals(HandRankingEnum.STRAIGHT, HandOperations.findHandRank(1, cards, tableCards).getHand());
    } 
	
	@Test                                        
    public final void testStraightStartingWithAce() {          
		cards.add(new Card("Ace", "Heart"));
		cards.add(new Card("2", "Club"));
		tableCards.add(new Card("4", "Club"));
		tableCards.add(new Card("Queen", "Heart"));
		tableCards.add(new Card("10", "Club"));
		tableCards.add(new Card("3", "Spade"));
		tableCards.add(new Card("5", "Heart"));
		assertEquals(HandRankingEnum.STRAIGHT, HandOperations.findHandRank(1, cards, tableCards).getHand());
    } 
	
	@Test                                        
    public final void testStraightFlush() {          
		cards.add(new Card("King", "Club"));
		cards.add(new Card("2", "Heart"));
		tableCards.add(new Card("4", "Heart"));
		tableCards.add(new Card("6", "Heart"));
		tableCards.add(new Card("10", "Club"));
		tableCards.add(new Card("3", "Heart"));
		tableCards.add(new Card("5", "Heart"));
		assertEquals(HandRankingEnum.STRAIGHT_FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
    } 
	
}
