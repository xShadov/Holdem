package com.tp.holdem.server;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;

@AllArgsConstructor
class ConnectedPlayers {
	private final Map<Integer, Tuple2<Boolean, Integer>> connectedMap;

	static ConnectedPlayers empty() {
		return new ConnectedPlayers(HashMap.empty());
	}

	boolean isConnected(int connectionId) {
		return connectedMap.get(connectionId)
				.exists(Tuple2::_1);
	}

	boolean wasConnected(int connectionId) {
		return connectedMap.containsKey(connectionId);
	}

	Option<Integer> getConnected(int connectionId) {
		return connectedMap.get(connectionId)
				.filter(Tuple2::_1)
				.map(Tuple2::_2);
	}

	ConnectedPlayers connect(Integer connectionId, Integer playerNumber) {
		return new ConnectedPlayers(connectedMap.put(connectionId, Tuple.of(true, playerNumber)));
	}

	ConnectedPlayers reconnect(Integer connectionId, Integer playerNumber) {
		return new ConnectedPlayers(connectedMap.replaceValue(connectionId, Tuple.of(true, playerNumber)));
	}

	int size() {
		return connectedMap.size();
	}

	void forEach(BiConsumer<Integer, Integer> action) {
		connectedMap
				.mapValues(Tuple2::_2)
				.forEach(action);
	}
}
