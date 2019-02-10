package com.tp.holdem.logic.players

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.common.model.Moves
import com.tp.holdem.logic.table.emptyPotThisPhase
import com.tp.holdem.logic.table.highestBetThisPhase
import io.vavr.collection.List as VavrList

fun Player.prepareForBetInPhase(table: PokerTable): Player {
    if (availableChips() < table.highestBetThisPhase())
        return withMoves(VavrList.of(Moves.FOLD, Moves.ALLIN))

    val modifiedPlayer = this.withBetRanges(
            table.bigBlindAmount,
            this.availableChips()
    )

    return when {
        table.emptyPotThisPhase() -> modifiedPlayer.withMoves(VavrList.of(Moves.BET, Moves.CHECK, Moves.FOLD))
        table.highestBetThisPhase() > modifiedPlayer.betAmountThisPhase -> {
            modifiedPlayer
                    .withMoves(VavrList.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
                    .withBetRanges(
                            table.bigBlindAmount,
                            this.availableChips() - (table.highestBetThisPhase() - this.betAmountThisPhase)
                    )
        }
        else -> modifiedPlayer.withMoves(VavrList.of(Moves.CHECK, Moves.RAISE, Moves.FOLD))
    }
}

fun Player.prepareForFirstBetInRound(table: PokerTable): Player {
    if (availableChips() < table.highestBetThisPhase())
        return withMoves(VavrList.of(Moves.FOLD, Moves.ALLIN))

    return this
            .withMoves(VavrList.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
            .withBetRanges(
                    table.bigBlindAmount,
                    this.chipsAmount - table.highestBetThisPhase()
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

fun Player.availableChips(): Int {
    return chipsAmount - betAmountThisPhase
}