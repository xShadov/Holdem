package com.tp.holdem.server;

import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.UpdateStateMessage;
import com.tp.holdem.model.message.dto.CurrentPlayerDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@AllArgsConstructor
class MessageSender {
	private Server server;

	void sendStateUpdate(ConnectedPlayers connectedPlayers, PokerTable table) {
		log.debug(String.format("Sending state update to everyone"));

		final UpdateStateMessage message = UpdateStateMessage.builder()
				.table(table.toDTO())
				.build();

		log.debug(String.format("Players on poker table: %s", table.getAllPlayers().map(Player::getNumber)));
		connectedPlayers.forEach((connection, playerNumber) -> {
			log.debug(String.format("Sending message to player: %d", playerNumber));

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
		log.debug(String.format("Sending message %s to single connection: %d", message.getMessageType(), id));

		server.sendToTCP(id, message);
	}
}
