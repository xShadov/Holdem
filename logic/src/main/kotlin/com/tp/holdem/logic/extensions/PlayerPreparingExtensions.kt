package com.tp.holdem.logic.extensions

import com.tp.holdem.model.Player
import io.vavr.collection.List

fun Player.prepareForNewGame(startingChips: Int): Player {
    return this.copy(
            chipsAmount = startingChips,
            inGame = true
    )
}

fun Player.prepareForNewRound(): Player {
    return this.copy(
            betAmount = 0,
            betAmountThisPhase = 0,
            possibleMoves = List.empty(),
            maximumBet = 0,
            minimumBet = 0,
            folded = false,
            allIn = false,
            hand = List.empty()
    )
}

fun Player.prepareForNewPhase(): Player {
    return this.copy(
            betAmount = betAmount + betAmountThisPhase,
            betAmountThisPhase = 0,
            chipsAmount = chipsAmount - betAmountThisPhase,
            minimumBet = 0,
            maximumBet = 0,
            possibleMoves = List.empty()
    )
}

fun Player.roundOver(): Player {
    return this.copy(
            betAmount = betAmount + betAmountThisPhase,
            betAmountThisPhase = 0,
            chipsAmount = chipsAmount - betAmountThisPhase
    )
}

fun Player.bettingTurnOver(): Player {
    return this.copy(
            minimumBet = 0,
            maximumBet = 0,
            possibleMoves = List.empty()
    )
}

fun Player.gameOver(): Player {
    return this.copy(
            minimumBet = 0,
            maximumBet = 0,
            inGame = false,
            possibleMoves = List.empty()
    )
}