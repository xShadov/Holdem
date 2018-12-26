package com.tp.holdem.server;

import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.UpdateStateMessage;
import com.tp.holdem.model.message.dto.CurrentPlayerDTO;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
class MessageSender {
	private Server server;

	void sendStateUpdate(ConnectedPlayers connectedPlayers, PokerTable table) {
		final UpdateStateMessage message = UpdateStateMessage.builder()
				.table(table.toDTO())
				.build();

		connectedPlayers.forEach((connection, playerNumber) -> {
			final CurrentPlayerDTO currentPlayerDTO = table.getAllPlayers()
					.find(player -> Objects.equals(player.getNumber(), playerNumber))
					.map(Player::toCurrentPlayerDTO)
					.getOrElseThrow(() -> new IllegalStateException("There is no current player in list of all players"));

			final UpdateStateMessage modifiedResponse = message.toBuilder()
					.currentPlayer(currentPlayerDTO)
					.build();

			server.sendToTCP(connection, Message.from(MessageType.UPDATE_STATE, modifiedResponse));
		});
	}

	void sendSingle(int id, Message message) {
		server.sendToTCP(id, message);
	}
}
