package com.tp.holdem.logic.model;

import com.tp.holdem.logic.PlayerFunctions;
import com.tp.holdem.model.common.Moves;
import com.tp.holdem.model.common.Phase;
import com.tp.holdem.model.message.dto.PokerTableDTO;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.Builder;
import lombok.Value;

import java.util.Objects;

@Value
@Builder(toBuilder = true)
public class PokerTable {
	private int smallBlindAmount;
	private int bigBlindAmount;
	private Deck deck;
	private Player bettingPlayer;
	private Player winnerPlayer;
	private Player dealer;
	private Player bigBlind;
	private Player smallBlind;
	private Phase phase;
	@Builder.Default
	private List<Player> allPlayers = List.empty();
	@Builder.Default
	private List<Card> cardsOnTable = List.empty();
	@Builder.Default
	private Map<Integer, Moves> movesThisPhase = HashMap.empty();

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

	public int highestBetThisPhase() {
		return Math.max(bigBlindAmount, allPlayers.map(Player::getBetAmountThisPhase).max().getOrElse(bigBlindAmount));
	}

	public int potAmount() {
		return allPlayers.map(Player::getBetAmount).sum().intValue();
	}

	public int potAmountThisPhase() {
		return allPlayers.map(Player::getBetAmountThisPhase).sum().intValue();
	}

	public PokerTableDTO toDTO() {
		return PokerTableDTO.builder()
				.phase(phase)
				.potAmount(potAmount())
				.potAmountThisPhase(potAmountThisPhase())
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

	public PokerTable nextPhase() {
		if (phase == Phase.START)
			return goToPreFlopPhase();
		if (phase == Phase.PRE_FLOP)
			return goToFlopPhase();
		return this;
	}

	private PokerTable goToPreFlopPhase() {
		final Player bettingPlayer = PlayerFunctions.FIRST_BET_IN_ROUND.apply(getSmallBlind(), this);

		return this.toBuilder()
				.phase(getPhase().nextPhase())
				.allPlayers(getAllPlayers().replace(getSmallBlind(), bettingPlayer))
				.bettingPlayer(bettingPlayer)
				.movesThisPhase(HashMap.empty())
				.build();
	}

	private PokerTable goToFlopPhase() {
		final List<Player> preparedPlayers = getAllPlayers().map(Player::newPhase);
		final Player smallBlindPlayer = preparedPlayers.find(player -> Objects.equals(player.getNumber(), getSmallBlind().getNumber()))
				.getOrElseThrow(() -> new IllegalStateException("Could not find small blind player"));

		final PokerTable updatedTable = this.toBuilder()
				.phase(getPhase().nextPhase())
				.allPlayers(preparedPlayers)
				.cardsOnTable(deck.drawCards(3))
				.movesThisPhase(HashMap.empty())
				.build();

		final Player bettingPlayer = PlayerFunctions.BET_IN_PHASE.apply(smallBlindPlayer, updatedTable);

		return updatedTable.toBuilder()
				.allPlayers(updatedTable.getAllPlayers().replace(smallBlindPlayer, bettingPlayer))
				.bettingPlayer(bettingPlayer)
				.build();
	}

	public PokerTable playerActed(Player playerAfterAction, Moves move) {
		final Player beforeAction = allPlayers.find(player -> Objects.equals(player.getNumber(), playerAfterAction.getNumber()))
				.getOrElseThrow(() -> new IllegalStateException("Unregistered player acted"));

		return this.toBuilder()
				.allPlayers(allPlayers.replace(beforeAction, playerAfterAction.bettingTurnOver()))
				.movesThisPhase(movesThisPhase.put(beforeAction.getNumber(), move))
				.build();
	}

	public boolean isPhaseOver() {
		return movesThisPhase.size() == allPlayers.filter(Player::playing).size();
	}

	public PokerTable nextPlayerToBet() {
		final Player newBettingPlayer = allPlayers.get((allPlayers.indexOf(bettingPlayer) + 1) % allPlayers.size());
		final Player modifiedNewBettingPlayer = PlayerFunctions.BET_IN_PHASE.apply(newBettingPlayer, this);

		return this.toBuilder()
				.allPlayers(allPlayers.replace(newBettingPlayer, modifiedNewBettingPlayer))
				.bettingPlayer(modifiedNewBettingPlayer)
				.build();
	}
}