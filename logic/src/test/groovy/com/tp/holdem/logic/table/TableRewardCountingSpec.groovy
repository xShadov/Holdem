package com.tp.holdem.logic.table

import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import io.vavr.collection.HashSet
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class TableRewardCountingSpec extends Specification {
    @Shared
    Player.Companion player = Player.@Companion

    def "finding single winner between two players"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20).players(
                player.playing(0).chips(1200).generalBet(300).betThisPhase(100).hand(*oneCards),
                player.playing(1).chips(1200).generalBet(300).betThisPhase(100).hand(*twoCards)
        ).cardsOnTable(*tableCards)

        def afterRound = table.roundOver()
        chips.forEach { player, chips -> assert afterRound.findPlayer(player).chipsAmount == chips }

        where:
        oneCards     | twoCards     | tableCards                     || chips
        ['AC', 'AD'] | ['3D', '3C'] | ['4C', '4D', '4H', '5C', '5D'] || [0: 1500 + 400, 1: 1500 - 400]
        ['KC', 'KD'] | ['AD', 'AC'] | ['4C', '4D', '4H', '5C', '5D'] || [0: 1500 - 400, 1: 1500 + 400]
        ['5C', '5D'] | ['6D', '6C'] | ['5H', '5S', '8H', 'JC', 'QD'] || [0: 1500 + 400, 1: 1500 - 400]
    }

    def "two players have a tie"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20).players(
                player.playing(0).chips(1200).generalBet(300).betThisPhase(100).hand(*oneCards),
                player.playing(1).chips(1200).generalBet(300).betThisPhase(100).hand(*twoCards)
        ).cardsOnTable(*tableCards)

        def afterRound = table.roundOver()
        afterRound.allPlayers.forEach { player -> assert player.chipsAmount == 1500 }

        where:
        oneCards      | twoCards      | tableCards
        ['2C', '2D']  | ['3D', '3C']  | ['4C', '4D', '4H', '5C', '5D']
        ['AH', 'AS']  | ['AD', 'AC']  | ['4C', '4D', '4H', '5C', '5D']
        ['9C', '10D'] | ['9H', '10D'] | ['5H', '5S', '8H', 'JC', 'QD']
        ['2H', '2D']  | ['2S', '2C']  | ['3H', '6H', '8H', 'JC', 'JD']
    }

    def "finding single winner between three players"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20).players(
                player.playing(0).chips(1200).generalBet(300).betThisPhase(100).hand(*oneCards),
                player.playing(1).chips(1200).generalBet(300).betThisPhase(100).hand(*twoCards),
                player.playing(2).chips(1200).generalBet(300).betThisPhase(100).hand(*threeCards)
        ).cardsOnTable(*tableCards)

        def afterRound = table.roundOver()
        chips.forEach { player, chips -> assert afterRound.findPlayer(player).chipsAmount == chips }

        where:
        oneCards     | twoCards     | threeCards   | tableCards                     || chips
        ['AC', 'AD'] | ['3D', '3C'] | ['6D', '6C'] | ['4C', '4D', '4H', '5C', '5D'] || [0: 1500 + 800, 1: 1500 - 400, 2: 1500 - 400]
        ['KC', 'KD'] | ['AD', 'AC'] | ['9D', '7H'] | ['4C', '4D', '4H', '5C', '5D'] || [0: 1500 - 400, 1: 1500 + 800, 2: 1500 - 400]
        ['5C', '5D'] | ['6D', '6C'] | ['2D', 'JC'] | ['5H', '5S', '8H', 'JC', 'QD'] || [0: 1500 + 800, 1: 1500 - 400, 2: 1500 - 400]
    }

    def "two out of three players have a tie"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20).players(
                player.playing(0).chips(1200).generalBet(300).betThisPhase(100).hand(*oneCards),
                player.playing(1).chips(1200).generalBet(300).betThisPhase(100).hand(*twoCards),
                player.playing(2).chips(1200).generalBet(300).betThisPhase(100).hand(*threeCards)
        ).cardsOnTable(*tableCards)

        def afterRound = table.roundOver()
        chips.forEach { player, chips -> assert afterRound.findPlayer(player).chipsAmount == chips }

        where:
        oneCards     | twoCards     | threeCards   | tableCards                      || chips
        ['AC', 'AD'] | ['AH', 'AS'] | ['6D', '6C'] | ['4C', '4D', '4H', '5C', '5D']  || [0: 1700, 1: 1700, 2: 1100]
        ['7C', '7D'] | ['KD', 'KC'] | ['KH', 'KS'] | ['4C', '4D', '4H', '5C', '5D']  || [0: 1100, 1: 1700, 2: 1700]
        ['6D', '8C'] | ['5C', '5D'] | ['8D', 'JC'] | ['5H', '9S', '10H', 'JC', 'QD'] || [0: 1700, 1: 1100, 2: 1700]
    }

    def "all three players have a tie"() {
        expect:
        final PokerTable table = PokerTable.withBlinds(40, 20).players(
                player.playing(0).chips(1200).generalBet(300).betThisPhase(100).hand(*oneCards),
                player.playing(1).chips(1200).generalBet(300).betThisPhase(100).hand(*twoCards),
                player.playing(2).chips(1200).generalBet(300).betThisPhase(100).hand(*threeCards)
        ).cardsOnTable(*tableCards)

        def afterRound = table.roundOver()
        afterRound.allPlayers.forEach { player -> assert player.chipsAmount == 1500 }

        where:
        oneCards     | twoCards     | threeCards   | tableCards
        ['2C', '2D'] | ['3H', '3S'] | ['4D', '4C'] | ['7C', '7D', '7H', '8C', '8D']
        ['7C', '7D'] | ['KD', 'KC'] | ['KH', 'KS'] | ['6C', '7D', '8H', '9C', '10D']
        ['9H', 'AC'] | ['2C', '9D'] | ['8D', '9C'] | ['6S', '7S', '8H', '10C', 'QD']
    }
}
