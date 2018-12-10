package com.tp.holdem.client.architecture.model.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerBetAction implements ActionRequest {
	private int betAmount;

	public static PlayerBetAction from(int betAmount) {
		return new PlayerBetAction(betAmount);
	}
}
