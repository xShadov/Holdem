package com.tp.holdem.logic.players

import com.tp.holdem.logic.model.Player
import io.vavr.collection.List as VavrList

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
            possibleMoves = VavrList.empty(),
            maximumBet = 0,
            minimumBet = 0,
            folded = false,
            allIn = false,
            hand = VavrList.empty()
    )
}

fun Player.prepareForNewPhase(): Player {
    return this.copy(
            betAmount = betAmount + betAmountThisPhase,
            betAmountThisPhase = 0,
            chipsAmount = chipsAmount - betAmountThisPhase,
            minimumBet = 0,
            maximumBet = 0,
            possibleMoves = VavrList.empty()
    )
}

fun Player.roundOver(): Player {
    return when (availableChips()) {
        0 -> this.gameOver()
        else -> this.copy(
                betAmount = betAmount + betAmountThisPhase,
                betAmountThisPhase = 0,
                chipsAmount = chipsAmount - betAmountThisPhase
        )
    }
}

fun Player.bettingTurnOver(): Player {
    return this.copy(
            minimumBet = 0,
            maximumBet = 0,
            possibleMoves = VavrList.empty()
    )
}

fun Player.gameOver(): Player {
    return this.copy(
            betAmount = 0,
            betAmountThisPhase = 0,
            chipsAmount = 0,
            minimumBet = 0,
            maximumBet = 0,
            inGame = false,
            possibleMoves = VavrList.empty()
    )
}