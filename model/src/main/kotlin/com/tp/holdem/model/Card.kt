package com.tp.holdem.model

import com.tp.holdem.common.model.Honour
import com.tp.holdem.common.model.Suit


data class Card(
        val suit: Suit,
        val honour: Honour,
        val value: Int = honour.value()
) {
    companion object {
        @JvmStatic
        fun from(suit: Suit, honour: Honour): Card {
            return Card(suit, honour)
        }

        @JvmStatic
        fun coded(code: String): Card {
            return from(Suit.coded(code[code.length - 1]), Honour.coded(code.substring(0, code.length - 1)))
        }
    }
}