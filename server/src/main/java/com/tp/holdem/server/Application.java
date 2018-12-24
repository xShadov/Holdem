package com.tp.holdem.server;

import com.esotericsoftware.kryonet.Server;

public class Application {
	public static void main(String[] args) {
		final Server server = new Server();

		final GameParams gameParams = GameParams.builder().bigBlindAmount(40)
				.smallBlindAmount(20)
				.playerCount(2)
				.port(54555)
				.startingChips(1500)
				.build();

		final GameHandler gameHandler = new GameHandler(gameParams);

		final TempServer gameServer = new TempServer(server, gameHandler, gameParams);
		gameServer.start();
	}
}
