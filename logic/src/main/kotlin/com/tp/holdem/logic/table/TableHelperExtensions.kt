package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.byNumber
import com.tp.holdem.logic.players.byNumberOption
import io.vavr.collection.List
import io.vavr.control.Option

fun PokerTable.getBettingPlayer(): Option<Player> {
    return allPlayers.byNumberOption(bettingPlayerNumber)
}

fun PokerTable.getWinnerPlayer(): Option<Player> {
    return allPlayers.byNumberOption(winnerPlayerNumber)
}

fun PokerTable.getDealer(): Option<Player> {
    return allPlayers.byNumberOption(dealerNumber)
}

fun PokerTable.getBigBlind(): Option<Player> {
    return allPlayers.byNumberOption(bigBlindPlayerNumber)
}

fun PokerTable.getSmallBlind(): Option<Player> {
    return allPlayers.byNumberOption(smallBlindPlayerNumber)
}

fun PokerTable.playerNames(): List<String> {
    return this.allPlayers.map { it.name }
}

fun PokerTable.playerNumbers(): List<PlayerNumber> {
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