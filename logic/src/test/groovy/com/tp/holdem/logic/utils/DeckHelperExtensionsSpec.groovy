package com.tp.holdem.logic.utils

import com.tp.holdem.logic.model.Card
import com.tp.holdem.logic.model.Deck
import com.tp.holdem.logic.model.Player
import io.vavr.Tuple2
import io.vavr.collection.List as VavrList
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DeckHelperExtensionsSpec extends Specification {
    @Shared
    Player.Companion player = Player.@Companion

    def "drawing a single card"() {
        given:
        final deck = Deck.brandNew()

        when:
        final Tuple2<Deck, Card> tuple = deck.drawCard()

        then:
        tuple._1.cards.size() == 51
        tuple._2 != null
    }

    def "drawing cards"() {
        given:
        final deck = Deck.brandNew()

        expect:
        final Tuple2<Deck, VavrList<Card>> tuple = deck.drawCards(count)
        tuple._1.cards.size() == 52 - count
        tuple._2.size() == count

        where:
        count << [1, 2, 4, 52]
    }

    def "drawing incorrect amounts of cards"() {
        given:
        final deck = Deck.brandNew()

        when:
        deck.drawCards(count)

        then:
        thrown Exception

        where:
        count << [-1, 0, 55]
    }

    def "dealing cards to players"() {
        given:
        final deck = Deck.brandNew()

        when:
        final Tuple2<Deck, VavrList<Player>> tuple = deck.dealCards(count, VavrList.ofAll(players))

        then:
        tuple._1().cards.size() == 52 - (playing * count)
        tuple._2().size() == players.size()
        tuple._2().playing().size() == playing
        tuple._2().notPlaying().size() == players.size() - playing
        tuple._2().playing().forAll { player -> player.hand.size() == count }
        tuple._2().notPlaying().forAll { player -> player.hand.size() == 0 }

        where:
        count | playing | players
        2     | 1       | [player.playing(0), player.folded(1), player.sitting(2)]
        2     | 3       | [player.playing(0), player.playing(1), player.playing(2)]
        16    | 3       | [player.playing(0), player.playing(1), player.playing(2)]
    }

    def "dealing cards to players with incorrect parameters"() {
        given:
        final deck = Deck.brandNew()

        when:
        deck.dealCards(count, VavrList.ofAll(players))

        then:
        thrown Exception

        where:
        count | players
        2     | []
        0     | [player.playing(0), player.folded(1)]
        -1    | [player.playing(0), player.playing(1)]
        27    | [player.playing(0), player.playing(1)]
    }
}