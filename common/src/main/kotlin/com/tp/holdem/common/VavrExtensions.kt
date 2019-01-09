package com.tp.holdem.common

import io.vavr.collection.List
import io.vavr.kotlin.toVavrList

fun <T> List<T>.chunks(size: Int): List<List<T>> {
    return this.chunked(size).toVavrList().map { it.toVavrList() }
}

fun <T> List<T>.cycleGet(index: Int): T {
    return this.get(index % size())
}