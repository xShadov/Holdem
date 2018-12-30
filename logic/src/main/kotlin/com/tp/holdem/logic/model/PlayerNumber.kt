package com.tp.holdem.logic.model

data class PlayerNumber(val number: Int = -1) {
    companion object {

        @JvmStatic
        fun empty(): PlayerNumber {
            return PlayerNumber(-1)
        }

        @JvmStatic
        fun of(number: Int): PlayerNumber {
            return PlayerNumber(number)
        }
    }

    fun exists(): Boolean {
        return number != -1
    }
}
