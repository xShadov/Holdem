package com.tp.holdem.client.architecture.model.common;

import com.tp.holdem.client.architecture.model.action.ActionRequest;
import com.tp.holdem.client.architecture.model.event.ServerResponse;
import com.tp.holdem.client.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStartMessage implements ServerResponse, ActionRequest {
	private List<Player> players;

	public static GameStartMessage from(List<Player> players) {
		return new GameStartMessage(players);
	}
}
