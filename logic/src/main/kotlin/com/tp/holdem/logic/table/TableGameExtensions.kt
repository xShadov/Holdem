package com.tp.holdem.logic.table

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.byNumber
import com.tp.holdem.logic.players.gameOver
import com.tp.holdem.logic.players.prepareForNewGame

fun PokerTable.gameOver(): PokerTable {
    return this.copy(
            gameOver = true,
            allPlayers = allPlayers.map { it.gameOver() }
    )
}

fun PokerTable.addPlayer(player: Player): PokerTable {
    if (allPlayers.map { it.number }.contains(player.number))
        throw IllegalStateException("There is already player with this number at the table")

    return this.copy(
            allPlayers = allPlayers.append(player)
    )
}

fun PokerTable.playerLeft(playerNumber: PlayerNumber): PokerTable {
    val foundPlayer = allPlayers.byNumber(playerNumber)

    val modifiedPlayers = allPlayers
            .replace(foundPlayer, foundPlayer.copy(inGame = false))

    return this.copy(
            allPlayers = modifiedPlayers
    )
}

fun PokerTable.newGame(startingChips: Int): PokerTable {
    if (startingChips < bigBlindAmount)
        throw IllegalArgumentException("Starting player chips should be at least equal to big blind")
    return this.copy(
            allPlayers = allPlayers.map { it.prepareForNewGame(startingChips) }
    )
}