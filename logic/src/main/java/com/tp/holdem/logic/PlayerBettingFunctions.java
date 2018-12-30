package com.tp.holdem.logic;

import com.tp.holdem.logic.model.Player;
import com.tp.holdem.logic.model.PokerTable;
import com.tp.holdem.model.common.Moves;
import io.vavr.collection.List;

public class PlayerBettingFunctions {
	public static Player betInPhase(Player player, PokerTable table) {
		//TODO if player HAS TO all in show only all in and fold, not call and raise
		final Player modifiedPlayer = player.betRanges(
				Math.min(player.availableChips(), table.getBigBlindAmount()),
				player.availableChips()
		);

		if (table.potAmountThisPhase() == 0)
			return modifiedPlayer.withMoves(
					List.of(Moves.BET, Moves.CHECK, Moves.FOLD)
			);

		if (modifiedPlayer.getBetAmountThisPhase() < table.highestBetThisPhase())
			return modifiedPlayer
					.withMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
					.betRanges(
							Math.min(table.getBigBlindAmount(), player.availableChips() - (table.highestBetThisPhase() - player.getBetAmountThisPhase())),
							player.availableChips() - (table.highestBetThisPhase() - player.getBetAmountThisPhase())
					);

		return modifiedPlayer.withMoves(List.of(Moves.CHECK, Moves.RAISE, Moves.FOLD));
	}

	public static Player firstBetInRound(Player player, PokerTable table) {
		return player.withMoves(List.of(Moves.CALL, Moves.RAISE, Moves.FOLD))
				.betRanges(
						Math.min(player.availableChips(), table.getBigBlindAmount()),
						player.availableChips() - (table.highestBetThisPhase() - player.getBetAmountThisPhase())
				);
	}

	public static Player smallBlindTime(Player player, PokerTable table) {
		if (player.availableChips() < table.getSmallBlindAmount())
			return player.allIn();
		return player.bet(table.getSmallBlindAmount());
	}

	public static Player bigBlindTime(Player player, PokerTable table) {
		if (player.availableChips() < table.getBigBlindAmount())
			return player.allIn();
		return player.bet(table.getBigBlindAmount());
	}
}
