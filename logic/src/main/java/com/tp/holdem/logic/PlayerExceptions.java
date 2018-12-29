package com.tp.holdem.logic;

import java.util.function.Supplier;

public class PlayerExceptions {
	public static final Supplier<IllegalStateException> PLAYER_NOT_FOUND = () -> new IllegalStateException("No player with the best hand");
}
