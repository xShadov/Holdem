package com.tp.holdem.common

import io.vavr.kotlin.toVavrList
import io.vavr.collection.List as VavrList

fun <T> VavrList<T>.chunks(size: Int): VavrList<VavrList<T>> {
    return this.chunked(size).toVavrList().map { it.toVavrList() }
}

fun <T> VavrList<T>.cycleGet(index: Int): T {
    return this.get(index % size())
}