package com.tp.holdem.logic;

import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.common.Moves;
import io.vavr.collection.List;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class PlayerFunctions {
	public static Predicate<Player> byNumber(Integer playerNumber) {
		return player -> Objects.equals(player.getNumber(), playerNumber);
	}

	public static final BiFunction<Player, PokerTable, Player> BET_IN_PHASE = (player, table) -> {
		final Player modifiedPlayer = player.toBuilder()
				.minimumBet(table.getBigBlindAmount())
				.maximumBet(player.availableChips())
				.build();

		if (table.potAmountThisPhase() == 0)
			return modifiedPlayer.toBuilder()
					.possibleMoves(List.of(Moves.BET, Moves.CHECK, Moves.FOLD))
					.build();

		if (modifiedPlayer.getBetAmountThisPhase() < table.highestBetThisPhase())
			return modifiedPlayer.toBuilder()
					.possibleMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
					.build();

		return modifiedPlayer.toBuilder()
				.possibleMoves(List.of(Moves.CHECK, Moves.RAISE, Moves.FOLD))
				.build();
	};

	public static final BiFunction<Player, PokerTable, Player> FIRST_BET_IN_ROUND = (player, table) -> {
		final Player modifiedPlayer = player.toBuilder()
				.minimumBet(table.getBigBlindAmount())
				.maximumBet(player.availableChips())
				.build();

		if (modifiedPlayer.getBetAmount() < table.highestBetThisPhase())
			return modifiedPlayer.toBuilder()
					.possibleMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
					.build();

		return modifiedPlayer.toBuilder()
				.possibleMoves(List.of(Moves.CHECK, Moves.RAISE, Moves.FOLD))
				.build();
	};

	public static final BiFunction<Player, PokerTable, Player> SMALL_BLIND_TIME = (player, table) -> {
		if (player.availableChips() < table.getSmallBlindAmount())
			return player.allIn();
		return player.bet(table.getSmallBlindAmount());
	};

	public static final BiFunction<Player, PokerTable, Player> BIG_BLIND_TIME = (player, table) -> {
		if (player.availableChips() < table.getBigBlindAmount())
			return player.allIn();
		return player.bet(table.getBigBlindAmount());
	};
}
