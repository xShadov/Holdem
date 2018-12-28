package com.tp.holdem.model.common;

public enum Phase {
	START, PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, OVER;

	public Phase nextPhase() {
		if (this == OVER)
			throw new IllegalArgumentException("There is no next phase");
		return Phase.values()[this.ordinal() + 1];
	}
}
