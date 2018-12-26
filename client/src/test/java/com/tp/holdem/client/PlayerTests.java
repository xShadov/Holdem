package com.tp.holdem.client;

import static org.junit.Assert.assertEquals;

public class PlayerTests {
	/*private PlayerDTO playerNoName, playerWithName;

	@Before
	public void setUp() {
		playerNoName = PlayerDTO.unnamed(5);
		playerWithName = PlayerDTO.named(6, "Roger");
	}

	@Test
	public final void testPlayerDefaultNameCheck() {
		assertEquals("PlayerDTO has the wrong default name", "Player5", playerNoName.getName());
	}

	@Test
	public final void testPlayerAddCard() {
		playerNoName.addCard(CardDTO.from(Suit.HEART, Honour.FIVE));
		assertEquals("PlayerDTO still has no cards", 1, playerNoName.getHand().size());
	}

	@Test
	public final void testPlayerCardsInPosesion() {
		playerWithName.addCard(CardDTO.from(Suit.HEART, Honour.FIVE));
		playerWithName.addCard(CardDTO.from(Suit.HEART, Honour.SIX));
		assertEquals("PlayerDTO has wrong number of cards", 2, playerWithName.getHand().size());
	}

	@Test
	public final void testPlayerCompareBets() {
		final PlayerDTO player1 = new PlayerDTO(10);
		final PlayerDTO player2 = new PlayerDTO(20);
		player1.setBetAmount(20);
		player2.setBetAmount(30);
		final BetComparator comp = new BetComparator();
		assertEquals(-1, comp.compare(player1, player2));
		assertEquals(1, comp.compare(player2, player1));
		player1.setBetAmount(0);
		player2.setBetAmount(0);
		assertEquals(0, comp.compare(player1, player2));
	}

	@Test
	public final void testPlayerClearHand() {
		final PlayerDTO player = new PlayerDTO(15);
		player.addCard(CardDTO.from(Suit.DIAMOND, Honour.QUEEN));
		player.addCard(CardDTO.from(Suit.SPADE, Honour.JACK));
		player.clearHand();
		assertEquals(0, player.getHand().size());
	}

	@Test
	public final void testPlayerStrategy() {
		final PlayerDTO player = new PlayerDTO(15);
		final Bot playerBot = new Bot(5, "bot", new AllInStrategy());
		final List<PlayerDTO> players = new ArrayList<PlayerDTO>();
		players.add(player);
		players.add(playerBot);
		for (final PlayerDTO playerk : players) {
			if (playerk instanceof Bot) {
				assertEquals("Always All-in", playerk.getStrategy().getName());
			} else {
				assertEquals(null, playerk.getStrategy());
			}
		}
	}

	@Test
	public final void testPlayerHandSetting() {
		final List<CardDTO> cards = new ArrayList<CardDTO>();
		final PlayerDTO player = new PlayerDTO(15);
		cards.add(CardDTO.from(Suit.SPADE, Honour.JACK));
		cards.add(CardDTO.from(Suit.DIAMOND, Honour.QUEEN));
		player.setHand(cards);
		assertEquals(2, player.getHand().size());
		assertEquals("Jack", player.getHand().get(0).getHonour());
		assertEquals("Queen", player.getHand().get(1).getHonour());
	}

	@Test
	public final void testPlayerPotCheck() {
		final PlayerDTO player = new PlayerDTO(15);
		player.setFromWhichPot(10);
		assertEquals(10, player.getFromWhichPot());
	}

	@Test
	public final void testPlayerConnectionId() {
		final PlayerDTO player = new PlayerDTO(15);
		player.setConnectionId(52131);
		assertEquals(52131, player.getConnectionId());
	}

	@Test
	public final void testPlayerHandRankNoCards() {
		final PlayerDTO player = new PlayerDTO(15);
		final HandRank handRank = new HandRank();
		handRank.setPlayerNumber(player.getNumber());
		handRank.setHand(Hands.ROYAL_FLUSH);
		handRank.setCardsThatMakeHand(new ArrayList<CardDTO>());
		player.setHandRank(handRank);
		assertEquals(Hands.ROYAL_FLUSH, player.getHandRank().getHand());
		assertEquals(15, player.getHandRank().getPlayerNumber());
		assertEquals(0, player.getHandRank().getCardsThatMakeHand().size());
	}

	@Test
	public final void testPlayerHandRankWithCards() {
		final PlayerDTO player = new PlayerDTO(15);
		final List<CardDTO> cards = new ArrayList<CardDTO>();
		cards.add(CardDTO.from(Suit.CLUB, Honour.TWO));
		cards.add(CardDTO.from(Suit.HEART,Honour.TWO));
		cards.add(CardDTO.from(Suit.CLUB, Honour.FIVE));
		cards.add(CardDTO.from(Suit.HEART, Honour.FIVE));
		cards.add(CardDTO.from(Suit.CLUB, Honour.FIVE));
		player.setHandRank(new HandRank(player.getNumber(), Hands.ROYAL_FLUSH, cards));
		assertEquals(Hands.ROYAL_FLUSH, player.getHandRank().getHand());
		assertEquals(15, player.getHandRank().getPlayerNumber());
		assertEquals(5, player.getHandRank().getCardsThatMakeHand().size());
		String string = player.getNumber() + " " + player.getHandRank().getHand();
		for (final CardDTO card : player.getHandRank().getCardsThatMakeHand()) {
			string += " " + card.getHonour() + " " + card.getSuit();
		}
		assertEquals(string, player.getHandRank() + "");
	}

	@Test
	public final void testPlayerCompareHands() {
		final PlayerDTO player = new PlayerDTO(15);
		player.setHandRank(new HandRank(player.getNumber(), Hands.STRAIGHT, new ArrayList<CardDTO>()));
		final PlayerDTO player2 = new PlayerDTO(20);
		player2.setHandRank(new HandRank(player2.getNumber(), Hands.STRAIGHT_FLUSH, new ArrayList<CardDTO>()));
		final HandRankComparator comp = new HandRankComparator();
		assertEquals(-1, comp.compare(player.getHandRank(), player2.getHandRank()));
		assertEquals(1, comp.compare(player2.getHandRank(), player.getHandRank()));
	}

	@Test
	public final void testPlayerCloneMethod() {
		final PlayerDTO playerToClone = new PlayerDTO(10, "Cloner");
		playerToClone.setAllIn(true);
		playerToClone.setBetAmount(152);
		playerToClone.setBetAmountThisRound(50);
		playerToClone.setHasBigBlind(true);
		playerToClone.setHasDealerButton(true);
		playerToClone.setHasSmallBlind(true);
		playerToClone.setChipsAmount(1512);
		playerToClone.setFolded(true);
		playerToClone.setInGame(false);
		final PlayerDTO clonedPlayer = new PlayerDTO();
		clonedPlayer.copy(playerToClone);
		assertEquals(clonedPlayer.isAllIn(), playerToClone.isAllIn());
		assertEquals(clonedPlayer.getBetAmount(), playerToClone.getBetAmount());
		assertEquals(clonedPlayer.getBetAmountThisRound(), playerToClone.getBetAmountThisRound());
		assertEquals(clonedPlayer.isHasBigBlind(), playerToClone.isHasBigBlind());
		assertEquals(clonedPlayer.isHasDealerButton(), playerToClone.isHasDealerButton());
		assertEquals(clonedPlayer.isHasSmallBlind(), playerToClone.isHasSmallBlind());
		assertEquals(clonedPlayer.getChipsAmount(), playerToClone.getChipsAmount());
		assertEquals(clonedPlayer.isFolded(), playerToClone.isFolded());
		assertEquals(clonedPlayer.isInGame(), playerToClone.isInGame());
	}*/

}
