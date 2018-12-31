package com.tp.holdem.logic.extensions

import com.tp.holdem.model.Player

fun Player.allIn(): Player {
    return this.copy(
            allIn = true,
            betAmountThisPhase = availableChips()
    )
}

fun Player.fold(): Player {
    return this.copy(
            folded = true
    )
}

fun Player.bet(bet: Int): Player {
    if (availableChips() < bet)
        throw IllegalArgumentException(String.format("Player does not have enough chips: %d vs. %d", availableChips(), bet))

    val afterBet = this.copy(betAmountThisPhase = betAmountThisPhase + bet)
    return afterBet.copy(
            allIn = afterBet.availableChips() == 0
    )
}