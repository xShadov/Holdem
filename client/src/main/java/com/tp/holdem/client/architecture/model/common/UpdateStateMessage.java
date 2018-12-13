package com.tp.holdem.client.architecture.model.common;

import com.tp.holdem.client.architecture.model.action.ActionRequest;
import com.tp.holdem.client.architecture.model.event.ServerResponse;
import com.tp.holdem.client.game.GameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStateMessage implements ServerResponse, ActionRequest {
	private GameState gameState;

	public static UpdateStateMessage from(GameState gameState) {
		return new UpdateStateMessage(gameState);
	}
}
