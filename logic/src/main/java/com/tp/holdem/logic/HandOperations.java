package com.tp.holdem.logic;

import com.tp.holdem.logic.model.*;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.function.Function;

@Slf4j
public class HandOperations {
	private static final Comparator<Card> CARD_COMPARATOR = Comparator.comparing(Card::getValue);

	private static HandRank findHandRank(Player player, PokerTable pokerTable) {
		final List<Card> cards = List.ofAll(player.getHand())
				.appendAll(pokerTable.getCardsOnTable())
				.sorted(CARD_COMPARATOR);

		final Hands playerHand = HandFinder.findHand(cards);
		final List<Card> bestCardsThatMakeHand = BestCardCombinationFinder.findBestCardsForHand(cards, playerHand);

		return new HandRank(playerHand, bestCardsThatMakeHand);
	}

	public static Tuple2<Player, HandRank> findWinner(List<Player> allPlayers, PokerTable pokerTable) {
		final Map<Player, HandRank> hands = allPlayers.filter(Player::playing)
				.toMap(Function.identity(), player -> findHandRank(player, pokerTable));

		log.debug(String.format("Players to hands map: %s", hands));

		return hands.values().maxBy(HandRankComparator.INSTANCE)
				.flatMap(handRank -> hands.find(tuple -> tuple._2.equals(handRank)))
				.getOrElseThrow(PlayerExceptions.PLAYER_NOT_FOUND);
	}
}
