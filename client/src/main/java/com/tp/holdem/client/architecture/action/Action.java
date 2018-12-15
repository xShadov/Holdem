package com.tp.holdem.client.architecture.action;

import com.tp.holdem.model.game.Moves;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {
	private Moves move;
	private int betAmount;

	public static Action from(Moves move, int betAmount) {
		return new Action(move, betAmount);
	}

	public static Action simple(Moves move) {
		return new Action(move, 0);
	}
}
