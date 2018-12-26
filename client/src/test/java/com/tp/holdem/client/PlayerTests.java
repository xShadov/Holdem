package com.tp.holdem.client;

import static org.junit.Assert.assertEquals;

public class PlayerTests {
	/*private Player playerNoName, playerWithName;

	@Before
	public void setUp() {
		playerNoName = Player.unnamed(5);
		playerWithName = Player.named(6, "Roger");
	}

	@Test
	public final void testPlayerDefaultNameCheck() {
		assertEquals("Player has the wrong default name", "Player5", playerNoName.getName());
	}

	@Test
	public final void testPlayerAddCard() {
		playerNoName.addCard(Card.from(Suit.HEART, Honour.FIVE));
		assertEquals("Player still has no cards", 1, playerNoName.getHand().size());
	}

	@Test
	public final void testPlayerCardsInPosesion() {
		playerWithName.addCard(Card.from(Suit.HEART, Honour.FIVE));
		playerWithName.addCard(Card.from(Suit.HEART, Honour.SIX));
		assertEquals("Player has wrong number of cards", 2, playerWithName.getHand().size());
	}

	@Test
	public final void testPlayerCompareBets() {
		final Player player1 = new Player(10);
		final Player player2 = new Player(20);
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
		final Player player = new Player(15);
		player.addCard(Card.from(Suit.DIAMOND, Honour.QUEEN));
		player.addCard(Card.from(Suit.SPADE, Honour.JACK));
		player.clearHand();
		assertEquals(0, player.getHand().size());
	}

	@Test
	public final void testPlayerStrategy() {
		final Player player = new Player(15);
		final Bot playerBot = new Bot(5, "bot", new AllInStrategy());
		final List<Player> players = new ArrayList<Player>();
		players.add(player);
		players.add(playerBot);
		for (final Player playerk : players) {
			if (playerk instanceof Bot) {
				assertEquals("Always All-in", playerk.getStrategy().getName());
			} else {
				assertEquals(null, playerk.getStrategy());
			}
		}
	}

	@Test
	public final void testPlayerHandSetting() {
		final List<Card> cards = new ArrayList<Card>();
		final Player player = new Player(15);
		cards.add(Card.from(Suit.SPADE, Honour.JACK));
		cards.add(Card.from(Suit.DIAMOND, Honour.QUEEN));
		player.setHand(cards);
		assertEquals(2, player.getHand().size());
		assertEquals("Jack", player.getHand().get(0).getHonour());
		assertEquals("Queen", player.getHand().get(1).getHonour());
	}

	@Test
	public final void testPlayerPotCheck() {
		final Player player = new Player(15);
		player.setFromWhichPot(10);
		assertEquals(10, player.getFromWhichPot());
	}

	@Test
	public final void testPlayerConnectionId() {
		final Player player = new Player(15);
		player.setConnectionId(52131);
		assertEquals(52131, player.getConnectionId());
	}

	@Test
	public final void testPlayerHandRankNoCards() {
		final Player player = new Player(15);
		final HandRank handRank = new HandRank();
		handRank.setPlayerNumber(player.getNumber());
		handRank.setHand(Hands.ROYAL_FLUSH);
		handRank.setCardsThatMakeHand(new ArrayList<Card>());
		player.setHandRank(handRank);
		assertEquals(Hands.ROYAL_FLUSH, player.getHandRank().getHand());
		assertEquals(15, player.getHandRank().getPlayerNumber());
		assertEquals(0, player.getHandRank().getCardsThatMakeHand().size());
	}

	@Test
	public final void testPlayerHandRankWithCards() {
		final Player player = new Player(15);
		final List<Card> cards = new ArrayList<Card>();
		cards.add(Card.from(Suit.CLUB, Honour.TWO));
		cards.add(Card.from(Suit.HEART,Honour.TWO));
		cards.add(Card.from(Suit.CLUB, Honour.FIVE));
		cards.add(Card.from(Suit.HEART, Honour.FIVE));
		cards.add(Card.from(Suit.CLUB, Honour.FIVE));
		player.setHandRank(new HandRank(player.getNumber(), Hands.ROYAL_FLUSH, cards));
		assertEquals(Hands.ROYAL_FLUSH, player.getHandRank().getHand());
		assertEquals(15, player.getHandRank().getPlayerNumber());
		assertEquals(5, player.getHandRank().getCardsThatMakeHand().size());
		String string = player.getNumber() + " " + player.getHandRank().getHand();
		for (final Card card : player.getHandRank().getCardsThatMakeHand()) {
			string += " " + card.getHonour() + " " + card.getSuit();
		}
		assertEquals(string, player.getHandRank() + "");
	}

	@Test
	public final void testPlayerCompareHands() {
		final Player player = new Player(15);
		player.setHandRank(new HandRank(player.getNumber(), Hands.STRAIGHT, new ArrayList<Card>()));
		final Player player2 = new Player(20);
		player2.setHandRank(new HandRank(player2.getNumber(), Hands.STRAIGHT_FLUSH, new ArrayList<Card>()));
		final HandRankComparator comp = new HandRankComparator();
		assertEquals(-1, comp.compare(player.getHandRank(), player2.getHandRank()));
		assertEquals(1, comp.compare(player2.getHandRank(), player.getHandRank()));
	}

	@Test
	public final void testPlayerCloneMethod() {
		final Player playerToClone = new Player(10, "Cloner");
		playerToClone.setAllIn(true);
		playerToClone.setBetAmount(152);
		playerToClone.setBetAmountThisRound(50);
		playerToClone.setHasBigBlind(true);
		playerToClone.setHasDealerButton(true);
		playerToClone.setHasSmallBlind(true);
		playerToClone.setChipsAmount(1512);
		playerToClone.setFolded(true);
		playerToClone.setInGame(false);
		final Player clonedPlayer = new Player();
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
