package com.tp.holdem.logic.model;

import com.tp.holdem.model.message.dto.PokerTableDTO;
import io.vavr.collection.List;
import lombok.Builder;
import lombok.Value;

import java.util.Objects;

@Value
@Builder(toBuilder = true)
public class PokerTable {
	private int smallBlindAmount;
	private int bigBlindAmount;
	private Player bettingPlayer;
	private Player winnerPlayer;
	private Player dealer;
	private Player bigBlind;
	private Player smallBlind;
	@Builder.Default
	private List<Player> allPlayers = List.empty();
	@Builder.Default
	private List<Card> cardsOnTable = List.empty();

	public PokerTable addPlayer(Player player) {
		return this.toBuilder()
				.allPlayers(getAllPlayers().append(player))
				.build();
	}

	public PokerTable playerLeft(Integer playerNumber) {
		final Player foundPlayer = allPlayers
				.find(player -> Objects.equals(player.getNumber(), playerNumber))
				.getOrElseThrow(() -> new IllegalArgumentException("Player not found"));

		final List<Player> modifiedPlayers = allPlayers
				.replace(foundPlayer, foundPlayer.toBuilder().inGame(false).build());

		return this.toBuilder().allPlayers(modifiedPlayers).build();
	}

	public int highestBet() {
		return Math.max(bigBlindAmount, allPlayers.map(Player::getBetAmount).max().getOrElse(bigBlindAmount));
	}

	public int getPotAmount() {
		return allPlayers.map(Player::getBetAmount).sum().intValue();
	}

	public PokerTableDTO toDTO() {
		return PokerTableDTO.builder()
				.potAmount(getPotAmount())
				.smallBlindAmount(smallBlindAmount)
				.bigBlindAmount(bigBlindAmount)
				.dealer(dealer.toPlayerDTO())
				.bigBlind(bigBlind.toPlayerDTO())
				.smallBlind(smallBlind.toPlayerDTO())
				.allPlayers(allPlayers.map(Player::toPlayerDTO).toJavaList())
				.bettingPlayer(bettingPlayer.toPlayerDTO())
				.cardsOnTable(cardsOnTable.map(Card::toDTO).toJavaList())
				.build();
	}
}