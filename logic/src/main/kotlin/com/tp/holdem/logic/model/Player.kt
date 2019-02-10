package com.tp.holdem.logic.model

import com.tp.holdem.common.model.Moves
import io.vavr.collection.List as VavrList

data class Player(
        internal val number: PlayerNumber = PlayerNumber.empty(),

        internal val minimumBet: Int = 0,
        internal val maximumBet: Int = 0,
        internal val betAmount: Int = 0,
        internal val betAmountThisPhase: Int = 0,
        internal val chipsAmount: Int = 0,

        internal val inGame: Boolean = false,
        internal val allIn: Boolean = false,
        internal val folded: Boolean = false,

        internal val name: String = "Player${number.number}",
        internal val possibleMoves: VavrList<Moves> = VavrList.empty(),
        internal val hand: VavrList<Card> = VavrList.empty()
) {
    companion object {
        @JvmStatic
        fun numbered(number: PlayerNumber): Player {
            if (number.number < 0)
                throw IllegalArgumentException("Player number should be positive integer")

            return Player(
                    number = number
            )
        }
    }
}
