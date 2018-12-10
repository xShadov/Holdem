package com.tp.holdem.client.architecture.model.common;

import com.tp.holdem.client.architecture.model.action.ActionRequest;
import com.tp.holdem.client.architecture.model.event.ServerResponse;
import com.tp.holdem.client.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerConnectMessage implements ServerResponse, ActionRequest {
	private Player player;

	public static PlayerConnectMessage from(Player player) {
		return new PlayerConnectMessage(player);
	}
}
