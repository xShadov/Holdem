package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.players.byNumber
import com.tp.holdem.logic.players.gameOver
import com.tp.holdem.logic.players.prepareForNewGame
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable

fun PokerTable.gameOver(): PokerTable {
    return this.copy(
            gameOver = true,
            phase = Phase.OVER,
            allPlayers = allPlayers.map { it.gameOver() }
    )
}

fun PokerTable.addPlayer(player: Player): PokerTable {
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

fun PokerTable.preparePlayersForNewGame(startingChips: Int): PokerTable {
    return this.copy(
            allPlayers = allPlayers.map { it.prepareForNewGame(startingChips) }
    )
}