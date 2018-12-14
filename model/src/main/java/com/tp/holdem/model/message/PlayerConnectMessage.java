package com.tp.holdem.model.message;

import com.tp.holdem.model.game.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerConnectMessage implements ServerMessage {
	private Player player;

	public static PlayerConnectMessage from(Player player) {
		return new PlayerConnectMessage(player);
	}
}
