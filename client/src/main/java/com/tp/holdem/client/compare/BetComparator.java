package com.tp.holdem.client.compare;

import com.tp.holdem.client.model.Player;

import java.util.Comparator;

public class BetComparator implements Comparator<Player> {

	@Override
	public int compare(final Player player1, final Player player2) {
		if (player1.getBetAmount() > player2.getBetAmount())
			return 1;
		else if (player1.getBetAmount() == player2.getBetAmount())
			return 0;
		else
			return -1;
	}

}
