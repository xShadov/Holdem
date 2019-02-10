package com.tp.holdem.logic.hands

import com.tp.holdem.common.model.Honour
import com.tp.holdem.common.model.Suit
import com.tp.holdem.logic.model.Card
import com.tp.holdem.logic.model.Hands
import io.vavr.collection.List as VavrList
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class HandCombinationsSpec extends Specification {
    def mapCards = { cards ->
        return VavrList.ofAll(cards).map({ code -> Card.coded(code) })
    }

    def "test different (correct) hand combinations"() {
        expect:
        mapCards(cards).bestCardsForHand(hand).toSet() == mapCards(result).toSet()

        where:
        hand                  | cards                                         | result
        Hands.HIGH_CARD       | ['3D', '5H', '7S', '10D', 'QD', 'KD', 'AD']   | ['7S', '10D', 'QD', 'KD', 'AD']
        Hands.HIGH_CARD       | ['2C', '2S', '5D', 'JH', 'QD', 'KS', 'AD']    | ['5D', 'JH', 'QD', 'KS', 'AD']
        Hands.HIGH_CARD       | ['2D', '3C', '6D', '7D', '10D', 'JS', 'AH']   | ['6D', '7D', '10D', 'JS', 'AH']

        Hands.PAIR            | ['4D', '4C', '7D', '10S', 'QD', 'KS', 'AH']   | ['4C', '4D', 'QD', 'KS', 'AH']
        Hands.PAIR            | ['4D', '6C', '7D', '10S', 'QD', 'AS', 'AH']   | ['7D', '10S', 'QD', 'AS', 'AH']

        Hands.TWO_PAIR        | ['10D', '10C', '9D', '9S', 'QD', 'QS', 'AC']  | ['AC', 'QD', 'QS', '10D', '10C']
        Hands.TWO_PAIR        | ['8D', '10C', '9D', 'QS', 'QD', 'AS', 'AC']   | ['10C', 'QS', 'QD', 'AS', 'AC']

        Hands.THREE_OF_A_KIND | ['10D', '10C', '10H', '8C', 'QD', 'KH', 'AC'] | ['10D', '10C', '10H', 'KH', 'AC']
        Hands.THREE_OF_A_KIND | ['3D', '6C', '8H', 'JC', 'AD', 'AH', 'AC']    | ['8H', 'JC', 'AD', 'AH', 'AC']

        Hands.STRAIGHT        | ['AD', '2C', '3D', '4S', '5D', '6S', 'QC']    | ['2C', '3D', '4S', '5D', '6S']
        Hands.STRAIGHT        | ['7D', '8C', '9D', '10S', 'JD', 'QS', 'KC']   | ['9D', '10S', 'JD', 'QS', 'KC']
        Hands.STRAIGHT        | ['7D', '8C', '9D', '10S', 'JD', 'QS', 'AC']   | ['8C', '9D', '10S', 'JD', 'QS']
        Hands.STRAIGHT        | ['7D', '8C', '9D', '10S', 'JD', 'KS', 'AC']   | ['7D', '8C', '9D', '10S', 'JD']

        Hands.FLUSH           | ['2D', '3D', '6D', '7D', '10D', 'JD', 'AD']   | ['6D', '7D', '10D', 'JD', 'AD']
        Hands.FLUSH           | ['2D', '2C', '3C', '5C', '7C', '8C', 'AD']    | ['2C', '3C', '5C', '7C', '8C']

        Hands.FOUR_OF_A_KIND  | ['2D', '2C', '2S', '2H', '7C', '8C', 'AD']    | ['2D', '2C', '2S', '2H', 'AD']
        Hands.FOUR_OF_A_KIND  | ['2D', '3C', '4S', 'AC', 'AH', 'AS', 'AD']    | ['4S', 'AS', 'AH', 'AC', 'AD']

        Hands.FULL_HOUSE      | ['2D', '2C', '2S', '3C', '3H', '4S', '4D']    | ['2D', '2C', '2S', '4S', '4D']
        Hands.FULL_HOUSE      | ['2D', '2C', '2S', '3C', '3H', '3S', 'AD']    | ['2C', '2D', '3C', '3H', '3S']

        Hands.STRAIGHT        | ['AD', '2D', '3D', '4D', '5D', '6D', 'QD']    | ['2D', '3D', '4D', '5D', '6D']
        Hands.STRAIGHT_FLUSH  | ['7D', '8D', '9D', '10D', 'JD', 'QD', 'KD']   | ['9D', '10D', 'JD', 'QD', 'KD']
        Hands.STRAIGHT_FLUSH  | ['7S', '8S', '9S', '10S', 'JS', 'QS', 'AS']   | ['8S', '9S', '10S', 'JS', 'QS']
        Hands.STRAIGHT_FLUSH  | ['7S', '8S', '9S', '10S', 'JS', 'QD', 'KS']   | ['7S', '8S', '9S', '10S', 'JS']

        Hands.ROYAL_FLUSH     | ['2D', '3C', '10D', 'JD', 'QD', 'KD', 'AD']   | ['10D', 'JD', 'QD', 'KD', 'AD']
        Hands.ROYAL_FLUSH     | ['10D', '10C', '10D', 'JD', 'QD', 'KD', 'AD'] | ['10D', 'JD', 'QD', 'KD', 'AD']
    }

    def "test with incorrect hand combinations"() {
        when:
        VavrList.ofAll(cards).bestCardsForHand(Hands.ROYAL_FLUSH)

        then:
        thrown IllegalArgumentException

        where:
        cards << [[], [Card.from(Suit.CLUB, Honour.ACE)] * 6, [Card.from(Suit.CLUB, Honour.ACE)] * 8]
    }
}
