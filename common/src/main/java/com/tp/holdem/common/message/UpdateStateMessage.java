package com.tp.holdem.common.message;

import com.tp.holdem.common.message.dto.CurrentPlayerDTO;
import com.tp.holdem.common.message.dto.PokerTableDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateStateMessage implements ServerMessage {
	private PokerTableDTO table;
	private CurrentPlayerDTO currentPlayer;
}
