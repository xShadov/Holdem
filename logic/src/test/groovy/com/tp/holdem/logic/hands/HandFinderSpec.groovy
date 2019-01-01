package com.tp.holdem.logic.hands

import com.tp.holdem.common.model.Honour
import com.tp.holdem.common.model.Suit
import com.tp.holdem.model.Card
import com.tp.holdem.model.Hands
import io.vavr.collection.List
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class HandFinderSpec extends Specification {
    def "test different (correct) hand combinations"() {
        expect:
        HandCheckersKt.findHand(List.ofAll(cards).map({ code -> Card.coded(code) })) == hand

        where:
        cards                                         || hand
        ['10D', '10H', 'JS', 'JD', 'QD', 'KD', 'AD']   || Hands.ROYAL_FLUSH
        ['2C', '2D', '10D', 'JD', 'QD', 'KD', 'AD']   || Hands.ROYAL_FLUSH
        ['AD', '10D', '10C', 'JD', 'QD', 'KD', '9D']  || Hands.ROYAL_FLUSH

        ['2C', '3C', '4C', '5C', '6C', 'KS', 'AC']    || Hands.STRAIGHT_FLUSH
        ['2C', '3D', '4C', '5C', '6C', '7C', '8C']    || Hands.STRAIGHT_FLUSH

        ['2C', '2D', '2H', '3D', '3H', '3S', '3D']    || Hands.FOUR_OF_A_KIND
        ['2C', '3D', '4H', '4D', '4S', '4C', '5D']    || Hands.FOUR_OF_A_KIND

        ['2C', '2D', '2H', '3D', '3H', '3S', '4D']    || Hands.FULL_HOUSE
        ['2C', '2D', '3C', '3D', '3H', '4S', '4D']    || Hands.FULL_HOUSE
        ['2C', '2D', '2H', '3D', '4H', '4S', '4D']    || Hands.FULL_HOUSE
        ['10C', '10D', '10H', 'AD', 'AH', 'AS', 'KD'] || Hands.FULL_HOUSE

        ['2C', '3C', '7C', '8D', '8H', 'JC', 'AC']    || Hands.FLUSH
        ['2C', '3C', '7C', '8C', '10C', 'JC', 'AC']   || Hands.FLUSH
        ['2C', '3C', '7D', '8D', '10D', 'JD', 'AD']   || Hands.FLUSH
        ['2C', '3D', '4D', '5H', '6D', '9D', 'JD']    || Hands.FLUSH

        ['2C', '3D', '4H', '5D', '10H', '10S', 'AD']  || Hands.STRAIGHT
        ['2C', '3D', '4H', '5D', '6H', '7S', '8D']    || Hands.STRAIGHT
        ['2C', '3D', '5H', '6D', '7H', '8S', '9D']    || Hands.STRAIGHT
        ['2C', '8D', '9H', '10D', 'JH', 'QS', 'AD']   || Hands.STRAIGHT
        ['2C', '2D', '4H', '5D', '6H', '7S', '8D']    || Hands.STRAIGHT

        ['2C', '3D', '4H', '5D', '5H', '5S', '8D']    || Hands.THREE_OF_A_KIND
        ['2C', '2D', '2H', '5D', '6H', '7S', '8D']    || Hands.THREE_OF_A_KIND
        ['2C', '3D', '4H', '4D', '4S', '7S', '8D']    || Hands.THREE_OF_A_KIND

        ['2C', '2D', '3H', '3D', '6H', '7S', '8D']    || Hands.TWO_PAIR
        ['2C', '2D', '3H', '3D', '6H', '6S', '8D']    || Hands.TWO_PAIR

        ['2C', '2D', '4H', '7D', '8H', '10S', 'JD']    || Hands.PAIR
        ['2C', '3D', '4H', '8D', '10H', 'JS', 'JD']   || Hands.PAIR

        ['2C', '7D', '8H', 'JD', 'QH', 'KS', 'AD']    || Hands.HIGH_CARD
    }

    def "test with incorrect hand combinations"() {
        when:
        HandCheckersKt.findHand(List.ofAll(cards))

        then:
        thrown IllegalArgumentException

        where:
        cards << [[], [Card.from(Suit.CLUB, Honour.ACE)] * 6, [Card.from(Suit.CLUB, Honour.ACE)] * 8]
    }
}
