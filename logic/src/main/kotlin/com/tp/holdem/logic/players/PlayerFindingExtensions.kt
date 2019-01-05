package com.tp.holdem.logic.players

import com.tp.holdem.logic.hands.HandOperations
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import io.vavr.collection.List
import io.vavr.control.Either
import io.vavr.control.Option

val PLAYER_NOT_FOUND = { IllegalStateException("Player not found") }

fun Option<Player>.assertFound(): Player {
    return this.getOrElseThrow(PLAYER_NOT_FOUND)
}

fun List<Player>.byNumber(playerNumber: PlayerNumber): Player {
    return when {
        playerNumber.exists() -> this.find { player -> player.number == playerNumber }.getOrElseThrow(PLAYER_NOT_FOUND)
        else -> throw PLAYER_NOT_FOUND.invoke()
    }
}

fun List<Player>.byNumberOption(playerNumber: PlayerNumber): Option<Player> {
    return if (playerNumber.exists()) this.find { player -> player.number == playerNumber } else Option.none()
}

fun List<Player>.indexOfNumber(playerNumber: PlayerNumber): Int {
    return if (playerNumber.exists()) this.indexWhere { player -> player.number == playerNumber } else -1
}

fun List<Player>.winner(table: PokerTable): Either<Player, List<Player>> {
    return HandOperations.findWinner(this, table)
}

fun List<Player>.playing(): List<Player> {
    return this.filter { it.playing() }
}

fun List<Player>.notPlaying(): List<Player> {
    return this.filter { it.notPlaying() }
}

fun List<Player>.allIn(): List<Player> {
    return this.filter { it.allIn }
}

fun List<Player>.notAllIn(): List<Player> {
    return this.filter { !it.allIn }
}

fun List<Player>.notBroke(): List<Player> {
    return this.filter { it.chipsAmount > 0 }
}

fun List<Player>.folded(): List<Player> {
    return this.filter { it.folded }
}

fun List<Player>.notFolded(): List<Player> {
    return this.filter { !it.folded }
}

fun List<Player>.inGame(): List<Player> {
    return this.filter { it.inGame }
}

fun List<Player>.notInGame(): List<Player> {
    return this.filter { !it.inGame }
}