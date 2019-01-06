package com.tp.holdem.client;

public class DeckTests {

/*
	private transient DeckDTO deck;

	@Before
	public void setUp() {
		deck = new DeckDTO();
	}

	@After
	public void tearDown() {
		deck = null;
	}

	@Test
	public final void testDeckCardsLeft() {
		assertEquals("Wrong number of cards left", 52, deck.howManyCardsLeft());
	}

	@Test
	public final void testDeckGetCardsSize() {
		final List<CardDTO> cards = deck.getCards();
		assertEquals("DeckDTO has wrong cards", 52, cards.size());
	}

	@Test
	public final void testDeckGetCardsHonour() {
		final List<CardDTO> cards = deck.getCards();
		assertEquals("DeckDTO has wrong cards", "2", cards.get(0).getHonour());
	}

	@Test
	public final void testDeckGetCardsSuit() {
		final List<CardDTO> cards = deck.getCards();
		assertEquals("DeckDTO has wrong cards", "Spade", cards.get(0).getSuit());
	}

	@Test
	public final void testDeckShuffling() {
		deck.shuffleCards();
		if (deck.getCards().get(0).getHonour().equals("2") && deck.getCards().get(0).getSuit().equals("Spade")) {
			fail();
		}
	}

	@Test
	public final void testDeckDealingCardsHowManyLeft() {
		final List<PlayerDTO> players = new ArrayList<PlayerDTO>();
		players.add(new PlayerDTO(1));
		players.add(new PlayerDTO(2));
		deck.dealCards(6, players);
		assertEquals("DeckDTO is dealing wrong", 40, deck.howManyCardsLeft());
	}

	@Test
	public final void testDeckDrawCard() {
		deck.drawCard();
		deck.drawCard();
		deck.drawCard();
		assertEquals(49, deck.getCards().size());
	}

	@Test
	public final void testDeckDealingCardsFirstPlayer() {
		final List<PlayerDTO> players = new ArrayList<PlayerDTO>();
		players.add(new PlayerDTO(1));
		players.add(new PlayerDTO(2));
		deck.dealCards(6, players);
		assertEquals("DeckDTO is dealing cards wrong", 6, players.get(0).getHand().size());
	}

	@Test
	public final void testDeckDealingCardsSecondPlayer() {
		final List<PlayerDTO> players = new ArrayList<PlayerDTO>();
		players.add(new PlayerDTO(1));
		players.add(new PlayerDTO(2));
		deck.dealCards(6, players);
		assertEquals("DeckDTO is dealing cards wrong", 6, players.get(1).getHand().size());
	}

	@Test
	public final void testDeckEmptyCheck() {
		final List<PlayerDTO> players = new ArrayList<PlayerDTO>();
		assertFalse("DeckDTO shouldnt be empty", deck.isEmpty());
		players.add(new PlayerDTO(1));
		players.add(new PlayerDTO(2));
		deck.dealCards(26, players);
		assertTrue("DeckDTO should be empty", deck.isEmpty());
	}*/
}
