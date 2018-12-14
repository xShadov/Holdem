package com.tp.holdem.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.Lists;
import com.tp.holdem.model.game.*;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.PlayerConnectMessage;
import com.tp.holdem.model.message.UpdateStateMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TempServer implements Runnable {
	private final Server server;
	private final int playersCount;

	private final List<Player> players = Lists.newArrayList();
	private PokerTable table;

	public TempServer(final int playersCount) throws Exception {
		log.debug(String.format("Staring kryoServer, playersCount=%d", playersCount));

		this.playersCount = playersCount;

		server = new Server();

		final Thread gameThread = new Thread(this);

		final AtomicInteger registerCount = new AtomicInteger(16);

		final Kryo kryo = server.getKryo();
		kryo.register(ArrayList.class, registerCount.getAndIncrement());
		kryo.register(List.class, registerCount.getAndIncrement());
		kryo.register(Honour.class, registerCount.getAndIncrement());
		kryo.register(Suit.class, registerCount.getAndIncrement());
		kryo.register(Player.class, registerCount.getAndIncrement());
		kryo.register(Card.class, registerCount.getAndIncrement());
		kryo.register(Deck.class, registerCount.getAndIncrement());
		kryo.register(PokerTable.class, registerCount.getAndIncrement());

		kryo.register(Message.class, registerCount.getAndIncrement());
		kryo.register(MessageType.class, registerCount.getAndIncrement());
		kryo.register(PlayerConnectMessage.class, registerCount.getAndIncrement());
		kryo.register(UpdateStateMessage.class, registerCount.getAndIncrement());
		kryo.register(Moves.class, registerCount.getAndIncrement());

		server.addListener(new Listener() {
			public synchronized void received(final Connection connection, final Object object) {
				//handleReceived(object);
			}

			public synchronized void connected(final Connection con) {
				log.debug(String.format("Connection attempt: %d", con.getID()));

				if (players.size() < playersCount) {
					handleConnected(con);
				} else {
					con.close();
				}
			}

			public synchronized void disconnected(final Connection con) {
				//handleDisconnected(con);
			}

		});

		server.bind(54555);
		server.start();
		gameThread.start();

		while (true) {
			Thread.sleep(100);
		}
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleConnected(final Connection con) {
		int assignedNumber = players.size();

		Player newPlayer = Player.builder()
				.name("Player" + assignedNumber)
				.number(assignedNumber)
				.chipsAmount(1500)
				.betAmount(new Random().nextInt(3000))
				.minimumBet((int) (Math.random() * 100))
				.maximumBet((int) (Math.random() * 500))
				.connectionId(con.getID())
				.possibleMoves(Lists.newArrayList(Moves.BET, Moves.ALLIN, Moves.FOLD))
				.inGame(true)
				.build();

		players.add(newPlayer);

		log.debug(String.format("Player connected, assigned ID: %d", assignedNumber));

		PlayerConnectMessage response = PlayerConnectMessage.from(newPlayer);
		server.sendToTCP(con.getID(), Message.from(MessageType.PLAYER_CONNECTION, response));

		if (players.size() == playersCount) {
			startGame();
		}
	}

	private void startGame() {
		log.debug(String.format("Starting game with %d players", players.size()));

		Deck deck = new Deck();
		table = PokerTable.builder()
				.cardsOnTable(Lists.newArrayList(deck.drawCard(), deck.drawCard(), deck.drawCard()))
				.bigBlindAmount(40)
				.smallBlindAmount(20)
				.build();


		deck.dealCards(2, io.vavr.collection.List.ofAll(players));

		players.forEach(System.out::println);

		players.forEach(player -> {
			UpdateStateMessage response = UpdateStateMessage.builder()
							.currentPlayer(player)
							.bettingPlayer(players.get(1))
							.allPlayers(players)
							.table(table)
							.build();

			server.sendToTCP(player.getConnectionId(), Message.from(MessageType.UPDATE_STATE, response));
		});
	}

	public static void main(final String[] args) {
		try {
			new TempServer(2);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}