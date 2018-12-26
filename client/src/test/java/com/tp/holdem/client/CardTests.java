package com.tp.holdem.client;

import static org.junit.Assert.assertEquals;

public class CardTests {
/*

	private static final String KING = "King";
	private static final String ACE = "Ace";
	private static final String QUEEN = "Queen";
	private static final String JACK = "Jack";
	private static final String DIAMOND = "Diamond";
	private static final String CLUB = "Club";
	private static final String HEART = "Heart";
	private static final String SPADE = "Spade";
	transient List<CardDTO> cards, tableCards;
	transient List<PlayerDTO> players;
	transient CardDTO card;

	@Before
	public void setUp() {
		card = new CardDTO("6", SPADE);
		cards = new ArrayList<CardDTO>();
		tableCards = new ArrayList<CardDTO>();
		players = new ArrayList<PlayerDTO>();
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
		cards.add(new CardDTO(JACK, SPADE));
		cards.add(new CardDTO(JACK, HEART));
		cards.add(new CardDTO(JACK, DIAMOND));
		cards.add(new CardDTO(QUEEN, SPADE));
		cards.add(new CardDTO("5", HEART));
		cards.add(new CardDTO(QUEEN, DIAMOND));
		cards.add(new CardDTO("4", SPADE));
		cards.add(new CardDTO("8", SPADE));
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
		final CardDTO card = new CardDTO(JACK, SPADE);
		assertEquals("black", card.getColor());
		final CardDTO card2 = new CardDTO(JACK, DIAMOND);
		assertEquals("red", card2.getColor());
		final CardDTO card3 = new CardDTO();
		card3.setSuit(CLUB);
		assertEquals("black", card3.getColor());
	}

	@Test
	public final void testCardCordination() {
		final CardDTO card = new CardDTO(JACK, SPADE);
		assertEquals(732, card.getxCordination());
		assertEquals(199, card.getyCordination());
		final CardDTO card2 = new CardDTO();
		card2.setxCordination(150);
		card2.setyCordination(250);
		assertEquals(150, card2.getxCordination());
		assertEquals(250, card2.getyCordination());
		final CardDTO card3 = new CardDTO(QUEEN, HEART);
		assertEquals(805, card3.getxCordination());
		assertEquals(101, card3.getyCordination());
		card3.setHonour(ACE);
		assertEquals(2, card3.getxCordination());
	}

	@Test
	public final void testHighCard() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO(JACK, SPADE));
		tableCards.add(new CardDTO("6", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("9", SPADE));
		tableCards.add(new CardDTO("3", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.HIGH_CARD, HandOperations.findHandRank(0, cards, tableCards).getHand());

		cards = new ArrayList<CardDTO>();
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("4", SPADE));
		tableCards = new ArrayList<CardDTO>();
		tableCards.add(new CardDTO("8", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("8", SPADE));
		tableCards.add(new CardDTO("3", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testFullHouse() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("4", SPADE));
		tableCards.add(new CardDTO("8", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("8", SPADE));
		tableCards.add(new CardDTO("3", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(0).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(2).getHonour());

		cards = new ArrayList<CardDTO>();
		tableCards = new ArrayList<CardDTO>();
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("4", SPADE));
		tableCards.add(new CardDTO("4", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("8", SPADE));
		tableCards.add(new CardDTO("8", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.FULL_HOUSE, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(0).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(2).getHonour());
	}

	@Test
	public final void testOnePair() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("4", SPADE));
		tableCards.add(new CardDTO("7", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("10", SPADE));
		tableCards.add(new CardDTO("3", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testTwoPair() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("4", SPADE));
		tableCards.add(new CardDTO("8", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("10", SPADE));
		tableCards.add(new CardDTO("3", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.TWO_PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("4", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(1).getHonour());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(2).getHonour());
		assertEquals(KING, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(4).getHonour());

		cards = new ArrayList<CardDTO>();
		tableCards = new ArrayList<CardDTO>();
		cards.add(new CardDTO("4", DIAMOND));
		cards.add(new CardDTO("4", SPADE));
		tableCards.add(new CardDTO("8", DIAMOND));
		tableCards.add(new CardDTO("8", SPADE));
		tableCards.add(new CardDTO("9", DIAMOND));
		tableCards.add(new CardDTO("9", CLUB));
		tableCards.add(new CardDTO("10", SPADE));
		assertEquals(Hands.TWO_PAIR, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals("8", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(1).getHonour());
		assertEquals("9", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(2).getHonour());
		assertEquals("10", HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(4).getHonour());
	}

	@Test
	public final void testThreeOfAKind() {
		cards.add(new CardDTO("4", CLUB));
		cards.add(new CardDTO("4", HEART));
		tableCards.add(new CardDTO("4", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("10", SPADE));
		tableCards.add(new CardDTO("3", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.THREE_OF_A_KIND, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testFourOfAKind() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("4", SPADE));
		tableCards.add(new CardDTO("8", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("8", SPADE));
		tableCards.add(new CardDTO("8", CLUB));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.FOUR_OF_A_KIND, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testFlush() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("4", SPADE));
		tableCards.add(new CardDTO("8", SPADE));
		tableCards.add(new CardDTO("8", SPADE));
		tableCards.add(new CardDTO("10", SPADE));
		tableCards.add(new CardDTO("3", SPADE));
		tableCards.add(new CardDTO(KING, SPADE));
		assertEquals(Hands.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(SPADE, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(1).getSuit());

		cards = new ArrayList<CardDTO>();
		tableCards = new ArrayList<CardDTO>();
		cards.add(new CardDTO("4", DIAMOND));
		cards.add(new CardDTO("5", SPADE));
		tableCards.add(new CardDTO("8", DIAMOND));
		tableCards.add(new CardDTO("8", DIAMOND));
		tableCards.add(new CardDTO("10", DIAMOND));
		tableCards.add(new CardDTO("3", DIAMOND));
		tableCards.add(new CardDTO("9", DIAMOND));
		assertEquals(Hands.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(DIAMOND,
				HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(1).getSuit());

		cards = new ArrayList<CardDTO>();
		tableCards = new ArrayList<CardDTO>();
		cards.add(new CardDTO("4", CLUB));
		cards.add(new CardDTO("5", CLUB));
		tableCards.add(new CardDTO("8", DIAMOND));
		tableCards.add(new CardDTO("8", CLUB));
		tableCards.add(new CardDTO("10", CLUB));
		tableCards.add(new CardDTO("3", CLUB));
		tableCards.add(new CardDTO("9", CLUB));
		assertEquals(Hands.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(CLUB, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(1).getSuit());

		cards = new ArrayList<CardDTO>();
		tableCards = new ArrayList<CardDTO>();
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO("5", CLUB));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("8", HEART));
		tableCards.add(new CardDTO("10", HEART));
		tableCards.add(new CardDTO("3", HEART));
		tableCards.add(new CardDTO("9", HEART));
		assertEquals(Hands.FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
		assertEquals(HEART, HandOperations.findHandRank(1, cards, tableCards).getCardsThatMakeHand().get(1).getSuit());
	}

	@Test
	public final void testRoyalFlush() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO(JACK, CLUB));
		tableCards.add(new CardDTO("4", CLUB));
		tableCards.add(new CardDTO(QUEEN, CLUB));
		tableCards.add(new CardDTO("10", CLUB));
		tableCards.add(new CardDTO(ACE, CLUB));
		tableCards.add(new CardDTO(KING, CLUB));
		assertEquals(Hands.ROYAL_FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testStraight() {
		cards.add(new CardDTO("4", HEART));
		cards.add(new CardDTO(JACK, CLUB));
		tableCards.add(new CardDTO("4", CLUB));
		tableCards.add(new CardDTO(QUEEN, HEART));
		tableCards.add(new CardDTO("10", CLUB));
		tableCards.add(new CardDTO(ACE, SPADE));
		tableCards.add(new CardDTO(KING, HEART));
		assertEquals(Hands.STRAIGHT, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testStraightStartingWithAce() {
		cards.add(new CardDTO(ACE, HEART));
		cards.add(new CardDTO("2", CLUB));
		tableCards.add(new CardDTO("4", CLUB));
		tableCards.add(new CardDTO(QUEEN, HEART));
		tableCards.add(new CardDTO("10", CLUB));
		tableCards.add(new CardDTO("3", SPADE));
		tableCards.add(new CardDTO("5", HEART));
		assertEquals(Hands.STRAIGHT, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}

	@Test
	public final void testStraightFlush() {
		cards.add(new CardDTO(KING, CLUB));
		cards.add(new CardDTO("2", HEART));
		tableCards.add(new CardDTO("4", HEART));
		tableCards.add(new CardDTO("6", HEART));
		tableCards.add(new CardDTO("10", CLUB));
		tableCards.add(new CardDTO("3", HEART));
		tableCards.add(new CardDTO("5", HEART));
		assertEquals(Hands.STRAIGHT_FLUSH, HandOperations.findHandRank(1, cards, tableCards).getHand());
	}
*/

}
