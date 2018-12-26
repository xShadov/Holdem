package com.tp.holdem.client;

import static org.junit.Assert.assertEquals;

public class MessagesTests {
/*
	private static final String TAG = "TAG";
	private static final String TAG2 = "TAG2";
	private transient ClientMoveRequest request;
	private transient SimpleServerResponse response;

	@Before
	public void setUp() {
		request = new ClientMoveRequest();
		response = new SimpleServerResponse();
	}

	@After
	public void tearDown() {
		request = null;
		response = null;
	}

	@Test
	public final void testResponseOnlyTAG() {
		response = new SimpleServerResponse(TAG);
		assertEquals(TAG, response.getTag());
		response.setTag(TAG2);
		assertEquals(TAG2, response.getTag());
	}

	@Test
	public final void testResponseTAGWithMessage() {
		response = new SimpleServerResponse(TAG, "message");
		assertEquals(TAG, response.getTag());
		assertEquals("message", response.getText());
		response.setTag(TAG2);
		response.setText("message2");
		assertEquals(TAG2, response.getTag());
		assertEquals("message2", response.getText());
	}

	@Test
	public final void testResponseTAGWithTable() {
		final PokerTableDTO table = new PokerTableDTO();
		table.setPotAmount(5000);
		response = new SimpleServerResponse(TAG, table);
		assertEquals(TAG, response.getTag());
		assertEquals(5000, response.getTable().getPotAmount());
		response.setTag(TAG2);
		table.setPotAmount(6000);
		response.setTable(table);
		assertEquals(TAG2, response.getTag());
		assertEquals(6000, response.getTable().getPotAmount());
	}

	@Test
	public final void testResponseTAGWithPlayerList() {
		final List<PlayerDTO> players = new ArrayList<PlayerDTO>();
		players.add(new PlayerDTO(0));
		players.add(new PlayerDTO(1));
		response = new SimpleServerResponse(TAG, players);
		assertEquals(TAG, response.getTag());
		assertEquals("Player0", response.getPlayers().get(0).getName());
		assertEquals("Player1", response.getPlayers().get(1).getName());
		response.setTag(TAG2);
		players.add(new PlayerDTO(2));
		response.setPlayers(players);
		assertEquals(TAG2, response.getTag());
		assertEquals("Player2", response.getPlayers().get(2).getName());
	}

	@Test
	public final void testResponseTAGWithCardList() {
		final List<CardDTO> cards = new ArrayList<CardDTO>();
		cards.add(new CardDTO("Jack", "Spade"));
		cards.add(new CardDTO("Queen", "Diamond"));
		response = new SimpleServerResponse(TAG, cards, false);
		assertEquals(TAG, response.getTag());
		assertEquals("Jack", response.getCards().get(0).getHonour());
		assertEquals("Queen", response.getCards().get(1).getHonour());
	}

	@Test
	public final void testResponseTAGWithNumber() {
		response = new SimpleServerResponse(TAG, 155);
		assertEquals(TAG, response.getTag());
		assertEquals(155, response.getNumber());
		response.setTag(TAG2);
		response.setNumber(222);
		assertEquals(TAG2, response.getTag());
		assertEquals(222, response.getNumber());
	}

	@Test
	public final void testResponseTAGWithNumberAndMaxBetOnTable() {
		final List<String> options = new ArrayList<String>();
		options.add("CALL");
		response = new SimpleServerResponse(TAG, 155, 500, options);
		assertEquals(TAG, response.getTag());
		assertEquals(155, response.getNumber());
		assertEquals(500, response.getMaxBetOnTable());
		assertEquals("CALL", response.getPossibleOptions().get(0));
		response.setTag(TAG2);
		response.setNumber(333);
		response.setMaxBetOnTable(2221);
		options.remove(0);
		options.add("CHECK");
		response.setPossibleOptions(options);
		assertEquals(TAG2, response.getTag());
		assertEquals(333, response.getNumber());
		assertEquals(2221, response.getMaxBetOnTable());
		assertEquals("CHECK", response.getPossibleOptions().get(0));
	}

	@Test
	public final void testRequestTAGWithMessage() {
		request = new ClientMoveRequest(TAG, "message");
		assertEquals(TAG, request.getTag());
		assertEquals("message", request.getText());
		request.setTag(TAG2);
		request.setText("message2");
		assertEquals(TAG2, request.getTag());
		assertEquals("message2", request.getText());
	}

	@Test
	public final void testRequestTAGWithNumber() {
		request = new ClientMoveRequest(TAG, 155);
		assertEquals(TAG, request.getTag());
		assertEquals(155, request.getNumber());
		request.setTag(TAG2);
		request.setNumber(222);
		assertEquals(TAG2, request.getTag());
		assertEquals(222, request.getNumber());
	}

	@Test
	public final void testResponseTAGWithNumberAndBetAmount() {
		request = new ClientMoveRequest(TAG, 155, 500);
		assertEquals(TAG, request.getTag());
		assertEquals(500, request.getNumber());
		assertEquals(155, request.getBetAmount());
		request.setTag(TAG2);
		request.setNumber(333);
		request.setBetAmount(999);
		assertEquals(TAG2, request.getTag());
		assertEquals(333, request.getNumber());
		assertEquals(999, request.getBetAmount());
	}*/
}
