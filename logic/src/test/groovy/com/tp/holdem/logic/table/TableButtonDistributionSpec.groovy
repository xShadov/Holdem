package com.tp.holdem.logic.table

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import io.vavr.collection.List as VavrList
import spock.lang.Shared
import spock.lang.Specification

class TableButtonDistributionSpec extends Specification {
    @Shared
    Player.Companion player = Player.@Companion

    def "buttons are distributed according to number of current hand"() {
        expect:
        def count = 0
        def playerVavrList = VavrList.fill(players, { -> player.playing(count++) })

        def table = PokerTable.withBlinds(40, 20).players(playerVavrList.toJavaArray(Player))

        def newRound = table.newRound(handCount)

        newRound.smallBlindPlayerNumber.number == smallBlind
        newRound.bigBlindPlayerNumber.number == bigBlind
        newRound.dealerPlayerNumber.number == dealer

        where:
        handCount | players || dealer | smallBlind | bigBlind
        0         | 2       || 1          | 1      | 0
        1         | 2       || 0          | 0      | 1
        2         | 2       || 1          | 1      | 0
        3         | 2       || 0          | 0      | 1

        0         | 3       || 0          | 1      | 2
        1         | 3       || 1          | 2      | 0
        2         | 3       || 2          | 0      | 1
        3         | 3       || 0          | 1      | 2
        4         | 3       || 1          | 2      | 0

    }
}
