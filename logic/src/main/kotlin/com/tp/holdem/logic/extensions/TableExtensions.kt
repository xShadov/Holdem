package com.tp.holdem.logic.extensions

import com.tp.holdem.model.PlayerNumber
import com.tp.holdem.model.PokerTable

fun PokerTable.playerLeft(playerNumber: PlayerNumber): PokerTable {
    val foundPlayer = allPlayers.byNumber(playerNumber)

    val modifiedPlayers = allPlayers
            .replace(foundPlayer, foundPlayer.copy(inGame = false))

    return this.copy(
            allPlayers = modifiedPlayers
    )
}