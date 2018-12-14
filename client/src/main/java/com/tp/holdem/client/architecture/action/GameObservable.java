package com.tp.holdem.client.architecture.action;

import com.tp.holdem.client.architecture.action.Action;

public interface GameObservable {
	void accept(Action action);
}
