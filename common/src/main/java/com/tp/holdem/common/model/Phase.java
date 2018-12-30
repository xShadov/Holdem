package com.tp.holdem.common.model;

public enum Phase {
	START, PRE_FLOP, FLOP, TURN, RIVER, OVER;

	public Phase nextPhase() {
		if (this == OVER)
			throw new IllegalArgumentException("There is no next phase");
		return Phase.values()[this.ordinal() + 1];
	}
}
