package com.tp.holdem.client;

import com.badlogic.gdx.Game;
import com.tp.holdem.client.architecture.KryoClient;
import com.tp.holdem.client.architecture.action.ActionBus;
import com.tp.holdem.client.architecture.message.MessageBus;
import com.tp.holdem.client.game.GameElements;
import com.tp.holdem.client.game.GameScreen;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.client.game.GameWatcher;
import com.tp.holdem.client.game.rendering.CompositeRenderer;
import com.tp.holdem.client.game.rendering.ElementsRenderer;
import com.tp.holdem.client.game.rendering.StateRenderer;
import io.vavr.collection.List;

public class Holdem extends Game {
	@Override
	public void create() {
		final MessageBus clientToWatcher = new MessageBus();
		final ActionBus elementsToWatcher = new ActionBus();
		final MessageBus watcherToElements = new MessageBus();
		final MessageBus watcherToState = new MessageBus();
		final MessageBus watcherToClient = new MessageBus();

		final GameState gameState = new GameState();
		watcherToState.register(gameState);

		final GameElements gameElements = new GameElements(elementsToWatcher);
		watcherToElements.register(gameElements);

		final GameWatcher watcher = new GameWatcher(watcherToState, watcherToElements, watcherToClient);
		clientToWatcher.register(watcher);
		elementsToWatcher.register(watcher);

		final StateRenderer renderer = new StateRenderer(gameState);
		final ElementsRenderer elementsRenderer = new ElementsRenderer(gameElements);

		final KryoClient client = new KryoClient(clientToWatcher);
		watcherToClient.register(client);

		client.start();

		setScreen(new GameScreen(watcher, gameElements, gameState, CompositeRenderer.of(List.of(renderer, elementsRenderer))));
	}
}
