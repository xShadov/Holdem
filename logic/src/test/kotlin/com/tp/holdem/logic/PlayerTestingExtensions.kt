package com.tp.holdem.logic

import com.tp.holdem.common.model.Moves
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import io.vavr.collection.List

private fun Player.Companion.sample(number: Int): Player {
    return Player.numbered(PlayerNumber.of(number)).copy(
            chipsAmount = 1500
    )
}

fun Player.Companion.sitting(number: Int) = Player.sample(number).copy(inGame = false)

fun Player.Companion.playing(number: Int) = Player.sample(number).copy(inGame = true)

fun Player.Companion.folded(number: Int) = Player.sample(number).copy(inGame = true, folded = true)

fun Player.Companion.allIn(number: Int) = Player.sample(number).copy(inGame = true, allIn = true)

fun Player.chips(chips: Int): Player {
    return this.copy(
            chipsAmount = chips
    )
}

fun Player.betThisPhase(amount: Int): Player {
    return this.copy(
            betAmountThisPhase = amount
    )
}

fun Player.isNotInBettingTurn(): Boolean {
    return !isDuringBettingTurn()
}

fun Player.isDuringBettingTurn(): Boolean {
    return possibleMoves != List.empty<Moves>() && maximumBet != 0 && minimumBet != 0
}