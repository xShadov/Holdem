package com.tp.holdem.logic.utils

import com.tp.holdem.logic.hands.HandComparators
import com.tp.holdem.logic.model.Card
import io.vavr.collection.List as VavrList

private val COMBO_NOT_FOUND = { IllegalStateException("Card combination could not be found") }

fun VavrList<Card>.overallValue(): Int {
    return map(Card::value).sum().toInt()
}

fun VavrList<Card>.extractHighestWithSameHonour(number: Int): VavrList<Card> {
    return combinations(number)
            .filter(VavrList<Card>::sameHonour)
            .maxBy(HandComparators.highestOverallValue)
            .getOrElseThrow(COMBO_NOT_FOUND)
}

private fun VavrList<Card>.sameHonour(): Boolean {
    return isEmpty || distinctBy(Card::honour).size() == 1
}

