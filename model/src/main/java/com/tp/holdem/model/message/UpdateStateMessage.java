package com.tp.holdem.model.message;

import com.tp.holdem.model.message.dto.CurrentPlayerDTO;
import com.tp.holdem.model.message.dto.PlayerDTO;
import com.tp.holdem.model.message.dto.PokerTableDTO;
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
	private PokerTableDTO table;
	private CurrentPlayerDTO currentPlayer;
	private PlayerDTO bettingPlayer;
	private PlayerDTO winnerPlayer;
	private List<PlayerDTO> allPlayers;
}
