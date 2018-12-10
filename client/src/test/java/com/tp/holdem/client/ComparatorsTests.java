package com.tp.holdem.client;

import static org.junit.Assert.assertEquals;

public class ComparatorsTests {
/*
	private static final String KING = "King";
	private static final String QUEEN = "Queen";
	private static final String JACK = "Jack";
	private static final String ACE = "Ace";
	private static final String HEART = "Heart";
	private static final String SPADE = "Spade";
	private static final String CLUB = "Club";
	transient List<Card> cards, tableCards;
	transient List<HandRank> hands;
	transient HandRankComparator comp;
	transient List<Player> players;
	transient HandRank hand1, hand2;
	transient List<Hands> allRanks;

	@Before
	public void setUp() {
		cards = new ArrayList<Card>();
		tableCards = new ArrayList<Card>();
		hands = new ArrayList<HandRank>();
		comp = new HandRankComparator();
		players = new ArrayList<Player>();
		hand1 = new HandRank();
		hand2 = new HandRank();
		allRanks = new ArrayList<Hands>();
		allRanks.add(Hands.HIGH_CARD);
		allRanks.add(Hands.PAIR);
		allRanks.add(Hands.TWO_PAIR);
		allRanks.add(Hands.THREE_OF_A_KIND);
		allRanks.add(Hands.STRAIGHT);
		allRanks.add(Hands.FLUSH);
		allRanks.add(Hands.FULL_HOUSE);
		allRanks.add(Hands.FOUR_OF_A_KIND);
		allRanks.add(Hands.STRAIGHT_FLUSH);
		allRanks.add(Hands.ROYAL_FLUSH);
	}

	@After
	public void tearDown() {
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
	public final void testCompareTwoHands() {
		cards.add(new Card("4", HEART));
		cards.add(new Card("5", SPADE));
		tableCards.add(new Card("6", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("9", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card("7", SPADE));
		hands.add(HandOperations.findHandRank(0, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card(QUEEN, HEART));
		cards.add(new Card(KING, HEART));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card(ACE, HEART));
		tableCards.add(new Card("10", HEART));
		tableCards.add(new Card(JACK, HEART));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card("7", SPADE));
		hands.add(HandOperations.findHandRank(1, cards, tableCards));
		assertEquals(-1, comp.compare(hands.get(0), hands.get(1)));
		assertEquals(1, comp.compare(hands.get(1), hands.get(0)));
		assertEquals(0, comp.compare(hands.get(0), hands.get(0)));
	}

	@Test
	public final void testSortingByHandPower() {
		cards.add(new Card("4", HEART));
		cards.add(new Card(JACK, SPADE));
		tableCards.add(new Card("6", CLUB));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("9", SPADE));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		hands.add(HandOperations.findHandRank(0, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card("10", HEART));
		cards.add(new Card(JACK, HEART));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", HEART));
		tableCards.add(new Card("4", HEART));
		tableCards.add(new Card(ACE, HEART));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card(KING, SPADE));
		hands.add(HandOperations.findHandRank(1, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card("10", HEART));
		cards.add(new Card(JACK, HEART));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", HEART));
		tableCards.add(new Card("4", HEART));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card("9", SPADE));
		hands.add(HandOperations.findHandRank(2, cards, tableCards));
		cards = new ArrayList<Card>();
		cards.add(new Card("10", HEART));
		cards.add(new Card(JACK, HEART));
		tableCards = new ArrayList<Card>();
		tableCards.add(new Card("2", HEART));
		tableCards.add(new Card("4", HEART));
		tableCards.add(new Card("8", HEART));
		tableCards.add(new Card("3", CLUB));
		tableCards.add(new Card("9", SPADE));
		hands.add(HandOperations.findHandRank(3, cards, tableCards));
		Collections.sort(hands, new HandRankComparator());
		assertEquals(0, hands.get(0).getPlayerNumber());
		assertEquals(2, hands.get(1).getPlayerNumber());
		assertEquals(3, hands.get(2).getPlayerNumber());
		assertEquals(1, hands.get(3).getPlayerNumber());
	}

	@Test
	public final void testSortingByBet() {
		final Player player0 = new Player(0);
		player0.setBetAmount(500);
		final Player player1 = new Player(1);
		player1.setBetAmount(600);
		final Player player2 = new Player(2);
		player2.setBetAmount(1000);
		final Player player3 = new Player(3);
		player3.setBetAmount(600);
		players.add(player0);
		players.add(player1);
		players.add(player2);
		players.add(player3);
		hands.add(new HandRank(0, Hands.ROYAL_FLUSH, new ArrayList<Card>()));
		hands.add(new HandRank(1, Hands.FLUSH, new ArrayList<Card>()));
		hands.add(new HandRank(2, Hands.STRAIGHT, new ArrayList<Card>()));
		hands.add(new HandRank(3, Hands.FOUR_OF_A_KIND, new ArrayList<Card>()));
		Collections.sort(hands, new HandRankComparator());
		final List<Player> winners = new ArrayList<Player>();
		for (int i = 0; i < hands.size(); i++) {
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
		for (int i = 0; i < allRanks.size(); i++) {
			hand1.setHand(allRanks.get(i));
			for (int j = 0; j < allRanks.size(); j++) {
				hand2.setHand(allRanks.get(j));
				if (i == j) {
					continue;
				} else if (i < j) {
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
		hand1.setHand(Hands.HIGH_CARD);
		hand2.setHand(Hands.HIGH_CARD);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("5", HEART));
		cards1.add(new Card("8", CLUB));
		cards1.add(new Card("9", SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", SPADE));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("8", CLUB));
		cards2.add(new Card("9", SPADE));
		cards2.add(new Card(JACK, SPADE));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(ACE);
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
		hand1.setHand(Hands.PAIR);
		hand2.setHand(Hands.PAIR);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("5", HEART));
		cards1.add(new Card("8", CLUB));
		cards1.add(new Card(JACK, SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", SPADE));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("8", CLUB));
		cards2.add(new Card(JACK, SPADE));
		cards2.add(new Card(JACK, SPADE));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(3).setHonour(ACE);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand1.getCardsThatMakeDeck().get(3).setHonour(ACE);
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
		hand1.setHand(Hands.PAIR);
		hand2.setHand(Hands.PAIR);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("5", HEART));
		cards1.add(new Card("8", CLUB));
		cards1.add(new Card("8", SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", SPADE));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("8", CLUB));
		cards2.add(new Card("8", SPADE));
		cards2.add(new Card(JACK, SPADE));
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
		hand2.getCardsThatMakeDeck().get(4).setHonour(QUEEN);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(QUEEN);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareOnePairsByCardsMakingDeckThird() {
		hand1.setHand(Hands.PAIR);
		hand2.setHand(Hands.PAIR);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("5", HEART));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("8", SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", SPADE));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("8", SPADE));
		cards2.add(new Card(JACK, SPADE));
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
		hand2.getCardsThatMakeDeck().get(4).setHonour(QUEEN);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(QUEEN);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareOnePairsByCardsMakingDeckFourth() {
		hand1.setHand(Hands.PAIR);
		hand2.setHand(Hands.PAIR);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("2", HEART));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("8", SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", SPADE));
		cards2.add(new Card("2", HEART));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("8", SPADE));
		cards2.add(new Card(JACK, SPADE));
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
		hand2.getCardsThatMakeDeck().get(4).setHonour(QUEEN);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(QUEEN);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareTwoPairsByCardsMakingDeck() {
		hand1.setHand(Hands.TWO_PAIR);
		hand2.setHand(Hands.TWO_PAIR);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("8", HEART));
		cards1.add(new Card("8", CLUB));
		cards1.add(new Card(JACK, SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", SPADE));
		cards2.add(new Card("8", HEART));
		cards2.add(new Card("8", CLUB));
		cards2.add(new Card(JACK, SPADE));
		cards2.add(new Card(JACK, SPADE));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(3).setHonour(ACE);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand1.getCardsThatMakeDeck().get(3).setHonour(ACE);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(2).setHonour(QUEEN);
		hand2.getCardsThatMakeDeck().get(1).setHonour(QUEEN);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(KING);
		hand2.getCardsThatMakeDeck().get(3).setHonour(KING);
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(3).setHonour(ACE);
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
		hand1.setHand(Hands.TWO_PAIR);
		hand2.setHand(Hands.TWO_PAIR);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", SPADE));
		cards1.add(new Card("3", HEART));
		cards1.add(new Card("8", CLUB));
		cards1.add(new Card(JACK, SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", SPADE));
		cards2.add(new Card("3", HEART));
		cards2.add(new Card("8", CLUB));
		cards2.add(new Card(JACK, SPADE));
		cards2.add(new Card(JACK, SPADE));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(3).setHonour(ACE);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand1.getCardsThatMakeDeck().get(3).setHonour(ACE);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("5");
		hand2.getCardsThatMakeDeck().get(1).setHonour("5");
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(KING);
		hand2.getCardsThatMakeDeck().get(3).setHonour(KING);
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(3).setHonour(ACE);
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
		hand1.setHand(Hands.TWO_PAIR);
		hand2.setHand(Hands.TWO_PAIR);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", SPADE));
		cards1.add(new Card("3", HEART));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("5", SPADE));
		cards1.add(new Card(JACK, SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", SPADE));
		cards2.add(new Card("3", HEART));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("5", SPADE));
		cards2.add(new Card(JACK, SPADE));
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
		hand2.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareThreeOfAKindByCardsMakingDeck() {
		hand1.setHand(Hands.THREE_OF_A_KIND);
		hand2.setHand(Hands.THREE_OF_A_KIND);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("8", HEART));
		cards1.add(new Card(JACK, CLUB));
		cards1.add(new Card(JACK, SPADE));
		cards1.add(new Card(JACK, HEART));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", SPADE));
		cards2.add(new Card("8", HEART));
		cards2.add(new Card(JACK, CLUB));
		cards2.add(new Card(JACK, SPADE));
		cards2.add(new Card(JACK, HEART));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(3).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(2).setHonour(ACE);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(ACE);
		hand1.getCardsThatMakeDeck().get(3).setHonour(ACE);
		hand1.getCardsThatMakeDeck().get(2).setHonour(ACE);
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
		hand1.setHand(Hands.THREE_OF_A_KIND);
		hand2.setHand(Hands.THREE_OF_A_KIND);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("4", SPADE));
		cards1.add(new Card("4", HEART));
		cards1.add(new Card("4", CLUB));
		cards1.add(new Card("10", SPADE));
		cards1.add(new Card(QUEEN, HEART));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("4", SPADE));
		cards2.add(new Card("4", HEART));
		cards2.add(new Card("4", CLUB));
		cards2.add(new Card("10", SPADE));
		cards2.add(new Card(QUEEN, HEART));
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
		hand2.getCardsThatMakeDeck().get(3).setHonour(JACK);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour(JACK);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareThreeOfAKindByCardsMakingDeckThird() {
		hand1.setHand(Hands.THREE_OF_A_KIND);
		hand2.setHand(Hands.THREE_OF_A_KIND);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("4", SPADE));
		cards1.add(new Card("7", HEART));
		cards1.add(new Card("7", CLUB));
		cards1.add(new Card("7", SPADE));
		cards1.add(new Card(QUEEN, HEART));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("4", SPADE));
		cards2.add(new Card("7", HEART));
		cards2.add(new Card("7", CLUB));
		cards2.add(new Card("7", SPADE));
		cards2.add(new Card(QUEEN, HEART));
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
		hand2.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareStraightByCardsMakingDeck() {
		hand1.setHand(Hands.STRAIGHT);
		hand2.setHand(Hands.STRAIGHT);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", SPADE));
		cards1.add(new Card("4", HEART));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("6", SPADE));
		cards1.add(new Card("7", HEART));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", SPADE));
		cards2.add(new Card("4", HEART));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("6", SPADE));
		cards2.add(new Card("7", HEART));
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
		hand2.getCardsThatMakeDeck().get(0).setHonour(ACE);
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
		hand1.setHand(Hands.FLUSH);
		hand2.setHand(Hands.FLUSH);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("5", SPADE));
		cards1.add(new Card("7", SPADE));
		cards1.add(new Card("9", SPADE));
		cards1.add(new Card("10", SPADE));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", HEART));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("7", HEART));
		cards2.add(new Card("9", HEART));
		cards2.add(new Card("10", HEART));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(JACK);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(JACK);
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
		hand1.setHand(Hands.FULL_HOUSE);
		hand2.setHand(Hands.FULL_HOUSE);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", CLUB));
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("5", SPADE));
		cards1.add(new Card("5", CLUB));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", CLUB));
		cards2.add(new Card("2", HEART));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("5", CLUB));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(3).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(2).setHonour(JACK);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(3).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(2).setHonour(JACK);
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
		hand1.setHand(Hands.FULL_HOUSE);
		hand2.setHand(Hands.FULL_HOUSE);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", CLUB));
		cards1.add(new Card("2", SPADE));
		cards1.add(new Card("2", CLUB));
		cards1.add(new Card(QUEEN, SPADE));
		cards1.add(new Card(QUEEN, CLUB));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", CLUB));
		cards2.add(new Card("2", HEART));
		cards2.add(new Card("2", CLUB));
		cards2.add(new Card(QUEEN, HEART));
		cards2.add(new Card(QUEEN, CLUB));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(1).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(2).setHonour(JACK);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(1).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(2).setHonour(JACK);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(0).setHonour("10");
		hand2.getCardsThatMakeDeck().get(1).setHonour("10");
		hand2.getCardsThatMakeDeck().get(2).setHonour("10");
		hand2.getCardsThatMakeDeck().get(3).setHonour(KING);
		hand2.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(1, comp.compare(hand1, hand2));
		assertEquals(-1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(0).setHonour("10");
		hand1.getCardsThatMakeDeck().get(1).setHonour("10");
		hand1.getCardsThatMakeDeck().get(2).setHonour("10");
		hand1.getCardsThatMakeDeck().get(3).setHonour(KING);
		hand1.getCardsThatMakeDeck().get(4).setHonour(KING);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(1).setHonour(ACE);
		hand2.getCardsThatMakeDeck().get(0).setHonour(ACE);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(1).setHonour(ACE);
		hand1.getCardsThatMakeDeck().get(0).setHonour(ACE);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareFourOfAKindByCardsMakingDeck() {
		hand1.setHand(Hands.FOUR_OF_A_KIND);
		hand2.setHand(Hands.FOUR_OF_A_KIND);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("2", CLUB));
		cards1.add(new Card("5", SPADE));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("5", SPADE));
		cards1.add(new Card("5", CLUB));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("2", CLUB));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("5", CLUB));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(3).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(2).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(1).setHonour(JACK);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(3).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(2).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(1).setHonour(JACK);
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
		hand1.setHand(Hands.FOUR_OF_A_KIND);
		hand2.setHand(Hands.FOUR_OF_A_KIND);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("5", SPADE));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("5", SPADE));
		cards1.add(new Card(KING, CLUB));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("5", HEART));
		cards2.add(new Card(KING, CLUB));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(3).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(2).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(1).setHonour(JACK);
		hand2.getCardsThatMakeDeck().get(0).setHonour(JACK);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(3).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(2).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(1).setHonour(JACK);
		hand1.getCardsThatMakeDeck().get(0).setHonour(JACK);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
		hand2.getCardsThatMakeDeck().get(4).setHonour(ACE);
		assertEquals(-1, comp.compare(hand1, hand2));
		assertEquals(1, comp.compare(hand2, hand1));
		hand1.getCardsThatMakeDeck().get(4).setHonour(ACE);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}

	@Test
	public final void testCompareStraightFlushByCardsMakingDeck() {
		hand1.setHand(Hands.STRAIGHT_FLUSH);
		hand2.setHand(Hands.STRAIGHT_FLUSH);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("3", CLUB));
		cards1.add(new Card("4", CLUB));
		cards1.add(new Card("5", CLUB));
		cards1.add(new Card("6", CLUB));
		cards1.add(new Card("7", CLUB));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("3", CLUB));
		cards2.add(new Card("4", CLUB));
		cards2.add(new Card("5", CLUB));
		cards2.add(new Card("6", CLUB));
		cards2.add(new Card("7", CLUB));
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
		hand2.getCardsThatMakeDeck().get(0).setHonour(ACE);
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
		hand1.setHand(Hands.ROYAL_FLUSH);
		hand2.setHand(Hands.ROYAL_FLUSH);
		final List<Card> cards1 = new ArrayList<Card>();
		cards1.add(new Card("10", CLUB));
		cards1.add(new Card(JACK, CLUB));
		cards1.add(new Card(QUEEN, CLUB));
		cards1.add(new Card(KING, CLUB));
		cards1.add(new Card(ACE, CLUB));
		final List<Card> cards2 = new ArrayList<Card>();
		cards2.add(new Card("10", CLUB));
		cards2.add(new Card(JACK, CLUB));
		cards2.add(new Card(QUEEN, CLUB));
		cards2.add(new Card(KING, CLUB));
		cards2.add(new Card(ACE, CLUB));
		hand1.setCardsThatMakeDeck(cards1);
		hand2.setCardsThatMakeDeck(cards2);
		assertEquals(0, comp.compare(hand1, hand2));
		assertEquals(0, comp.compare(hand2, hand1));
	}*/
}
