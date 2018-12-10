package com.tp.holdem.client;

import com.badlogic.gdx.Game;
import com.tp.holdem.client.architecture.KryoClient;
import com.tp.holdem.client.architecture.bus.ActionBus;
import com.tp.holdem.client.architecture.bus.EventBus;
import com.tp.holdem.client.game.*;

public class Holdem extends Game {
	@Override
	public void create() {
		final EventBus clientToWatcher = new EventBus();
		final ActionBus elementsToWatcher = new ActionBus();
		final ActionBus watcherToState = new ActionBus();

		final GameState gameState = new GameState();
		watcherToState.register(gameState);

		final GameElements gameElements = new GameElements(elementsToWatcher);

		final GameWatcher watcher = new GameWatcher(watcherToState);
		clientToWatcher.register(watcher);
		elementsToWatcher.register(watcher);

		final GameRenderer renderer = new GameRenderer(gameState);

		final KryoClient client = new KryoClient(clientToWatcher);
		client.start();

		setScreen(new GameScreen(watcher, renderer, gameElements, gameState));
	}
}
