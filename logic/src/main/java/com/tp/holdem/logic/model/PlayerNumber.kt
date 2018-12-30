package com.tp.holdem.logic.model

data class PlayerNumber(val number: Int? = null) {
    companion object {

        @JvmStatic
        fun empty(): PlayerNumber {
            return PlayerNumber(null)
        }

        @JvmStatic
        fun of(number: Int): PlayerNumber {
            return PlayerNumber(number)
        }
    }

    fun exists(): Boolean {
        return number != null
    }
}
