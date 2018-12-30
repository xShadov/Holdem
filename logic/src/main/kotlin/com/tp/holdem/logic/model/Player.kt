package com.tp.holdem.logic.model

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

    fun withCards(cards: List<Card>): Player {
        return this.copy(hand = cards)
    }

    fun playing(): Boolean {
        return inGame && !folded
    }

    fun notPlaying(): Boolean {
        return !playing()
    }

    fun availableChips(): Int {
        return chipsAmount - betAmountThisPhase
    }

    fun allIn(): Player {
        return this.copy(
                allIn = true,
                betAmountThisPhase = availableChips()
        )
    }

    fun fold(): Player {
        return this.copy(
                folded = true
        )
    }

    fun bet(bet: Int): Player {
        if (availableChips() < bet)
            throw IllegalArgumentException(String.format("Player does not have enough chips: %d vs. %d", availableChips(), bet))

        val afterBet = this.copy(betAmountThisPhase = betAmountThisPhase + bet)
        return afterBet.copy(
                allIn = afterBet.availableChips() == 0
        )
    }

    fun roundOver(): Player {
        return this.copy(
                betAmount = betAmount + betAmountThisPhase,
                betAmountThisPhase = 0,
                chipsAmount = chipsAmount - betAmountThisPhase
        )
    }

    fun bettingTurnOver(): Player {
        return this.copy(
                minimumBet = 0,
                maximumBet = 0,
                possibleMoves = List.empty()
        )
    }

    fun prepareForNewGame(startingChips: Int): Player {
        return this.copy(
                chipsAmount = startingChips,
                inGame = true
        )
    }

    fun prepareForNewRound(): Player {
        return this.copy(
                betAmount = 0,
                betAmountThisPhase = 0,
                possibleMoves = List.empty(),
                maximumBet = 0,
                minimumBet = 0,
                folded = false,
                allIn = false,
                hand = List.empty()
        )
    }

    fun prepareForNewPhase(): Player {
        return this.copy(
                betAmount = betAmount + betAmountThisPhase,
                betAmountThisPhase = 0,
                chipsAmount = chipsAmount - betAmountThisPhase,
                minimumBet = 0,
                maximumBet = 0,
                possibleMoves = List.empty()
        )
    }

    fun gameOver(): Player {
        return this.copy(
                minimumBet = 0,
                maximumBet = 0,
                inGame = false,
                possibleMoves = List.empty()
        )
    }

    fun betRanges(min: Int, max: Int): Player {
        return this.copy(
                minimumBet = min,
                maximumBet = max
        )
    }

    fun withMoves(moves: List<Moves>): Player {
        return this.copy(
                possibleMoves = moves
        )
    }
}
