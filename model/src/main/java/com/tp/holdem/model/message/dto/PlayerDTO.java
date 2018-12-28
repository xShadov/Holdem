package com.tp.holdem.model.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
	private int number;

	private int betAmount;
	private int chipsAmount;

	private boolean inGame;
	private boolean allIn;
	private boolean folded;

	private String name;

	public boolean playing() {
		return isInGame() && !isFolded();
	}
}
