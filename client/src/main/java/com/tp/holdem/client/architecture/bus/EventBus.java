package com.tp.holdem.client.architecture.bus;

import io.vavr.collection.List;

public class EventBus {
	private List<ServerObservable> observables = List.empty();

	public void register(ServerObservable observable) {
		observables = observables.push(observable);
	}

	public void message(Event response) {
		observables.forEach(observable -> observable.accept(response));
	}
}
