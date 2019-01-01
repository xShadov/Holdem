package com.tp.holdem.logic.utils

import com.tp.holdem.logic.hands.HandComparators
import com.tp.holdem.model.Card
import io.vavr.collection.List

private val COMBO_NOT_FOUND = { IllegalStateException("Card combination could not be found") }

fun List<Card>.overallValue(): Int {
    return map(Card::value).sum().toInt()
}

fun List<Card>.extractHighest(number: Int) : List<Card> {
    return combinations(number)
            .filter(List<Card>::sameHonour)
            .maxBy(HandComparators.highestOverallValue)
            .getOrElseThrow(COMBO_NOT_FOUND)
}

fun List<Card>.sameHonour() : Boolean {
    return distinctBy(Card::honour).size() == 1
}

