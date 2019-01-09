package com.tp.holdem.logic.utils


import com.tp.holdem.logic.model.Card
import io.vavr.collection.List
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CardHelperExtensionsSpec extends Specification {
    private def mapCards = { cards ->
        List.ofAll(cards).map({ code -> Card.coded(code) })
    }

    def "counting overall value"() {
        expect:
        mapCards(cards).overallValue() == value

        where:
        cards                    || value
        []                       || 0
        ['2C', '2D']             || 4
        ['3C', '4C', '5C', '6C'] || 18
        ['AC', 'AD', 'KS']       || 41
    }

    def "extracting highest cards from list with correct inputs"() {
        expect:
        mapCards(cards).extractHighestWithSameHonour(number).toSet() == mapCards(value).toSet()

        where:
        cards                          | number || value
        ['2C', '2D']                   | 2      || ['2C', '2D']
        ['3C', '4C', '6D', '6C']       | 2      || ['6D', '6C']
        ['AC', 'AD', 'KS']             | 2      || ['AC', 'AD']
        ['AC', 'AD', 'KS', 'KD', 'KC'] | 2      || ['AC', 'AD']
        ['QC', 'QD', 'KS', 'KD', 'KC'] | 2      || ['KS', 'KD']
    }

    def "extracting highest cards from list with incorrect inputs"() {
        when:
        mapCards(cards).extractHighestWithSameHonour(number)

        then:
        thrown IllegalStateException

        where:
        cards                    | number
        ['2C', '3D']             | 2
        ['3C', '4C', '5D', '6C'] | 2
        ['AC', 'QD', 'KS']       | 2
    }
}