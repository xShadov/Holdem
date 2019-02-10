package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.byNumber
import com.tp.holdem.logic.players.byNumberOption
import io.vavr.collection.List as VavrList
import io.vavr.control.Option

fun PokerTable.getBettingPlayer(): Option<Player> {
    return allPlayers.byNumberOption(bettingPlayerNumber)
}

fun PokerTable.getWinnerPlayers(): VavrList<Player> {
    return allPlayers.filter { player -> winnerPlayerNumbers.contains(player.number) }
}

fun PokerTable.getDealer(): Option<Player> {
    return allPlayers.byNumberOption(dealerPlayerNumber)
}

fun PokerTable.getBigBlind(): Option<Player> {
    return allPlayers.byNumberOption(bigBlindPlayerNumber)
}

fun PokerTable.getSmallBlind(): Option<Player> {
    return allPlayers.byNumberOption(smallBlindPlayerNumber)
}

fun PokerTable.playerNames(): VavrList<String> {
    return this.allPlayers.map { it.name }
}

fun PokerTable.playerNumbers(): VavrList<PlayerNumber> {
    return this.allPlayers
            .map { it.number }
}

fun PokerTable.playerCount(): Int {
    return this.allPlayers.size()
}

fun PokerTable.currentPhase(): Phase {
    return this.phase
}

fun PokerTable.latestPlayer(): Option<PlayerNumber> {
    return this.allPlayers.lastOption().map { it.number }
}

fun PokerTable.currentlyBetting(): PlayerNumber {
    return this.bettingPlayerNumber
}

fun PokerTable.inShowdownMode(): Boolean {
    return this.showdown
}

fun PokerTable.findPlayer(number: PlayerNumber): Player {
    return allPlayers.byNumber(number)
}

fun PokerTable.lastPlayingPhase(): Boolean {
    return phase.isLastPlayingPhase
}

fun PokerTable.emptyPotThisPhase(): Boolean {
    return potAmountThisPhase() == 0
}

fun PokerTable.emptyPot(): Boolean {
    return potAmount() == 0
}