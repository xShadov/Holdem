package com.tp.holdem.core.model;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public class CardCoordinates {
	private static Map<Honour, Integer> honourCoordinates = HashMap.ofEntries(
			Tuple.of(Honour.ACE, 2),
			Tuple.of(Honour.TWO, 75),
			Tuple.of(Honour.THREE, 148),
			Tuple.of(Honour.FOUR, 221),
			Tuple.of(Honour.FIVE, 294),
			Tuple.of(Honour.SIX, 367),
			Tuple.of(Honour.SEVEN, 440),
			Tuple.of(Honour.EIGHT, 513),
			Tuple.of(Honour.NINE, 586),
			Tuple.of(Honour.TEN, 659),
			Tuple.of(Honour.JACK, 732),
			Tuple.of(Honour.QUEEN, 805),
			Tuple.of(Honour.KING, 878)
	);

	private static Map<Suit, Integer> suitCoordinates = HashMap.of(
			Suit.CLUB, 3,
			Suit.DIAMOND, 297,
			Suit.HEART, 101,
			Suit.SPADE, 199
	);


	public static Tuple2<Integer, Integer> find(Card card) {
		return Tuple.of(
				honourCoordinates.get(card.getHonour())
						.getOrElseThrow(() -> new IllegalArgumentException(String.format("Coordinate for %s not found", card.getHonour()))),
				suitCoordinates.get(card.getSuit())
						.getOrElseThrow(() -> new IllegalArgumentException(String.format("Coordinate for %s not found", card.getSuit())))
		);
	}
}
