package com.tp.holdem.logic.hands

import com.tp.holdem.logic.utils.extractHighestWithSameHonour
import com.tp.holdem.logic.utils.overallValue
import com.tp.holdem.logic.model.Card
import io.vavr.Tuple
import io.vavr.collection.List
import io.vavr.kotlin.component1
import io.vavr.kotlin.component2
import java.util.*

internal object HandComparators {
    val highestOverallValue: Comparator<List<Card>> = compareBy(List<Card>::overallValue)

    val highestFullHouse: Comparator<in List<Card>> = Comparator { a, b ->
        val aFirstThree = a.extractHighestWithSameHonour(3)
        val bFirstThree = b.extractHighestWithSameHonour(3)

        val compare = highestOverallValue.compare(aFirstThree, bFirstThree)
        return@Comparator when (compare) {
            0 -> highestPair.compare(a.removeAll(aFirstThree), b.removeAll(bFirstThree))
            else -> compare
        }
    }

    val highestFour: Comparator<in List<Card>> = Comparator { a, b ->
        val aFirstFour = a.extractHighestWithSameHonour(4)
        val bFirstFour = b.extractHighestWithSameHonour(4)

        val compare = highestOverallValue.compare(aFirstFour, bFirstFour)
        return@Comparator when (compare) {
            0 -> highestKicker.compare(a.removeAll(aFirstFour), b.removeAll(bFirstFour))
            else -> compare
        }
    }

    val highestThree: Comparator<in List<Card>> = Comparator { a, b ->
        val aFirstThree = a.extractHighestWithSameHonour(3)
        val bFirstThree = b.extractHighestWithSameHonour(3)

        val compare = highestOverallValue.compare(aFirstThree, bFirstThree)
        return@Comparator when (compare) {
            0 -> highestKicker.compare(a.removeAll(aFirstThree), b.removeAll(bFirstThree))
            else -> compare
        }
    }

    val highestPair: Comparator<in List<Card>> = Comparator { a, b ->
        val aFirstPair = a.extractHighestWithSameHonour(2)
        val bFirstPair = b.extractHighestWithSameHonour(2)

        val compare = highestOverallValue.compare(aFirstPair, bFirstPair)
        return@Comparator when (compare) {
            0 -> highestKicker.compare(a.removeAll(aFirstPair), b.removeAll(bFirstPair))
            else -> compare
        }
    }

    val highestPairs: Comparator<List<Card>> = Comparator { a, b ->
        val aFirstPair = a.extractHighestWithSameHonour(2)
        val bFirstPair = b.extractHighestWithSameHonour(2)

        val compareFirstPairs = highestOverallValue.compare(aFirstPair, bFirstPair)
        if (compareFirstPairs != 0)
            return@Comparator compareFirstPairs

        val aTrimmed = a.removeAll(aFirstPair)
        val bTrimmed = b.removeAll(bFirstPair)

        val aSecondPair = aTrimmed.extractHighestWithSameHonour(2)
        val bSecondPair = bTrimmed.extractHighestWithSameHonour(2)

        val compare = highestOverallValue.compare(aSecondPair, bSecondPair)
        return@Comparator when (compare) {
            0 -> highestOverallValue.compare(aTrimmed.removeAll(aSecondPair), bTrimmed.removeAll(bSecondPair))
            else -> compare
        }
    }

    val highestKicker: Comparator<List<Card>> = Comparator { a, b ->
        return@Comparator a.zip(b)
                .toStream()
                .reverse()
                .map { (card1, card2) -> Tuple.of(card1.value, card2.value) }
                .map { (first, second) -> Integer.compare(first, second) }
                .find { value -> value != 0 }
                .getOrElse(0)
    }

    val highestStraightKicker: Comparator<List<Card>> = Comparator { a, b ->
        when (listOf(a.isAceStraight(), b.isAceStraight())) {
            listOf(true, true) -> 0
            listOf(true, false) -> -1
            listOf(false, true) -> 1
            else -> highestKicker.compare(a, b)
        }
    }
}
