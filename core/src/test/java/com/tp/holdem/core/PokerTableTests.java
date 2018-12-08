package com.tp.holdem.core;

import com.tp.holdem.core.model.Card;
import com.tp.holdem.core.model.PokerTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PokerTableTests {

	transient PokerTable table;

	@Before
	public void setUp() {
		table = new PokerTable();
	}

	@After
	public void tearDown() {
		table = null;
	}

	@Test
	public final void testTablePot() {
		assertEquals(0, table.getPot());
		table.setPot(5000);
		assertEquals(5000, table.getPot());
		table.setPot(table.getPot() - 500);
		assertEquals(4500, table.getPot());
	}

	@Test
	public final void testCardsOnTable() {
		assertEquals(0, table.getCardsOnTable().size());
		table.addCard(new Card("Jack", "Spade"));
		table.addCard(new Card("Queen", "Heart"));
		assertEquals(2, table.getCardsOnTable().size());
		final List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("2", "Spade"));
		cards.add(new Card("3", "Spade"));
		cards.add(new Card("4", "Spade"));
		table.setCardsOnTable(cards);
		assertEquals(3, table.getCardsOnTable().size());
	}

	@Test
	public final void testTableBlinds() {
		assertEquals(0, table.getBigBlindAmount());
		assertEquals(0, table.getSmallBlindAmount());
		table.setBigBlindAmount(500);
		table.setSmallBlindAmount(230);
		assertEquals(500, table.getBigBlindAmount());
		assertEquals(230, table.getSmallBlindAmount());
	}
}
