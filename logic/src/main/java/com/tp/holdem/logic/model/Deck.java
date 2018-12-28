package com.tp.holdem.logic.model;


import com.tp.holdem.model.common.Honour;
import com.tp.holdem.model.common.Suit;
import io.vavr.collection.List;
import lombok.ToString;

@ToString
public class Deck {
	private List<Card> cards = List.of(Honour.values())
			.flatMap(honour ->
					List.of(
							Card.from(Suit.HEART, honour),
							Card.from(Suit.CLUB, honour),
							Card.from(Suit.DIAMOND, honour),
							Card.from(Suit.SPADE, honour)
					)
			)
			.shuffle();

	public static Deck brandNew() {
		return new Deck();
	}

	public List<Player> dealCards(final int numberOfCards, final List<Player> players) {
		return players
				.filter(Player::playing)
				.map(player -> player.withCards(List.fill(numberOfCards, this::drawCard)))
				.appendAll(players.filter(Player::notPlaying));
	}

	public List<Card> drawCards(int number) {
		final List<Card> drawn = cards.take(number);
		cards = cards.removeAll(drawn);
		return drawn;
	}

	public Card drawCard() {
		return cards.headOption()
				.peek(card -> cards = cards.removeAt(0))
				.getOrElseThrow(() -> new IllegalStateException("Card cannot be drawn - deck is empty"));
	}

}
