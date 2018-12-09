package com.tp.holdem.core.model;


import io.vavr.collection.List;
import lombok.Data;

import java.util.stream.IntStream;

@Data
public class Deck {
	private java.util.List<Card> cards = List.of(Honour.values())
			.flatMap(honour ->
					List.of(
							Card.from(Suit.HEART, honour),
							Card.from(Suit.CLUB, honour),
							Card.from(Suit.DIAMOND, honour),
							Card.from(Suit.SPADE, honour)
					)
			).shuffle()
			.toJavaList();

	public void dealCards(final int numberOfCards, final List<Player> players) {
		final List<Player> playing = players
				.filter(Player::playing);

		IntStream.range(0, numberOfCards).forEach(i ->
				playing.forEach(player -> {
					player.addCard(cards.get(0));
					cards.remove(0);
				})
		);
	}

	public Card drawCard() {
		return List.ofAll(cards).headOption()
				.peek(card -> cards.remove(0))
				.getOrElseThrow(() -> new IllegalStateException("Card cannot be drawn - deck is empty"));
	}

}
