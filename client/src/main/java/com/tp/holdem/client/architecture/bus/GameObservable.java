package com.tp.holdem.client.architecture.bus;

public interface GameObservable {
	void accept(Action action);
}
