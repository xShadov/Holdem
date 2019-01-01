package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.players.byNumberOption
import com.tp.holdem.model.Player
import com.tp.holdem.model.PokerTable
import io.vavr.collection.List
import io.vavr.control.Option

fun PokerTable.getBettingPlayer(): Option<Player> {
    return allPlayers.byNumberOption(bettingPlayer)
}

fun PokerTable.getWinnerPlayer(): Option<Player> {
    return allPlayers.byNumberOption(winnerPlayer)
}

fun PokerTable.getDealer(): Option<Player> {
    return allPlayers.byNumberOption(dealer)
}

fun PokerTable.getBigBlind(): Option<Player> {
    return allPlayers.byNumberOption(bigBlind)
}

fun PokerTable.getSmallBlind(): Option<Player> {
    return allPlayers.byNumberOption(smallBlind)
}

fun PokerTable.playerNames(): List<String> {
    return this.allPlayers.map { it.name }
}

fun PokerTable.lastPlayingPhase(): Boolean {
    return phase == Phase.RIVER
}