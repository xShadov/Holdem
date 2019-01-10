package com.tp.holdem.logic.table

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import io.vavr.collection.HashSet
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class TableWinnerFindingSpec extends Specification {
    @Shared
    Player.Companion player = Player.@Companion

    def "finding single winner between two players"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20)
                .players(player.playing(0).hand(*oneCards), player.playing(1).hand(*twoCards))
                .cardsOnTable(*tableCards)

        def afterRound = table.roundOver()

        afterRound.winnerPlayerNumbers.toSet() == HashSet.of(new PlayerNumber(winner))

        where:
        oneCards     | twoCards     | tableCards                     || winner
        ['AC', 'AD'] | ['3D', '3C'] | ['4C', '4D', '4H', '5C', '5D'] || 0
        ['KC', 'KD'] | ['AD', 'AC'] | ['4C', '4D', '4H', '5C', '5D'] || 1
        ['5C', '5D'] | ['6D', '6C'] | ['5H', '5S', '8H', 'JC', 'QD'] || 0
    }

    def "two players have a tie"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20)
                .players(player.playing(0).hand(*oneCards), player.playing(1).hand(*twoCards))
                .cardsOnTable(*tableCards)

        def afterRound = table.roundOver()

        afterRound.winnerPlayerNumbers.toSet() == HashSet.of(new PlayerNumber(0), new PlayerNumber(1))

        where:
        oneCards      | twoCards      | tableCards
        ['2C', '2D']  | ['3D', '3C']  | ['4C', '4D', '4H', '5C', '5D']
        ['AH', 'AS']  | ['AD', 'AC']  | ['4C', '4D', '4H', '5C', '5D']
        ['9C', '10D'] | ['9H', '10D'] | ['5H', '5S', '8H', 'JC', 'QD']
        ['2H', '2D']  | ['2S', '2C']  | ['3H', '6H', '8H', 'JC', 'JD']
    }

    def "finding single winner between three players"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20)
                .players(player.playing(0).hand(*oneCards), player.playing(1).hand(*twoCards), player.playing(1).hand(*threeCards))
                .cardsOnTable(*tableCards)

        def afterRound = table.roundOver()

        afterRound.winnerPlayerNumbers.toSet() == HashSet.of(new PlayerNumber(winner))

        where:
        oneCards     | twoCards     | threeCards   | tableCards                     || winner
        ['AC', 'AD'] | ['3D', '3C'] | ['6D', '6C'] | ['4C', '4D', '4H', '5C', '5D'] || 0
        ['KC', 'KD'] | ['AD', 'AC'] | ['9D', '7H'] | ['4C', '4D', '4H', '5C', '5D'] || 1
        ['5C', '5D'] | ['6D', '6C'] | ['2D', 'JC'] | ['5H', '5S', '8H', 'JC', 'QD'] || 0
    }

    def "two out of three players have a tie"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20)
                .players(player.playing(0).hand(*oneCards), player.playing(1).hand(*twoCards), player.playing(2).hand(*threeCards))
                .cardsOnTable(*tableCards)

        def afterRound = table.roundOver()

        afterRound.winnerPlayerNumbers.toSet() == HashSet.ofAll(winners.collect { new PlayerNumber(it) })

        where:
        oneCards     | twoCards     | threeCards   | tableCards                      || winners
        ['AC', 'AD'] | ['AH', 'AS'] | ['6D', '6C'] | ['4C', '4D', '4H', '5C', '5D']  || [0, 1]
        ['7C', '7D'] | ['KD', 'KC'] | ['KH', 'KS'] | ['4C', '4D', '4H', '5C', '5D']  || [1, 2]
        ['6D', '8C'] | ['5C', '5D'] | ['8D', 'JC'] | ['5H', '9S', '10H', 'JC', 'QD'] || [0, 2]
    }

    def "all three players have a tie"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20)
                .players(player.playing(0).hand(*oneCards), player.playing(1).hand(*twoCards), player.playing(2).hand(*threeCards))
                .cardsOnTable(*tableCards)

        def afterRound = table.roundOver()

        afterRound.winnerPlayerNumbers.toSet() == HashSet.of(new PlayerNumber(0), new PlayerNumber(1), new PlayerNumber(2))

        where:
        oneCards     | twoCards     | threeCards   | tableCards
        ['2C', '2D'] | ['3H', '3S'] | ['4D', '4C'] | ['7C', '7D', '7H', '8C', '8D']
        ['7C', '7D'] | ['KD', 'KC'] | ['KH', 'KS'] | ['6C', '7D', '8H', '9C', '10D']
        ['9H', 'AC'] | ['2C', '9D'] | ['8D', '9C'] | ['6S', '7S', '8H', '10C', 'QD']
    }
}
