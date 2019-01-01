package com.tp.holdem.logic.table

import com.tp.holdem.logic.players.notBroke
import com.tp.holdem.logic.players.playing
import com.tp.holdem.model.PokerTable

fun PokerTable.highestBetThisPhase(): Int {
    return allPlayers.map { it.betAmountThisPhase }.max().getOrElse(0)
}

fun PokerTable.potAmount(): Int {
    return allPlayers.map { it.betAmount }.sum().toInt()
}

fun PokerTable.potAmountThisPhase(): Int {
    return allPlayers.map { it.betAmountThisPhase }.sum().toInt()
}

fun PokerTable.notEnoughPlayersWithChips(): Boolean {
    return allPlayers
            .playing()
            .notBroke()
            .length() < 2
}