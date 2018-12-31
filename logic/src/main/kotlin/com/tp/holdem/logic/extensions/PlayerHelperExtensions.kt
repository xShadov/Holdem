package com.tp.holdem.logic.extensions

import com.tp.holdem.common.model.Moves
import com.tp.holdem.model.Card
import com.tp.holdem.model.Player
import io.vavr.collection.List

fun Player.withCards(cards: List<Card>): Player {
    return this.copy(hand = cards)
}

fun Player.withMoves(moves: List<Moves>): Player {
    return this.copy(
            possibleMoves = moves
    )
}

fun Player.withBetRanges(min: Int, max: Int): Player {
    return this.copy(
            minimumBet = min,
            maximumBet = max
    )
}

fun Player.playing(): Boolean {
    return inGame && !folded
}

fun Player.notPlaying(): Boolean {
    return !playing()
}

fun Player.availableChips(): Int {
    return chipsAmount - betAmountThisPhase
}