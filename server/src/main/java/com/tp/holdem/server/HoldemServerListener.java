package com.tp.holdem.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.PlayerConnectMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class HoldemServerListener extends Listener {
	private final MessageSender sender;
	private final GameParams params;
	private final GameHandler gameHandler;

	private ConnectedPlayers connectedPlayers = ConnectedPlayers.empty();

	@Override
	public synchronized void received(final Connection connection, final Object object) {
		//handleReceived(object);
		log.debug("Received message from " + connection.getID() + ": " + object);
	}

	@Override
	public synchronized void connected(final Connection con) {
		log.debug(String.format("Connection attempt: %d", con.getID()));

		final boolean connected = tryConnectingPlayer(con);

		if (!connected)
			return;

		if (enoughPlayers())
			startGame();
	}

	@Override
	public synchronized void disconnected(final Connection con) {
		log.debug(String.format("Dis-connection attempt: %d", con.getID()));

		if (!connectedPlayers.isConnected(con.getID()))
			throw new IllegalStateException("Player is not connected");

		connectedPlayers.getConnected(con.getID())
				.map(gameHandler::disconnectPlayer)
				.forEach(table -> sender.sendStateUpdate(connectedPlayers, table));
	}

	private boolean tryConnectingPlayer(Connection con) {
		if (enoughPlayers()) {
			final PlayerConnectMessage response = PlayerConnectMessage.failure();
			sender.sendSingle(con.getID(), Message.from(MessageType.PLAYER_CONNECTION, response));
			return false;
		}

		if (connectedPlayers.isConnected(con.getID()))
			throw new IllegalStateException("Player already connected");

		final Player player = gameHandler.connectPlayer();
		connectedPlayers = connectedPlayers.connect(con.getID(), player.getNumber());

		final PlayerConnectMessage connectionResponse = PlayerConnectMessage.success(player.toCurrentPlayerDTO());
		sender.sendSingle(con.getID(), Message.from(MessageType.PLAYER_CONNECTION, connectionResponse));
		return true;
	}

	private void startGame() {
		final PokerTable response = gameHandler.startGame();
		sender.sendStateUpdate(connectedPlayers, response);
	}

	private boolean enoughPlayers() {
		return connectedPlayers.size() >= params.getPlayerCount();
	}
}
