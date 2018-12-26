package com.tp.holdem.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.common.Honour;
import com.tp.holdem.model.common.Moves;
import com.tp.holdem.model.common.Suit;
import com.tp.holdem.model.message.*;
import com.tp.holdem.model.message.dto.CardDTO;
import com.tp.holdem.model.message.dto.CurrentPlayerDTO;
import com.tp.holdem.model.message.dto.PlayerDTO;
import com.tp.holdem.model.message.dto.PokerTableDTO;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TempServer implements Runnable {
	private final Server server;
	private final GameHandler gameHandler;
	private final GameParams gameParams;

	private Map<Connection, Integer> connectedPlayers = HashMap.empty();

	public TempServer(Server server, GameHandler gameHandler, GameParams gameParams) {
		this.server = server;
		this.gameHandler = gameHandler;
		this.gameParams = gameParams;

		final AtomicInteger registerCount = new AtomicInteger(16);

		final Kryo kryo = server.getKryo();
		kryo.register(java.util.ArrayList.class, registerCount.getAndIncrement());
		kryo.register(java.util.List.class, registerCount.getAndIncrement());
		kryo.register(Honour.class, registerCount.getAndIncrement());
		kryo.register(Suit.class, registerCount.getAndIncrement());
		kryo.register(PlayerDTO.class, registerCount.getAndIncrement());
		kryo.register(CurrentPlayerDTO.class, registerCount.getAndIncrement());
		kryo.register(CardDTO.class, registerCount.getAndIncrement());
		kryo.register(PokerTableDTO.class, registerCount.getAndIncrement());

		kryo.register(Message.class, registerCount.getAndIncrement());
		kryo.register(MessageType.class, registerCount.getAndIncrement());
		kryo.register(PlayerConnectMessage.class, registerCount.getAndIncrement());
		kryo.register(UpdateStateMessage.class, registerCount.getAndIncrement());
		kryo.register(Moves.class, registerCount.getAndIncrement());
		kryo.register(PlayerActionMessage.class, registerCount.getAndIncrement());
	}

	public void start() {
		try {
			server.addListener(serverListener());

			server.bind(gameParams.getPort());
			server.start();

			final Thread gameThread = new Thread(this);
			gameThread.start();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public synchronized void run() {
		try {
			while (true) {
				Thread.sleep(500);
			}
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

	private Listener serverListener() {
		return new Listener() {
			public synchronized void received(final Connection connection, final Object object) {
				//handleReceived(object);
				log.debug("Received message from " + connection.getID() + ": " + object);
			}

			public synchronized void connected(final Connection con) {
				log.debug(String.format("Connection attempt: %d", con.getID()));

				final boolean connected = tryConnectingPlayer(con);

				if (!connected)
					return;

				if (enoughPlayers())
					startGame();
			}

			public synchronized void disconnected(final Connection con) {
				log.debug(String.format("Dis-connection attempt: %d", con.getID()));
				//handleDisconnected(con);
			}

		};
	}

	private boolean tryConnectingPlayer(Connection con) {
		if (enoughPlayers()) {
			final PlayerConnectMessage response = PlayerConnectMessage.failure();
			server.sendToTCP(con.getID(), Message.from(MessageType.PLAYER_CONNECTION, response));
			return false;
		}

		final Player player = gameHandler.connectPlayer();
		connectedPlayers = connectedPlayers.put(con, player.getNumber());

		final PlayerConnectMessage connectionResponse = PlayerConnectMessage.success(player.toCurrentPlayerDTO());
		server.sendToTCP(con.getID(), Message.from(MessageType.PLAYER_CONNECTION, connectionResponse));
		return true;
	}

	private void startGame() {
		final Tuple2<List<Player>, PokerTable> response = gameHandler.startGame();

		final UpdateStateMessage message = UpdateStateMessage.builder()
				.bettingPlayer(response._1.get(1).toPlayerDTO())
				.allPlayers(response._1.map(Player::toPlayerDTO).toJavaList())
				.table(response._2.toDTO())
				.build();

		connectedPlayers.forEach((connection, playerNumber) -> {
			final CurrentPlayerDTO currentPlayerDTO = response._1
					.find(player -> Objects.equals(player.getNumber(), playerNumber))
					.map(Player::toCurrentPlayerDTO)
					.getOrElseThrow(() -> new IllegalStateException("There is no current player in list of all players"));

			final UpdateStateMessage modifiedResponse = message.toBuilder()
					.currentPlayer(currentPlayerDTO)
					.build();

			server.sendToTCP(connection.getID(), Message.from(MessageType.UPDATE_STATE, modifiedResponse));
		});
	}

	private boolean enoughPlayers() {
		return connectedPlayers.size() >= gameParams.getPlayerCount();
	}
}