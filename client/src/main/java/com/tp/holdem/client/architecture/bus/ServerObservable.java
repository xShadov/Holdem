package com.tp.holdem.client.architecture.bus;

public interface ServerObservable {
	void accept(Event event);
}
