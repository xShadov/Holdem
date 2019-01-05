package com.tp.holdem.server

import com.tp.holdem.logic.model.PlayerNumber
import io.vavr.Tuple
import io.vavr.Tuple2
import io.vavr.collection.HashMap
import io.vavr.collection.Map
import io.vavr.control.Option

internal class ConnectedPlayers(private val connectedMap: Map<Int, Tuple2<Boolean, PlayerNumber>>) {
    companion object {
        @JvmStatic
        fun empty(): ConnectedPlayers {
            return ConnectedPlayers(HashMap.empty())
        }
    }

    fun isConnected(connectionId: Int): Boolean {
        return connectedMap.get(connectionId)
                .exists { it._1() }
    }

    fun wasConnected(connectionId: Int): Boolean {
        return connectedMap.containsKey(connectionId)
    }

    fun getConnectionId(playerNumber: PlayerNumber): Option<Int> {
        return if (playerNumber.exists()) getConnectionId(playerNumber.number) else Option.none()
    }

    private fun getConnectionId(playerNumber: Int): Option<Int> {
        return connectedMap
                .mapValues { it._2() }
                .mapValues { it.number }
                .find { value -> value._2 == playerNumber }
                .map { it._1() }
    }

    fun getConnected(connectionId: Int): Option<PlayerNumber> {
        return connectedMap.get(connectionId)
                .filter { it._1() }
                .map { it._2() }
    }

    fun connect(connectionId: Int, playerNumber: PlayerNumber): ConnectedPlayers {
        return ConnectedPlayers(connectedMap.put(connectionId, Tuple.of(true, playerNumber)))
    }

    fun reconnect(connectionId: Int, playerNumber: PlayerNumber): ConnectedPlayers {
        return ConnectedPlayers(connectedMap.replaceValue(connectionId, Tuple.of(true, playerNumber)))
    }

    fun size(): Int {
        return connectedMap.size()
    }

    fun forEach(action: (Int, PlayerNumber) -> Unit) {
        connectedMap
                .mapValues { it._2() }
                .forEach(action)
    }
}
