package com.tp.holdem.client.architecture;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.tp.holdem.client.architecture.bus.Event;
import com.tp.holdem.client.architecture.bus.EventBus;
import com.tp.holdem.client.architecture.model.ClientMoveRequest;
import com.tp.holdem.client.architecture.model.common.UpdateStateMessage;
import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;
import com.tp.holdem.client.architecture.model.event.EventType;
import com.tp.holdem.client.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class KryoClient {
	private final EventBus withWatcher;

	public KryoClient(EventBus withWatcher) {
		this.withWatcher = withWatcher;
	}

	public void start() {
		Client simulationClient = new Client();
		simulationClient.start();

		final AtomicInteger registerCount = new AtomicInteger(16);

		final Kryo kryo = simulationClient.getKryo();
		kryo.register(ClientMoveRequest.class, registerCount.getAndIncrement());
		kryo.register(ArrayList.class, registerCount.getAndIncrement());
		kryo.register(List.class, registerCount.getAndIncrement());
		kryo.register(Honour.class,registerCount.getAndIncrement());
		kryo.register(Suit.class,registerCount.getAndIncrement());
		kryo.register(Player.class,registerCount.getAndIncrement());
		kryo.register(Card.class,registerCount.getAndIncrement());
		kryo.register(Deck.class,registerCount.getAndIncrement());
		kryo.register(PokerTable.class,registerCount.getAndIncrement());

		kryo.register(Event.class,registerCount.getAndIncrement());
		kryo.register(EventType.class,registerCount.getAndIncrement());
		kryo.register(PlayerConnectMessage.class,registerCount.getAndIncrement());
		kryo.register(UpdateStateMessage.class,registerCount.getAndIncrement());

		simulationClient.addListener(new Listener() {
			public synchronized void received(final Connection connection, final Object responseObject) {
				if (responseObject instanceof Event) {
					final Event response = (Event) responseObject;
					withWatcher.message(response);
				} else
					log.error("Unknown message type from server");
			}
		});

		new Thread(() -> {
			try {
				simulationClient.connect(5000, "127.0.0.1", 54555);
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}).start();
	}
}
