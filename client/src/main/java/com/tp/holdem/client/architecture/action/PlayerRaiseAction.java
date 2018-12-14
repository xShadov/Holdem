package com.tp.holdem.client.architecture.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRaiseAction implements ActionRequest {
	private int raiseAmount;

	public static PlayerRaiseAction from(int raiseAmount) {
		return new PlayerRaiseAction(raiseAmount);
	}
}
