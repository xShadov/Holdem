package com.tp.holdem.logic;

import com.tp.holdem.logic.model.Player;

import java.util.Objects;
import java.util.function.Predicate;

public class PlayerFinders {
	public static Predicate<Player> byNumber(Integer playerNumber) {
		return player -> Objects.equals(player.getNumber(), playerNumber);
	}
}
