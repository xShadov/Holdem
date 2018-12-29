package com.tp.holdem.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.model.common.Honour;
import com.tp.holdem.model.common.Moves;
import com.tp.holdem.model.common.Phase;
import com.tp.holdem.model.common.Suit;
import com.tp.holdem.model.message.*;
import com.tp.holdem.model.message.dto.CardDTO;
import com.tp.holdem.model.message.dto.CurrentPlayerDTO;
import com.tp.holdem.model.message.dto.PlayerDTO;
import com.tp.holdem.model.message.dto.PokerTableDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class KryoServer implements Runnable {
	private final Server server;
	private final Listener serverListener;
	private final int port;

	KryoServer(Server server, GameParams params, Listener serverListener) {
		this.server = server;
		this.serverListener = serverListener;
		this.port = params.getPort();

		final AtomicInteger registerCount = new AtomicInteger(16);

		final Kryo kryo = server.getKryo();
		kryo.register(java.util.ArrayList.class, registerCount.getAndIncrement());
		kryo.register(java.util.List.class, registerCount.getAndIncrement());
		kryo.register(Honour.class, registerCount.getAndIncrement());
		kryo.register(Suit.class, registerCount.getAndIncrement());
		kryo.register(Phase.class, registerCount.getAndIncrement());
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

	void start() {
		try {
			server.addListener(serverListener);

			server.bind(port);
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
}