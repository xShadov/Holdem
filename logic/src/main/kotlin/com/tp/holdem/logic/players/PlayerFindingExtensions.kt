package com.tp.holdem.logic.players

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import io.vavr.collection.List as VavrList
import io.vavr.control.Option

val PLAYER_NOT_FOUND = { IllegalStateException("Player not found") }

fun Option<Player>.assertFound(): Player {
    return this.getOrElseThrow(PLAYER_NOT_FOUND)
}

fun VavrList<Player>.byNumber(playerNumber: PlayerNumber): Player {
    return when {
        playerNumber.exists() -> this.find { player -> player.number == playerNumber }.getOrElseThrow(PLAYER_NOT_FOUND)
        else -> throw PLAYER_NOT_FOUND.invoke()
    }
}

fun VavrList<Player>.byNumberOption(playerNumber: PlayerNumber): Option<Player> {
    return if (playerNumber.exists()) this.find { player -> player.number == playerNumber } else Option.none()
}

fun VavrList<Player>.indexOfNumber(playerNumber: PlayerNumber): Int {
    return if (playerNumber.exists()) this.indexWhere { player -> player.number == playerNumber } else -1
}

fun VavrList<Player>.playing(): VavrList<Player> {
    return this.filter { it.playing() }
}

fun VavrList<Player>.notPlaying(): VavrList<Player> {
    return this.filter { it.notPlaying() }
}

fun VavrList<Player>.allIn(): VavrList<Player> {
    return this.filter { it.allIn }
}

fun VavrList<Player>.notAllIn(): VavrList<Player> {
    return this.filter { !it.allIn }
}

fun VavrList<Player>.notBroke(): VavrList<Player> {
    return this.filter { it.chipsAmount > 0 }
}

fun VavrList<Player>.folded(): VavrList<Player> {
    return this.filter { it.folded }
}

fun VavrList<Player>.notFolded(): VavrList<Player> {
    return this.filter { !it.folded }
}

fun VavrList<Player>.inGame(): VavrList<Player> {
    return this.filter { it.inGame }
}

fun VavrList<Player>.notInGame(): VavrList<Player> {
    return this.filter { !it.inGame }
}