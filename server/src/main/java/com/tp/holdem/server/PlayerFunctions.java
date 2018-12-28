package com.tp.holdem.server;

import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.common.Moves;
import io.vavr.collection.List;

import java.util.function.BiFunction;

class PlayerFunctions {
	static final BiFunction<Player, PokerTable, Player> ROUND_START_BETTING_TIME = (player, table) -> {
		final Player modifiedPlayer = player.toBuilder()
				.minimumBet(table.getBigBlindAmount())
				.maximumBet(player.availableChips())
				.build();

		if (modifiedPlayer.getBetAmount() < table.highestBet())
			return modifiedPlayer.toBuilder()
					.possibleMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
					.build();
		return modifiedPlayer.toBuilder()
				.possibleMoves(List.of(Moves.CHECK, Moves.RAISE, Moves.FOLD))
				.build();
	};

	static final BiFunction<Player, PokerTable, Player> SMALL_BLIND_TIME = (player, table) -> {
		if (player.availableChips() < table.getSmallBlindAmount())
			return player.allIn();
		return player.bet(table.getSmallBlindAmount());
	};

	static final BiFunction<Player, PokerTable, Player> BIG_BLIND_TIME = (player, table) -> {
		if (player.availableChips() < table.getBigBlindAmount())
			return player.allIn();
		return player.bet(table.getBigBlindAmount());
	};
}