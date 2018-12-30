package com.tp.holdem.logic.model

import com.tp.holdem.model.common.Honour
import com.tp.holdem.model.common.Suit
import com.tp.holdem.model.message.dto.CardDTO
import lombok.Builder
import lombok.Value


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
            return Card.from(Suit.coded(code[code.length - 1]), Honour.coded(code.substring(0, code.length - 1)))
        }
    }

    fun toDTO(): CardDTO {
        return CardDTO.builder()
                .honour(honour)
                .suit(suit)
                .build()
    }
}