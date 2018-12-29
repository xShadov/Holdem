package com.tp.holdem.model.common;

public enum PhaseStatus {
	KEEP_GOING(false), READY_FOR_NEXT(false), EVERYBODY_FOLDED(true), EVERYBODY_ALL_IN(true);

	private boolean roundEnding;

	PhaseStatus(boolean roundEnding) {
		this.roundEnding = roundEnding;
	}

	public boolean roundEnding() {
		return roundEnding;
	}
}
