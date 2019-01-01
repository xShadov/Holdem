package com.tp.holdem.client.architecture.action;

import io.vavr.collection.List;

public class ActionBus {
	private List<GameObservable> observables = List.empty();

	public void register(GameObservable observable) {
		observables = observables.append(observable);
	}

	public void message(Action action) {
		observables.forEach(observable -> observable.accept(action));
	}
}
