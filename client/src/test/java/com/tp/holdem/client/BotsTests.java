package com.tp.holdem.client;

import static org.junit.Assert.assertTrue;

public class BotsTests {
	/*private static final String FOLD = "FOLD";
	private static final String BET = "BET";
	private static final String RAISE = "RAISE";
	private static final String ALLIN = "ALLIN";
	private static final String CHECK = "CHECK";
	private static final String CALL = "CALL";
	private transient final List<CardDTO> hand = new ArrayList<CardDTO>();
	private final List<CardDTO> tableCards = new ArrayList<CardDTO>();
	final private String[] suits = { "Spade", "Heart", "Diamond", "Club" };
	final private String[] honours = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };

	@Before
	public void setUp() {
		hand.clear();
		tableCards.clear();
	}

	@Test
	public void testMedium() {
		final FakeServer server = new FakeServer(20, 10); // maxBet , bigBlind
		final MediumStrategy bot = new MediumStrategy();

		hand.add(new CardDTO(honours[1], suits[1]));
		hand.add(new CardDTO(honours[1], suits[2]));
		server.setCardsOnTable(tableCards);

		bot.whatDoIDo(server, hand, 20, 20); // ilePostawilem, ileMam
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));

		// Flop
		tableCards.add(new CardDTO(honours[1], suits[3]));
		tableCards.add(new CardDTO(honours[3], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		server.setCardsOnTable(tableCards);

		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[2], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(CHECK));
	}

	@Test
	public void testEasy() {
		FakeServer server = new FakeServer(20, 0);
		EasyStrategy bot = new EasyStrategy();
		hand.add(new CardDTO(honours[1], suits[1]));
		hand.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		server.setCardsOnTable(tableCards);
		bot.whatDoIDo(server, hand, 20, 20);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
	}

	@Test
	public void testHard() {
		FakeServer server = new FakeServer(20, 10); // maxBet , bigBlind
		final HardStrategy bot = new HardStrategy();

		hand.add(new CardDTO(honours[1], suits[1]));
		hand.add(new CardDTO(honours[2], suits[2]));
		server.setCardsOnTable(tableCards);

		bot.whatDoIDo(server, hand, 20, 20); // ilePostawilem, ileMam
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));

		// Flop
		tableCards.add(new CardDTO(honours[3], suits[3]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		tableCards.add(new CardDTO(honours[5], suits[1]));
		server.setCardsOnTable(tableCards);

		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[2], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(CHECK));

		// Turn

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[2], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[2], suits[1]));
		tableCards.add(new CardDTO(honours[5], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[3], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[3], suits[1]));
		tableCards.add(new CardDTO(honours[6], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[4], suits[3]));
		tableCards.add(new CardDTO(honours[6], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		tableCards.add(new CardDTO(honours[10], suits[1]));
		server.setCardsOnTable(tableCards);
		server.setMaxBetOnTable(0);
		while (!bot.getTag().equals(ALLIN))
			bot.whatDoIDo(server, hand, 0, 9);
		while (!bot.getTag().equals(BET))
			bot.whatDoIDo(server, hand, 0, 11);
		server.setMaxBetOnTable(10);
		while (!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 200);
		while (!bot.getTag().equals(CHECK))
			bot.whatDoIDo(server, hand, 10, 200);
		while (!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 9);

		// River

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[2], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[2]));
		server.setCardsOnTable(tableCards);

		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[2], suits[1]));
		tableCards.add(new CardDTO(honours[5], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[2]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[3], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[2]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[2], suits[3]));
		tableCards.add(new CardDTO(honours[3], suits[1]));
		tableCards.add(new CardDTO(honours[6], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		tableCards.add(new CardDTO(honours[4], suits[2]));
		server.setCardsOnTable(tableCards);

		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));

		tableCards.clear();
		tableCards.add(new CardDTO(honours[4], suits[3]));
		tableCards.add(new CardDTO(honours[6], suits[1]));
		tableCards.add(new CardDTO(honours[8], suits[1]));
		tableCards.add(new CardDTO(honours[10], suits[1]));
		tableCards.add(new CardDTO(honours[9], suits[2]));
		server.setCardsOnTable(tableCards);
		server.setMaxBetOnTable(0);
		while (!bot.getTag().equals(ALLIN))
			bot.whatDoIDo(server, hand, 0, 9);
		while (!bot.getTag().equals(BET))
			bot.whatDoIDo(server, hand, 0, 11);
		server.setMaxBetOnTable(10);
		while (!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 200);
		while (!bot.getTag().equals(CHECK))
			bot.whatDoIDo(server, hand, 10, 200);
		while (!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 9);
	}

	@Test
	public void testAllInStrategy() {
		FakeServer server = new FakeServer(20, 0);
		AllInStrategy bot = new AllInStrategy();
		hand.add(new CardDTO(honours[1], suits[1]));
		hand.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		server.setCardsOnTable(tableCards);
		bot.whatDoIDo(server, hand, 20, 20);
		assertTrue(bot.getTag().equals(ALLIN));
	}

	@Test
	public void testAlwaysFold() {
		FakeServer server = new FakeServer(20, 0);
		FoldStrategy bot = new FoldStrategy();
		hand.add(new CardDTO(honours[1], suits[1]));
		hand.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		tableCards.add(new CardDTO(honours[1], suits[1]));
		server.setCardsOnTable(tableCards);
		bot.whatDoIDo(server, hand, 20, 20);
		assertTrue(bot.getTag().equals(FOLD));
	}*/
}
