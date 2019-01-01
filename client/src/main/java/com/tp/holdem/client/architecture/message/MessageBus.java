package com.tp.holdem.client.architecture.message;

import com.tp.holdem.common.message.Message;
import io.vavr.collection.List;

public class MessageBus {
	private List<ServerObservable> observables = List.empty();

	public void register(ServerObservable observable) {
		observables = observables.append(observable);
	}

	public void message(Message response) {
		observables.forEach(observable -> observable.accept(response));
	}
}
