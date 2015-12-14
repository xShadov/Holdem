package com.tp.holdem;

import java.util.Comparator;

public class BetComparator implements Comparator<Player> {

	@Override
	public int compare(Player player1, Player player2) {
		if(player1.getBetAmount()>player2.getBetAmount()) return 1;
		else if(player1.getBetAmount()==player2.getBetAmount()) return 0;
		else return -1;
	}

}
