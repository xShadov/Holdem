package com.tp.holdem.logic.players

import com.tp.holdem.logic.model.Player

fun Player.allIn(): Player {
    if (notPlaying())
        throw IllegalStateException("Player is not in the game and cannot move")
    if (allIn)
        throw IllegalStateException("Player is already all in")
    if (availableChips() == 0)
        throw IllegalStateException("Player has no chips, should be off the game")

    return this.copy(
            allIn = true,
            betAmountThisPhase = availableChips()
    )
}

fun Player.fold(): Player {
    if (notPlaying())
        throw IllegalStateException("Player is not in the game and cannot move")

    return this.copy(
            folded = true
    )
}

fun Player.bet(bet: Int): Player {
    if (notPlaying())
        throw IllegalStateException("Player is not in the game and cannot move")
    if (availableChips() < bet)
        throw IllegalStateException("Player does not have enough chips: ${availableChips()} vs. $bet")

    val newBetAmount = betAmountThisPhase + bet
    return this.copy(
            betAmountThisPhase = newBetAmount,
            allIn = availableChips() - bet == 0
    )
}