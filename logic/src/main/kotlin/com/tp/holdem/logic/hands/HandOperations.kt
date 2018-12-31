package com.tp.holdem.logic.hands

import com.tp.holdem.common.lazyLogger
import com.tp.holdem.logic.extensions.playing
import com.tp.holdem.model.Card
import com.tp.holdem.model.HandRank
import com.tp.holdem.model.Player
import com.tp.holdem.model.PokerTable
import io.vavr.collection.List
import io.vavr.control.Either
import java.util.*
import java.util.function.Function

internal object HandOperations {
    private val log by lazyLogger()

    private val CARD_COMPARATOR = Comparator.comparing { card: Card -> card.value }

    fun findWinner(allPlayers: List<Player>, pokerTable: PokerTable): Either<Player, List<Player>> {
        if (allPlayers.filter { player -> !player.folded }.size() == 1) {
            log.debug("Everyone folded except one player, he's the winner")
            return Either.left(allPlayers.find { player -> !player.folded }.single())
        }

        val hands = allPlayers.filter { it.playing() }
                .toMap<Player, HandRank>(Function.identity(), Function { player -> findHandRank(player, pokerTable) })

        log.debug(String.format("Players to hands map: %s", hands))

        val maxHandRank = hands.values()
                .maxBy(HandRankComparator.INSTANCE)
                .single()

        val playersWithMaxHandRank = hands
                .filter { player, hand -> HandRankComparator.INSTANCE.compare(hand, maxHandRank) == 0 }
                .map { tuple -> tuple._1 }

        return if (playersWithMaxHandRank.size() == 1) Either.left(playersWithMaxHandRank.head()) else Either.right(playersWithMaxHandRank.toList())
    }

    private fun findHandRank(player: Player, pokerTable: PokerTable): HandRank {
        val cards = player.hand
                .appendAll(pokerTable.cardsOnTable)
                .sorted(CARD_COMPARATOR)

        val playerHand = HandFinder.findHand(cards)
        val bestCardsThatMakeHand = BestCardCombinationFinder.findBestCardsForHand(cards, playerHand)

        return HandRank(playerHand, bestCardsThatMakeHand)
    }
}
