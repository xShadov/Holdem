package com.tp.holdem.logic.players

import com.tp.holdem.common.model.Moves
import com.tp.holdem.logic.hands.findHandRank
import com.tp.holdem.logic.model.*
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

fun Player.handRank(table: PokerTable): HandRank {
    return hand.appendAll(table.cardsOnTable).findHandRank()
}

fun Player.number(): PlayerNumber {
    return PlayerNumber.of(this.number)
}