package com.tp.holdem.client.architecture;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.tp.holdem.model.game.*;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.client.architecture.message.MessageBus;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.PlayerConnectMessage;
import com.tp.holdem.model.message.UpdateStateMessage;
import com.tp.holdem.client.game.GameState;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class KryoClient {
	private final MessageBus withWatcher;

	public KryoClient(MessageBus withWatcher) {
		this.withWatcher = withWatcher;
	}

	public void start() {
		Client simulationClient = new Client();
		simulationClient.start();

		final AtomicInteger registerCount = new AtomicInteger(16);

		final Kryo kryo = simulationClient.getKryo();
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

		simulationClient.addListener(new Listener() {
			public synchronized void received(final Connection connection, final Object responseObject) {
				if (responseObject instanceof Message) {
					final Message response = (Message) responseObject;
					withWatcher.message(response);
				} else if (responseObject instanceof FrameworkMessage)
					log.info("Received framework message");
				else
					log.error(String.format("Unknown message type from server: %s", responseObject));
			}
		});

		new Thread(() -> {
			try {
				simulationClient.connect(5000, "127.0.0.1", 54555);
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}).start();
	}
}
