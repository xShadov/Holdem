package com.tp.holdem.model.message;

import com.tp.holdem.model.game.Player;
import com.tp.holdem.model.game.PokerTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateStateMessage implements ServerMessage {
	private PokerTable table;
	private Player currentPlayer;
	private Player bettingPlayer;
	private Player winnerPlayer;
	private List<Player> allPlayers;
}
