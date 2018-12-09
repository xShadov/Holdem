package com.tp.holdem.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.tp.holdem.core.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KryoClient {

	private transient Client simulationClient;
	private transient GameRenderer renderer;

	public KryoClient(final GameRenderer renderer) {

		try {
			simulationClient = new Client();
			simulationClient.start();
			this.renderer = renderer;

			final Kryo kryo = simulationClient.getKryo();
			kryo.register(SampleResponse.class);
			kryo.register(SampleRequest.class);
			kryo.register(ArrayList.class);
			kryo.register(List.class);
			kryo.register(Honour.class);
			kryo.register(Moves.class);
			kryo.register(Suit.class);
			kryo.register(Player.class);
			kryo.register(Card.class);
			kryo.register(Deck.class);
			kryo.register(PokerTable.class);
			simulationClient.addListener(new Listener() {
				public synchronized void received(final Connection connection, final Object object) {
					if (object instanceof SampleResponse) {
						final SampleResponse response = (SampleResponse) object;
						changes(response.getTag(), response);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Thread("Connect") {
			public synchronized void run() {
				try {
					// Inet4Address.getLocalHost().getHostAddress() if you want
					// your local IP
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

			}
		}.start();
	}

	public void changes(final String TAG, final SampleResponse response) {
		renderer.changesOccurred(TAG, response);
	}

	public Client getSimulationClient() {
		return simulationClient;
	}
}
