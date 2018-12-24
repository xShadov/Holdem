package com.tp.holdem.model.message;

import com.tp.holdem.model.game.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerConnectMessage implements ServerMessage {
	private boolean success;
	private Player player;

	public static PlayerConnectMessage success(Player player) {
		return new PlayerConnectMessage(true, player);
	}

	public static PlayerConnectMessage failure() {
		return new PlayerConnectMessage(false, null);
	}
}
