package com.tp.holdem.logic.players

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.common.model.Moves
import com.tp.holdem.logic.table.emptyPotThisPhase
import com.tp.holdem.logic.table.highestBetThisPhase
import io.vavr.collection.List

fun Player.betInPhase(table: PokerTable): Player {
    if (availableChips() < table.bigBlindAmount)
        return withMoves(List.of(Moves.FOLD, Moves.ALLIN))

    val modifiedPlayer = this.withBetRanges(
            table.bigBlindAmount,
            this.availableChips()
    )

    return when {
        table.emptyPotThisPhase() -> modifiedPlayer.withMoves(List.of(Moves.BET, Moves.CHECK, Moves.FOLD))
        table.highestBetThisPhase() > modifiedPlayer.betAmountThisPhase -> {
            modifiedPlayer
                    .withMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
                    .withBetRanges(
                            Math.min(table.bigBlindAmount, this.availableChips() - (table.highestBetThisPhase() - this.betAmountThisPhase)),
                            this.availableChips() - (table.highestBetThisPhase() - this.betAmountThisPhase)
                    )
        }
        else -> modifiedPlayer.withMoves(List.of(Moves.CHECK, Moves.RAISE, Moves.FOLD))
    }
}

fun Player.firstBetInRound(table: PokerTable): Player {
    if (availableChips() < table.bigBlindAmount)
        return withMoves(List.of(Moves.FOLD, Moves.ALLIN))

    return this
            .withMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
            .withBetRanges(
                    Math.min(this.availableChips(), table.bigBlindAmount),
                    this.availableChips() - (table.highestBetThisPhase() - this.betAmountThisPhase)
            )
}

fun Player.betSmallBlind(table: PokerTable): Player {
    return when {
        this.availableChips() < table.smallBlindAmount -> this.allIn()
        else -> this.bet(table.smallBlindAmount)
    }
}

fun Player.betBigBlind(table: PokerTable): Player {
    return when {
        this.availableChips() < table.bigBlindAmount -> this.allIn()
        else -> this.bet(table.bigBlindAmount)
    }
}