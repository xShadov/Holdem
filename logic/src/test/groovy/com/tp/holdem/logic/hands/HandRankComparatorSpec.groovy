package com.tp.holdem.logic.hands

import com.tp.holdem.logic.model.Card
import com.tp.holdem.logic.model.HandRank
import com.tp.holdem.logic.model.Hands
import io.vavr.Tuple
import io.vavr.collection.List as VavrList
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class HandRankComparatorSpec extends Specification {
    def mapHandRank = { tuple ->
        return HandRank.from(tuple._2(), VavrList.ofAll(tuple._1()).map({ code -> Card.coded(code) }))
    }

    def handRankComparator = HandRankComparator.INSTANCE

    def "test comparisons of same hands"() {
        expect:
        handRankComparator.compare(mapHandRank.call(Tuple.of(one, hand)), mapHandRank.call(Tuple.of(two, hand))) == result

        where:
        hand                  | one                             | two                             || result
        Hands.HIGH_CARD       | ['2C', '4D', '6S', '8H', '10D'] | ['3C', '4D', '6S', '8H', '10D'] || -1
        Hands.HIGH_CARD       | ['2C', '5D', '6S', '8H', '10D'] | ['2C', '4D', '6S', '8H', '10D'] || 1
        Hands.HIGH_CARD       | ['2C', '4D', '6S', '8H', '10D'] | ['2C', '4D', '7S', '8H', '10D'] || -1
        Hands.HIGH_CARD       | ['2C', '4D', '6S', '9H', '10D'] | ['2C', '4D', '6S', '8H', '10D'] || 1
        Hands.HIGH_CARD       | ['2C', '4D', '6S', '8H', '10D'] | ['2C', '4D', '6S', '8H', 'JD']  || -1

        Hands.PAIR            | ['2C', '2D', '6S', '8H', 'JD']  | ['2C', '2D', '6S', '8H', '10D'] || 1
        Hands.PAIR            | ['2C', '2D', '6S', '7H', '10D'] | ['2C', '2D', '6S', '8H', '10D'] || -1
        Hands.PAIR            | ['2C', '2D', '6S', '8H', '10D'] | ['2C', '2D', '7S', '8H', '10D'] || -1
        Hands.PAIR            | ['2C', '4D', '6S', 'AH', 'AD']  | ['3C', '4D', '6S', 'AH', 'AD']  || -1

        Hands.TWO_PAIR        | ['2C', '6D', '6S', 'AH', 'AD']  | ['3C', '6D', '6S', 'AH', 'AD']  || -1
        Hands.TWO_PAIR        | ['JC', 'QD', 'QS', 'KH', 'KD']  | ['2C', '2D', '6S', 'AH', 'AD']  || -1
        Hands.TWO_PAIR        | ['AC', 'QD', 'QS', 'KH', 'KD']  | ['QC', 'AD', 'AS', 'KH', 'KD']  || -1

        Hands.THREE_OF_A_KIND | ['2C', '2D', '2S', 'KH', 'AD']  | ['2C', '2D', '2S', 'QH', 'KD']  || 1
        Hands.THREE_OF_A_KIND | ['2C', '2D', '2S', '3H', 'AD']  | ['2C', '2D', '2S', '3H', 'KD']  || 1
        Hands.THREE_OF_A_KIND | ['AC', 'AD', 'AS', '10H', '2D'] | ['AC', 'AD', 'AS', '10H', '3D'] || -1
        Hands.THREE_OF_A_KIND | ['AC', 'AD', 'AS', '7H', '5D']  | ['AC', 'AD', 'AS', '8H', '5D']  || -1

        Hands.STRAIGHT        | ['2D', '3C', '4S', '5H', '6C']  | ['AC', '2D', '3C', '4S', '5H']  || 1
        Hands.STRAIGHT        | ['2D', '3C', '4S', '5H', '6C']  | ['3C', '4S', '5H', '6C', '7C']  || -1

        Hands.FLUSH           | ['2D', '4D', '6D', '8D', '10D'] | ['2C', '3C', '4C', '5C', 'AC']  || -1
        Hands.FLUSH           | ['2D', '3D', '4D', '5D', 'AD']  | ['9C', '10C', 'JC', 'QC', 'AC'] || -1
        Hands.FLUSH           | ['4D', '10D', 'JD', 'QD', 'AD'] | ['2C', '10C', 'JC', 'QC', 'AC'] || 1
        Hands.FLUSH           | ['2D', '9D', 'JD', 'QD', 'AD']  | ['7C', '8C', 'JC', 'QC', 'AC']  || 1

        Hands.FULL_HOUSE      | ['2D', '2S', '2H', '3C', '3D']  | ['2D', '2C', '2S', '4H', '4C']  || -1
        Hands.FULL_HOUSE      | ['3D', '3S', '3H', 'AC', 'AD']  | ['4D', '4C', '4S', '2H', '2C']  || -1
        Hands.FULL_HOUSE      | ['KD', 'KS', 'KH', 'AC', 'AD']  | ['AD', 'AC', 'AS', 'KH', 'KC']  || -1

        Hands.FOUR_OF_A_KIND  | ['2D', '2S', '2H', '2C', 'AD']  | ['2D', '2C', '2S', '2H', 'KC']  || 1
        Hands.FOUR_OF_A_KIND  | ['AC', 'KS', 'KH', 'KC', 'KD']  | ['10D', 'KD', 'KS', 'KH', 'KC'] || 1

        Hands.STRAIGHT_FLUSH  | ['2C', '3C', '4C', '5C', '6C']  | ['AC', '2C', '3C', '4C', '5C']  || 1
        Hands.STRAIGHT_FLUSH  | ['2C', '3C', '4C', '5C', '6C']  | ['3C', '4C', '5C', '6C', '7C']  || -1

        Hands.ROYAL_FLUSH     | ['10D', 'JD', 'QD', 'KD', 'AD'] | ['10C', 'JC', 'QC', 'KC', 'AC'] || 0
    }

    def "test comparisons of different hands"() {
        expect:
        handRankComparator.compare(HandRank.empty(one), HandRank.empty(two)) == result

        where:
        one                  | two                   || result
        Hands.ROYAL_FLUSH    | Hands.FULL_HOUSE      || 1
        Hands.HIGH_CARD      | Hands.PAIR            || -1
        Hands.STRAIGHT_FLUSH | Hands.STRAIGHT        || 1
        Hands.TWO_PAIR       | Hands.FULL_HOUSE      || -1
        Hands.FOUR_OF_A_KIND | Hands.THREE_OF_A_KIND || 1

    }
}
