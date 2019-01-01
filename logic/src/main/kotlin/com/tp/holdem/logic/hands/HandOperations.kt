package com.tp.holdem.logic.hands

import com.tp.holdem.common.lazyLogger
import com.tp.holdem.logic.players.handRank
import com.tp.holdem.logic.players.notFolded
import com.tp.holdem.logic.players.playing
import com.tp.holdem.model.Player
import com.tp.holdem.model.PokerTable
import io.vavr.collection.List
import io.vavr.control.Either

internal object HandOperations {
    private val log by lazyLogger()

    fun findWinner(allPlayers: List<Player>, pokerTable: PokerTable): Either<Player, List<Player>> {
        val notFoldedPlayers = allPlayers.notFolded()

        if (notFoldedPlayers.size() == 1) {
            return Either.left<Player, List<Player>>(notFoldedPlayers.single())
                    .also { log.debug("Everyone folded except one player, he's the winner") }
        }

        val hands = allPlayers.playing()
                .toMap({ it }, { player -> player.handRank(pokerTable) })
                .also { log.debug("Players to hands map: $it") }

        val maxHandRank = hands.values()
                .maxBy(HandRankComparator)
                .single()

        val playersWithMaxHandRank = hands
                .filter { _, hand -> HandRankComparator.compare(hand, maxHandRank) == 0 }
                .map { tuple -> tuple._1 }
                .toList()

        return when (playersWithMaxHandRank.size()) {
            1 -> Either.left(playersWithMaxHandRank.head())
            else -> Either.right(playersWithMaxHandRank)
        }
    }
}
