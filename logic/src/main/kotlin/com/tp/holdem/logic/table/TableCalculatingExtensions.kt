package com.tp.holdem.logic.table

import com.tp.holdem.logic.players.inGame
import com.tp.holdem.logic.players.notAllIn
import com.tp.holdem.logic.players.notBroke
import com.tp.holdem.logic.players.playing
import com.tp.holdem.logic.model.PokerTable

fun PokerTable.highestBetThisPhase(): Int {
    return allPlayers.inGame().map { it.betAmountThisPhase }.max().getOrElse(0)
}

fun PokerTable.potAmount(): Int {
    return allPlayers.inGame().map { it.betAmount }.sum().toInt()
}

fun PokerTable.potAmountThisPhase(): Int {
    return allPlayers.inGame().map { it.betAmountThisPhase }.sum().toInt()
}

fun PokerTable.notEnoughPlayersWithChips(): Boolean {
    return allPlayers.inGame()
            .playing()
            .notBroke()
            .length() < 2
}

fun PokerTable.allPlayersMoved(): Boolean {
    return latestMoves.keySet().containsAll(allPlayers.inGame().map { it.number })
}

fun PokerTable.allBetsEqualToHighestBet(): Boolean {
    val highestBetThisPhase = highestBetThisPhase()
    return allPlayers.playing().notAllIn()
            .forAll { it.betAmountThisPhase == highestBetThisPhase }
}