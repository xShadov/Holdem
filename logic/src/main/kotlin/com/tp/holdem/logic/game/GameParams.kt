package com.tp.holdem.logic.game

data class GameParams(
        val playerCount: Int = 0,
        val startingChips: Int = 0,
        val bigBlindAmount: Int = 0,
        val smallBlindAmount: Int = 0,
        val port: Int = 54555
)
