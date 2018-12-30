package com.tp.holdem.logic

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.common.model.Moves
import io.vavr.collection.List

fun Player.betInPhase(table: PokerTable): Player {
    //TODO if player HAS TO all in show only all in and fold, not call and raise
    val modifiedPlayer = this.betRanges(
            Math.min(this.availableChips(), table.bigBlindAmount),
            this.availableChips()
    )

    if (table.potAmountThisPhase() == 0)
        return modifiedPlayer.withMoves(
                List.of(Moves.BET, Moves.CHECK, Moves.FOLD)
        )

    return if (modifiedPlayer.betAmountThisPhase < table.highestBetThisPhase()) modifiedPlayer
            .withMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
            .betRanges(
                    Math.min(table.bigBlindAmount, this.availableChips() - (table.highestBetThisPhase() - this.betAmountThisPhase)),
                    this.availableChips() - (table.highestBetThisPhase() - this.betAmountThisPhase)
            ) else modifiedPlayer.withMoves(List.of(Moves.CHECK, Moves.RAISE, Moves.FOLD))
}

fun Player.firstBetInRound(table: PokerTable): Player {
    return this.withMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
            .betRanges(
                    Math.min(this.availableChips(), table.bigBlindAmount),
                    this.availableChips() - (table.highestBetThisPhase() - this.betAmountThisPhase)
            )
}

fun Player.betSmallBlind(table: PokerTable): Player {
    return if (this.availableChips() < table.smallBlindAmount) this.allIn() else this.bet(table.smallBlindAmount)
}

fun Player.betBigBlind(table: PokerTable): Player {
    return if (this.availableChips() < table.bigBlindAmount) this.allIn() else this.bet(table.bigBlindAmount)
}