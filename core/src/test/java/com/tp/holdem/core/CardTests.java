package com.tp.holdem.core;

import com.tp.holdem.core.model.Card;
import com.tp.holdem.core.model.HandRankingEnum;
import com.tp.holdem.core.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CardTests {

	private static final String KING = "King";
	private static final String ACE = "Ace";
	private static final String QUEEN = "Queen";
	private static final String JACK = "Jack";
	private static final String DIAMOND = "Diamond";
	private static final String CLUB = "Club";
	private static final String HEART = "Heart";
	private static final String SPADE = "Spade";
	transient List<Card> cards, tableCards;
	transient List<Player> players;
	transient Card card;

	@Before
	public void setUp() {
		card = new Card("6", SPADE);
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		players = new ArrayList<Player>();
	}

	@After
	public void tearDown() {
		card = null;
		players = null;
		cards = null;
		tableCards = null;
	}

	@Test
	public final void testGetCardSuit() {
		assertEquals("Getting card suit doesnt work", SPADE, card.getSuit());
	}

	@Test
	public final void testGetCardHonour() {
		assertEquals("Getting card honour doesnt work", "6", card.getHonour());
	}

	@Test
	public final void testSetCardSuit() {
		card.setSuit(HEART);
		assertEquals("Setting card suit doesnt work ", HEART, card.getSuit());
	}

	@Test
	public final void testSetCardHonour() {
		card.setHonour("2");
		assertEquals("Setting card honour doesnt work", "2", card.getHonour());
	}

	@Test
	public final void testCardsValue() {
		cards.add(new Card(JACK, SPADE));
		cards.add(new Card(JACK, HEART));
		cards.add(new Card(JACK, DIAMOND));
		cards.add(new Card(QUEEN, SPADE));
		cards.add(new Card("5", HEART));
		cards.add(new Card(QUEEN, DIAMOND));
		cards.add(new Card("4", SPADE));
		cards.add(new Card("8", SPADE));
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
		cards.get(7).setHonour(ACE);
		assertEquals(14, cards.get(7).getValue());
	}

	@Test
	public final void testGetColorCard() {
		final Card card = new Card(JACK, SPADE);
		assertEquals("black", card.getColor());
		final Card card2 = new Card(JACK, DIAMOND);
		assertEquals("red", card2.getColor());
		final Card card3 = new Card();
		card3.setSuit(CLUB);
		assertEquals("black", card3.getColor());
	}

	@Test
	public final void testCardCordination() {
		final Card card = new Card(JACK, SPADE);
		assertEquals(732, card.getxCordination());
		assertEquals(199, card.getyCordination());
		final Card card2 = new Card();
		card2.setxCordination(150);
		card2.setyCordination(250);
		assertEquals(150, card2.getxCordination());
		assertEquals(250, card2.getyCordination());
		final Card card3 = new Card(QUEEN, HEART);
		assertEquals(805, card3.getxCordination());
		assertEquals(101, card3.getyCordination());
		card3.setHonour(ACE);
		assertEquals(2, card3.getxCordination());
	}

	@Test
	public final void testHighCard() {
		cards.add(new Card("4", HEART));
		cards.add(new Card(JACK, SPADE));
		tableCards.add(new Card("6", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("9", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.HIGH_CARD, HandOperations.findHandRank(0, cards, tableCards).getHand());

		cards = new ArrayList<Card>();
		cards.add(new Card("4", HEART));
		cards.add(new Card("4", SPADE));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("8", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("8", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testFullHouse() {
		cards.add(new Card("4", HEART));
		cards.add(new Card("4", SPADE));
		tableCards.add(new Card("8", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("8", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(0).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());

		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", HEART));
		cards.add(new Card("4", SPADE));
		tableCards.add(new Card("4", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("8", SPADE));
		tableCards.add(new Card("8", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(0).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());
	}

	@Test
	public final void testOnePair() {
		cards.add(new Card("4", HEART));
		cards.add(new Card("4", SPADE));
		tableCards.add(new Card("7", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("10", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testTwoPair() {
		cards.add(new Card("4", HEART));
		cards.add(new Card("4", SPADE));
		tableCards.add(new Card("8", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("10", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.TWO_PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());
		assertEquals(KING, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(4).getHonour());

		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", DIAMOND));
		cards.add(new Card("4", SPADE));
		tableCards.add(new Card("8", DIAMOND));
		tableCards.add(new Card("8", SPADE));
		tableCards.add(new Card("9", DIAMOND));
		tableCards.add(new Card("9", CLUB));
		tableCards.add(new Card("10", SPADE));
		assertEquals(HandRankingEnum.TWO_PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getHonour());
		assertEquals("9", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(2).getHonour());
		assertEquals("10", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(4).getHonour());
	}

	@Test
	public final void testThreeOfAKind() {
		cards.add(new Card("4", CLUB));
		cards.add(new Card("4", HEART));
		tableCards.add(new Card("4", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("10", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.THREE_OF_A_KIND, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testFourOfAKind() {
		cards.add(new Card("4", HEART));
		cards.add(new Card("4", SPADE));
		tableCards.add(new Card("8", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("8", SPADE));
		tableCards.add(new Card("8", CLUB));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.FOUR_OF_A_KIND, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testFlush() {
		cards.add(new Card("4", HEART));
		cards.add(new Card("4", SPADE));
		tableCards.add(new Card("8", SPADE));
		tableCards.add(new Card("8", SPADE));
		tableCards.add(new Card("10", SPADE));
		tableCards.add(new Card("3", SPADE));
		tableCards.add(new Card(KING, SPADE));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(SPADE, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());

		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", DIAMOND));
		cards.add(new Card("5", SPADE));
		tableCards.add(new Card("8", DIAMOND));
		tableCards.add(new Card("8", DIAMOND));
		tableCards.add(new Card("10", DIAMOND));
		tableCards.add(new Card("3", DIAMOND));
		tableCards.add(new Card("9", DIAMOND));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(DIAMOND,
				HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());

		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", CLUB));
		cards.add(new Card("5", CLUB));
		tableCards.add(new Card("8", DIAMOND));
		tableCards.add(new Card("8", CLUB));
		tableCards.add(new Card("10", CLUB));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card("9", CLUB));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(CLUB, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());

		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		cards.add(new Card("4", HEART));
		cards.add(new Card("5", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("10", HEART));
		tableCards.add(new Card("3", HEART));
		tableCards.add(new Card("9", HEART));
		assertEquals(HandRankingEnum.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(HEART, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeDeck().get(1).getSuit());
	}

	@Test
	public final void testRoyalFlush() {
		cards.add(new Card("4", HEART));
		cards.add(new Card(JACK, CLUB));
		tableCards.add(new Card("4", CLUB));
		tableCards.add(new Card(QUEEN, CLUB));
		tableCards.add(new Card("10", CLUB));
		tableCards.add(new Card(ACE, CLUB));
		tableCards.add(new Card(KING, CLUB));
		assertEquals(HandRankingEnum.ROYAL_FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testStraight() {
		cards.add(new Card("4", HEART));
		cards.add(new Card(JACK, CLUB));
		tableCards.add(new Card("4", CLUB));
		tableCards.add(new Card(QUEEN, HEART));
		tableCards.add(new Card("10", CLUB));
		tableCards.add(new Card(ACE, SPADE));
		tableCards.add(new Card(KING, HEART));
		assertEquals(HandRankingEnum.STRAIGHT, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testStraightStartingWithAce() {
		cards.add(new Card(ACE, HEART));
		cards.add(new Card("2", CLUB));
		tableCards.add(new Card("4", CLUB));
		tableCards.add(new Card(QUEEN, HEART));
		tableCards.add(new Card("10", CLUB));
		tableCards.add(new Card("3", SPADE));
		tableCards.add(new Card("5", HEART));
		assertEquals(HandRankingEnum.STRAIGHT, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testStraightFlush() {
		cards.add(new Card(KING, CLUB));
		cards.add(new Card("2", HEART));
		tableCards.add(new Card("4", HEART));
		tableCards.add(new Card("6", HEART));
		tableCards.add(new Card("10", CLUB));
		tableCards.add(new Card("3", HEART));
		tableCards.add(new Card("5", HEART));
		assertEquals(HandRankingEnum.STRAIGHT_FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

}
