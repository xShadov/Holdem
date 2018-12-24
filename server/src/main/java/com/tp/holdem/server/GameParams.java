package com.tp.holdem.server;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GameParams {
	private final int playerCount;
	private final int startingChips;
	private final int bigBlindAmount;
	private final int smallBlindAmount;
	private final int port;
}
