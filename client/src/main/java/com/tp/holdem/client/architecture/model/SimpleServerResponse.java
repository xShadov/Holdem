package com.tp.holdem.client.architecture.model;

import com.tp.holdem.client.model.Card;
import com.tp.holdem.client.model.Player;
import com.tp.holdem.client.model.PokerTable;
import lombok.Data;

import java.util.List;

@Data
public class SimpleServerResponse {

	private String text = "";
	private String tag = "";
	private List<Player> players;
	private List<Card> cards;
	private List<List<Card>> cards2;
	private PokerTable pokerTable;
	private int number = 0;
	private boolean revealed = false;
	private int maxBetOnTable = 0;
	private List<String> possibleOptions;

	public SimpleServerResponse() {

	}

	public SimpleServerResponse(final String TAG) {
		this.tag = TAG;
	}

	public SimpleServerResponse(final String TAG, final String text) {
		this.tag = TAG;
		this.text = text;
	}

	public SimpleServerResponse(final String TAG, final PokerTable pokerTable) {
		this.tag = TAG;
		this.pokerTable = pokerTable;
	}

	public SimpleServerResponse(final String TAG, final List<Player> players) {
		this.players = players;
		this.tag = TAG;
	}

	public SimpleServerResponse(final String TAG, final List<Card> cards, final boolean dummy) {
		this.cards = cards;
		this.tag = TAG;
	}

	public SimpleServerResponse(final String TAG, final List<List<Card>> cards2, final boolean dummy, final boolean dummy2) {
		this.cards2 = cards2;
		this.tag = TAG;
		this.revealed = dummy;
	}

	public SimpleServerResponse(final String TAG, final int number) {
		this.tag = TAG;
		this.number = number;
	}

	public SimpleServerResponse(final String TAG, final int number, final int maxBetOnTable,
								final List<String> possibleOptions) {
		this.tag = TAG;
		this.number = number;
		this.maxBetOnTable = maxBetOnTable;
		this.possibleOptions = possibleOptions;
	}
}
