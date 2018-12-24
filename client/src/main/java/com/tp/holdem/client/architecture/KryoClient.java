package com.tp.holdem.client.architecture;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.tp.holdem.client.architecture.message.MessageBus;
import com.tp.holdem.client.architecture.message.ServerObservable;
import com.tp.holdem.model.game.*;
import com.tp.holdem.model.message.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class KryoClient implements ServerObservable {
	private final MessageBus withWatcher;
	private final Client client;

	public KryoClient(MessageBus withWatcher) {
		this.withWatcher = withWatcher;
		this.client = new Client();
	}

	public void start() {
		client.start();

		final AtomicInteger registerCount = new AtomicInteger(16);

		final Kryo kryo = client.getKryo();
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
		kryo.register(PlayerActionMessage.class, registerCount.getAndIncrement());

		client.addListener(new Listener() {
			public synchronized void received(final Connection connection, final Object responseObject) {
				if (responseObject instanceof Message)
					withWatcher.message((Message) responseObject);
				else if (responseObject instanceof FrameworkMessage)
					log.info("Received framework message");
				else
					log.error(String.format("Unknown message type from server: %s", responseObject));
			}
		});

		new Thread(() -> {
			try {
				client.connect(5000, "127.0.0.1", 54555);
				while (true) {
					Thread.sleep(100);
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}).start();
	}

	@Override
	public void accept(Message event) {
		log.debug(String.format("Sending message to server: %s", event));
		client.sendTCP(event);
	}
}
