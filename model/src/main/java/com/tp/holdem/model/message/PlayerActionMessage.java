package com.tp.holdem.model.message;

import com.tp.holdem.model.common.Moves;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerActionMessage implements ServerMessage {
	private Moves move;
	private int betAmount;

	public static PlayerActionMessage from(Moves move, int betAmount) {
		return new PlayerActionMessage(move, betAmount);
	}

	public static PlayerActionMessage simple(Moves move) {
		return new PlayerActionMessage(move, 0);
	}
}
