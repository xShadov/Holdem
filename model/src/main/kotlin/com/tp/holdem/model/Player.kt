package com.tp.holdem.model

import com.tp.holdem.common.model.Moves
import io.vavr.collection.List

data class Player(
        val number: Int = 0,

        val minimumBet: Int = 0,
        val maximumBet: Int = 0,
        val betAmount: Int = 0,
        val betAmountThisPhase: Int = 0,
        val chipsAmount: Int = 0,

        val inGame: Boolean = false,
        val allIn: Boolean = false,
        val folded: Boolean = false,

        val name: String = "Player$number",
        val possibleMoves: List<Moves> = List.empty(),
        val hand: List<Card> = List.empty()
) {
    companion object {
        fun numbered(number: Int): Player {
            return Player(
                    name = "Player$number",
                    number = number
            )
        }
    }
}
