package com.tp.holdem.core;

import com.tp.holdem.core.model.Card;
import com.tp.holdem.core.model.Deck;
import com.tp.holdem.core.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DeckTests {
/*
	private transient Deck deck;

	@Before
	public void setUp() {
		deck = new Deck();
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
		final List<Card> cards = deck.getCards();
		assertEquals("Deck has wrong cards", 52, cards.size());
	}

	@Test
	public final void testDeckGetCardsHonour() {
		final List<Card> cards = deck.getCards();
		assertEquals("Deck has wrong cards", "2", cards.get(0).getHonour());
	}

	@Test
	public final void testDeckGetCardsSuit() {
		final List<Card> cards = deck.getCards();
		assertEquals("Deck has wrong cards", "Spade", cards.get(0).getSuit());
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
		final List<Player> players = new ArrayList<Player>();
		players.add(new Player(1));
		players.add(new Player(2));
		deck.dealCards(6, players);
		assertEquals("Deck is dealing wrong", 40, deck.howManyCardsLeft());
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
		final List<Player> players = new ArrayList<Player>();
		players.add(new Player(1));
		players.add(new Player(2));
		deck.dealCards(6, players);
		assertEquals("Deck is dealing cards wrong", 6, players.get(0).getHand().size());
	}

	@Test
	public final void testDeckDealingCardsSecondPlayer() {
		final List<Player> players = new ArrayList<Player>();
		players.add(new Player(1));
		players.add(new Player(2));
		deck.dealCards(6, players);
		assertEquals("Deck is dealing cards wrong", 6, players.get(1).getHand().size());
	}

	@Test
	public final void testDeckEmptyCheck() {
		final List<Player> players = new ArrayList<Player>();
		assertFalse("Deck shouldnt be empty", deck.isEmpty());
		players.add(new Player(1));
		players.add(new Player(2));
		deck.dealCards(26, players);
		assertTrue("Deck should be empty", deck.isEmpty());
	}*/
}
