package com.tp.holdem.logic.table

import com.tp.holdem.logic.players.inGame
import com.tp.holdem.logic.players.notAllIn
import com.tp.holdem.logic.players.notBroke
import com.tp.holdem.logic.players.playing
import com.tp.holdem.logic.model.PokerTable

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

fun PokerTable.emptyPotThisPhase(): Boolean {
    return potAmountThisPhase() == 0
}

fun PokerTable.emptyPot(): Boolean {
    return potAmount() == 0
}

fun PokerTable.allPlayersMoved(): Boolean {
    return allPlayers.inGame().size() == latestMoves.size()
}

fun PokerTable.allBetsEqual(): Boolean {
    return allPlayers.playing().notAllIn()
            .forAll { player -> player.betAmountThisPhase == highestBetThisPhase() }
}

fun PokerTable.lastPlayingPhase(): Boolean {
    return phase.isLastPlayingPhase
}