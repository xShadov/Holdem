package com.tp.holdem.common.model;

public enum Phase {
	START(false),
	PRE_FLOP(true),
	FLOP(true),
	TURN(true),
	RIVER(true),
	OVER(false);

	private boolean playing;

	Phase(boolean playing) {
		this.playing = playing;
	}

	public boolean isPlaying() {
		return playing;
	}

	public boolean isStarting() {
		return this == Phase.START;
	}

	public Phase nextPhase() {
		if (this == OVER)
			throw new IllegalArgumentException("There is no next phase");
		return Phase.values()[this.ordinal() + 1];
	}

	public boolean isLastPlayingPhase() {
		return this == RIVER;
	}

	public static Phase lastPlayingPhase() {
		return Phase.RIVER;
	}
}
