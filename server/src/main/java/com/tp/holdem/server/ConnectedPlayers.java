package com.tp.holdem.server;

import com.tp.holdem.logic.model.PlayerNumber;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;

@AllArgsConstructor
class ConnectedPlayers {
	private final Map<Integer, Tuple2<Boolean, PlayerNumber>> connectedMap;

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

	Option<Integer> getConnectionId(int playerNumber) {
		return connectedMap
				.mapValues(Tuple2::_2)
				.mapValues(PlayerNumber::getNumber)
				.find(value -> value._2.equals(playerNumber))
				.map(Tuple2::_1);
	}

	Option<Integer> getConnected(int connectionId) {
		return connectedMap.get(connectionId)
				.filter(Tuple2::_1)
				.map(Tuple2::_2)
				.map(PlayerNumber::getNumber);
	}

	ConnectedPlayers connect(Integer connectionId, Integer playerNumber) {
		return new ConnectedPlayers(connectedMap.put(connectionId, Tuple.of(true, PlayerNumber.of(playerNumber))));
	}

	ConnectedPlayers reconnect(Integer connectionId, Integer playerNumber) {
		return new ConnectedPlayers(connectedMap.replaceValue(connectionId, Tuple.of(true, PlayerNumber.of(playerNumber))));
	}

	int size() {
		return connectedMap.size();
	}

	void forEach(BiConsumer<Integer, Integer> action) {
		connectedMap
				.mapValues(Tuple2::_2)
				.mapValues(PlayerNumber::getNumber)
				.forEach(action);
	}
}
