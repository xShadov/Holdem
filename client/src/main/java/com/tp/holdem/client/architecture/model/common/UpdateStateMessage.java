package com.tp.holdem.client.architecture.model.common;

import com.tp.holdem.client.architecture.model.action.ActionRequest;
import com.tp.holdem.client.architecture.model.event.ServerResponse;
import com.tp.holdem.client.model.Player;
import com.tp.holdem.client.model.PokerTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStateMessage implements ServerResponse, ActionRequest {
	private Player currentPlayer;
	private List<Player> players;
	private PokerTable table;
}
