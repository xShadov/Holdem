package com.tp.holdem.client.architecture.message;

import com.tp.holdem.common.message.Message;

public interface ServerObservable {
	void accept(Message event);
}
