package com.tp.holdem.logic.hands

import com.tp.holdem.model.Card
import io.vavr.collection.List
import java.util.*

internal object HandComparators {
    private val COMBO_NOT_FOUND = { IllegalStateException("Card combination could not be found") }

    private fun extractHighestPair(cards: List<Card>): List<Card> {
        return cards.combinations(2)
                .filter { HandFinder.isPair(it) }
                .maxBy(highestOverallValue())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun extractHighestThree(cards: List<Card>): List<Card> {
        return cards.combinations(3)
                .filter { HandFinder.isThreeOfAKind(it) }
                .maxBy(highestOverallValue())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    private fun extractHighestFour(cards: List<Card>): List<Card> {
        return cards.combinations(4)
                .filter { HandFinder.isFourOfAKind(it) }
                .maxBy(highestOverallValue())
                .getOrElseThrow(COMBO_NOT_FOUND)
    }

    fun highestOverallValue(): Comparator<List<Card>> {
        return Comparator.comparingInt { cards -> cards.map { it.value }.sum().toInt() }
    }

    fun highestFullHouse(): Comparator<in List<Card>> {
        return Comparator { a, b ->
            val aFirstThree = extractHighestThree(a)
            val bFirstThree = extractHighestThree(b)

            val compareFirstThrees = highestOverallValue().compare(aFirstThree, bFirstThree)
            if (compareFirstThrees != 0)
                return@Comparator compareFirstThrees

            val aTrimmed = a.removeAll(aFirstThree)
            val bTrimmed = b.removeAll(bFirstThree)

            return@Comparator highestPair().compare(aTrimmed, bTrimmed)
        }
    }

    fun highestFour(): Comparator<in List<Card>> {
        return Comparator { a, b ->
            val aFirstFour = extractHighestFour(a)
            val bFirstFour = extractHighestFour(b)

            val compareFirstFours = highestOverallValue().compare(aFirstFour, bFirstFour)
            if (compareFirstFours != 0)
                return@Comparator compareFirstFours

            val aTrimmed = a.removeAll(aFirstFour)
            val bTrimmed = b.removeAll(bFirstFour)

            return@Comparator highestKicker().compare(aTrimmed, bTrimmed)
        }
    }

    fun highestThree(): Comparator<in List<Card>> {
        return Comparator { a, b ->
            val aFirstThree = extractHighestThree(a)
            val bFirstThree = extractHighestThree(b)

            val compareFirstThrees = highestOverallValue().compare(aFirstThree, bFirstThree)
            if (compareFirstThrees != 0)
                return@Comparator compareFirstThrees

            val aTrimmed = a.removeAll(aFirstThree)
            val bTrimmed = b.removeAll(bFirstThree)

            return@Comparator highestKicker().compare(aTrimmed, bTrimmed)
        }
    }

    fun highestPair(): Comparator<in List<Card>> {
        return Comparator { a, b ->
            val aFirstPair = extractHighestPair(a)
            val bFirstPair = extractHighestPair(b)

            val compareFirstPairs = highestOverallValue().compare(aFirstPair, bFirstPair)
            if (compareFirstPairs != 0)
                return@Comparator compareFirstPairs

            val aTrimmed = a.removeAll(aFirstPair)
            val bTrimmed = b.removeAll(bFirstPair)

            return@Comparator highestKicker().compare(aTrimmed, bTrimmed)
        }
    }

    fun highestPairs(): Comparator<List<Card>> {
        return Comparator { a, b ->
            val aFirstPair = extractHighestPair(a)
            val bFirstPair = extractHighestPair(b)

            val compareFirstPairs = highestOverallValue().compare(aFirstPair, bFirstPair)
            if (compareFirstPairs != 0)
                return@Comparator compareFirstPairs

            val aTrimmed = a.removeAll(aFirstPair)
            val bTrimmed = b.removeAll(bFirstPair)

            val aSecondPair = extractHighestPair(aTrimmed)
            val bSecondPair = extractHighestPair(bTrimmed)

            val compareSecondPairs = highestOverallValue().compare(aSecondPair, bSecondPair)
            if (compareSecondPairs != 0)
                return@Comparator compareSecondPairs

            val aHighestCard = aTrimmed.removeAll(aSecondPair)
            val bHighestCard = bTrimmed.removeAll(bSecondPair)

            return@Comparator highestOverallValue().compare(aHighestCard, bHighestCard)
        }
    }

    fun highestKicker(): Comparator<List<Card>> {
        return Comparator { a, b ->
            for (i in a.length() - 1 downTo 0) {
                val firstValue = a.get(i).value
                val secondValue = b.get(i).value

                val compare = Integer.compare(firstValue, secondValue)

                if (compare != 0)
                    return@Comparator compare
            }

            return@Comparator 0
        }
    }

    fun highestStraightKicker(): Comparator<List<Card>> {
        return Comparator { a, b ->
            val aAceStraight = HandPredicates.ACE_STRAIGHT.test(a)
            val bAceStraight = HandPredicates.ACE_STRAIGHT.test(b)

            if (aAceStraight && bAceStraight)
                return@Comparator 0

            if (aAceStraight)
                return@Comparator -1

            if (bAceStraight)
                return@Comparator 1

            return@Comparator highestKicker().compare(a, b)
        }
    }
}
