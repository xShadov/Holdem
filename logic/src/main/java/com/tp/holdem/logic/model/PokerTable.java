package com.tp.holdem.logic.model;

import com.tp.holdem.logic.HandOperations;
import com.tp.holdem.logic.PlayerExceptions;
import com.tp.holdem.logic.PlayerFunctions;
import com.tp.holdem.model.common.Moves;
import com.tp.holdem.model.common.Phase;
import com.tp.holdem.model.message.dto.PokerTableDTO;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Value
@Builder(toBuilder = true)
public class PokerTable {
	private int smallBlindAmount;
	private int bigBlindAmount;
	private boolean showdown;
	private boolean gameOver;
	@Builder.Default
	private Deck deck = Deck.brandNew();
	@Builder.Default
	private Phase phase = Phase.START;
	@Builder.Default
	private PlayerNumber bettingPlayer = PlayerNumber.empty();
	@Builder.Default
	private PlayerNumber winnerPlayer = PlayerNumber.empty();
	@Builder.Default
	private PlayerNumber dealer = PlayerNumber.empty();
	@Builder.Default
	private PlayerNumber bigBlind = PlayerNumber.empty();
	@Builder.Default
	private PlayerNumber smallBlind = PlayerNumber.empty();
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

	public PokerTable playerLeft(PlayerNumber playerNumber) {
		final Player foundPlayer = allPlayers
				.find(PlayerFunctions.byNumber(playerNumber.getNumber()))
				.getOrElseThrow(PlayerExceptions.PLAYER_NOT_FOUND);

		final List<Player> modifiedPlayers = allPlayers
				.replace(foundPlayer, foundPlayer.toBuilder().inGame(false).build());

		return this.toBuilder().allPlayers(modifiedPlayers).build();
	}

	public int highestBetThisPhase() {
		return allPlayers.map(Player::getBetAmountThisPhase).max().getOrElse(0);
	}

	public int potAmount() {
		return allPlayers.map(Player::getBetAmount).sum().intValue();
	}

	public int potAmountThisPhase() {
		return allPlayers.map(Player::getBetAmountThisPhase).sum().intValue();
	}

	public PokerTable nextPhase() {
		final Phase nextPhase = phase.nextPhase();
		if (nextPhase == Phase.PRE_FLOP)
			return goToPreFlopPhase();
		if (nextPhase == Phase.FLOP)
			return goToNextPhase(3);
		if (nextPhase == Phase.TURN || nextPhase == Phase.RIVER)
			return goToNextPhase(1);
		throw new IllegalStateException("There is no next phase");
	}

	private PokerTable goToPreFlopPhase() {
		final Player smallBlindPlayer = getSmallBlind().getOrElseThrow(PlayerExceptions.PLAYER_NOT_FOUND);
		final Player bettingPlayer = PlayerFunctions.FIRST_BET_IN_ROUND.apply(smallBlindPlayer, this);

		return this.toBuilder()
				.phase(getPhase().nextPhase())
				.allPlayers(getAllPlayers().replace(smallBlindPlayer, bettingPlayer))
				.bettingPlayer(PlayerNumber.of(bettingPlayer.getNumber()))
				.movesThisPhase(HashMap.empty())
				.build();
	}

	private PokerTable goToNextPhase(int cards) {
		final List<Player> preparedPlayers = getAllPlayers().map(Player::prepareForNewPhase);
		final Player smallBlindPlayer = preparedPlayers
				.find(player -> Objects.equals(player.getNumber(), getSmallBlind().getOrElseThrow(PlayerExceptions.PLAYER_NOT_FOUND).getNumber()))
				.getOrElseThrow(PlayerExceptions.PLAYER_NOT_FOUND);

		final PokerTable updatedTable = this.toBuilder()
				.phase(getPhase().nextPhase())
				.allPlayers(preparedPlayers)
				.cardsOnTable(getCardsOnTable().appendAll(deck.drawCards(cards)))
				.movesThisPhase(HashMap.empty())
				.build();

		final Player bettingPlayer = PlayerFunctions.BET_IN_PHASE.apply(smallBlindPlayer, updatedTable);

		return updatedTable.toBuilder()
				.allPlayers(updatedTable.getAllPlayers().replace(smallBlindPlayer, bettingPlayer))
				.bettingPlayer(PlayerNumber.of(bettingPlayer.getNumber()))
				.build();
	}

	public PhaseStatus phaseStatus() {
		int notAllInCount = allPlayers
				.filter(Player::playing)
				.count(player -> !player.isAllIn());

		boolean allPlayersMoved = movesThisPhase.size() == allPlayers.filter(Player::playing).size();

		if (notAllInCount <= 1 && allPlayersMoved)
			return PhaseStatus.EVERYBODY_ALL_IN;

		int notFoldedCount = allPlayers
				.filter(Player::playing)
				.count(player -> !player.isFolded());

		if (notFoldedCount == 1)
			return PhaseStatus.EVERYBODY_FOLDED;

		boolean allBetsAreEqual = allPlayers
				.filter(Player::playing)
				.forAll(player -> player.isAllIn() || player.getBetAmountThisPhase() == highestBetThisPhase());

		return allPlayersMoved && allBetsAreEqual ? PhaseStatus.READY_FOR_NEXT : PhaseStatus.KEEP_GOING;
	}

	public PokerTable nextPlayerToBet() {
		final Player newBettingPlayer = allPlayers.get((allPlayers.indexOf(getBettingPlayer().getOrElseThrow(PlayerExceptions.PLAYER_NOT_FOUND)) + 1) % allPlayers.size());
		final Player modifiedNewBettingPlayer = PlayerFunctions.BET_IN_PHASE.apply(newBettingPlayer, this);

		return this.toBuilder()
				.allPlayers(allPlayers.replace(newBettingPlayer, modifiedNewBettingPlayer))
				.bettingPlayer(PlayerNumber.of(modifiedNewBettingPlayer.getNumber()))
				.build();
	}

	public PokerTable roundOver() {
		final List<Player> playersAfterRound = allPlayers.map(Player::roundOver);

		final PokerTable updatedTable = this.toBuilder()
				.allPlayers(playersAfterRound)
				.build();

		final Player winner = HandOperations.findWinner(playersAfterRound, updatedTable);

		final Player prizedWinner = winner.toBuilder()
				.chipsAmount(winner.getChipsAmount() + updatedTable.potAmount())
				.build();

		return updatedTable.toBuilder()
				.winnerPlayer(PlayerNumber.of(winner.getNumber()))
				.allPlayers(updatedTable.getAllPlayers().replace(winner, prizedWinner))
				.phase(Phase.OVER)
				.build();
	}

	public PokerTable dealCards() {
		log.debug("Dealing cards to players");
		final List<Player> playersWithCards = deck.dealCards(2, allPlayers);
		return this.toBuilder()
				.allPlayers(playersWithCards)
				.build();
	}

	public PokerTable preparePlayersForNewGame(int startingChips) {
		return toBuilder()
				.allPlayers(getAllPlayers().map(player -> player.prepareForNewGame(startingChips)))
				.build();
	}

	public Option<Player> getBettingPlayer() {
		return allPlayers.find(PlayerFunctions.byNumber(bettingPlayer.getNumber()));
	}

	public Option<Player> getWinnerPlayer() {
		return allPlayers.find(PlayerFunctions.byNumber(winnerPlayer.getNumber()));
	}

	public Option<Player> getDealer() {
		return allPlayers.find(PlayerFunctions.byNumber(dealer.getNumber()));
	}

	public Option<Player> getBigBlind() {
		return allPlayers.find(PlayerFunctions.byNumber(bigBlind.getNumber()));
	}

	public Option<Player> getSmallBlind() {
		return allPlayers.find(PlayerFunctions.byNumber(smallBlind.getNumber()));
	}

	public PokerTableDTO toDTO() {
		return PokerTableDTO.builder()
				.phase(phase)
				.gameOver(gameOver)
				.potAmount(potAmount())
				.potAmountThisPhase(potAmountThisPhase())
				.smallBlindAmount(smallBlindAmount)
				.bigBlindAmount(bigBlindAmount)
				.dealer(getDealer().map(Player::toPlayerDTO).getOrNull())
				.bigBlind(getBigBlind().map(Player::toPlayerDTO).getOrNull())
				.smallBlind(getSmallBlind().map(Player::toPlayerDTO).getOrNull())
				.allPlayers(allPlayers.map(Player::toPlayerDTO).toJavaList())
				.bettingPlayer(getBettingPlayer().map(Player::toPlayerDTO).getOrNull())
				.cardsOnTable(cardsOnTable.map(Card::toDTO).toJavaList())
				.winnerPlayer(getWinnerPlayer().map(Player::toPlayerDTO).getOrNull())
				.build();
	}

	public PokerTable newRound(AtomicLong handCount) {
		log.debug("Preparing players for new round");
		final List<Player> playersWithCleanBets = getAllPlayers().map(Player::prepareForNewRound);

		final Player dealerPlayer = playersWithCleanBets.get((int) (handCount.get() % playersWithCleanBets.size()));

		final Player smallBlindPlayer = playersWithCleanBets.get((int) ((handCount.get() + 1) % playersWithCleanBets.size()));
		log.debug(String.format("Taking small blind from player: %d", smallBlindPlayer.getNumber()));
		final Player newSmallBlindPlayer = PlayerFunctions.SMALL_BLIND_TIME.apply(smallBlindPlayer, this);

		final Player bigBlindPlayer = playersWithCleanBets.get((int) ((handCount.get() + 2) % playersWithCleanBets.size()));
		log.debug(String.format("Taking big blind from player: %d", bigBlindPlayer.getNumber()));
		final Player newBigBlindPlayer = PlayerFunctions.BIG_BLIND_TIME.apply(bigBlindPlayer, this);

		final PokerTable updatedTable = toBuilder()
				.deck(Deck.brandNew())
				.cardsOnTable(List.empty())
				.phase(Phase.START)
				.showdown(false)
				.movesThisPhase(HashMap.empty())
				.allPlayers(playersWithCleanBets.replace(smallBlindPlayer, newSmallBlindPlayer).replace(bigBlindPlayer, newBigBlindPlayer))
				.winnerPlayer(PlayerNumber.empty())
				.bettingPlayer(PlayerNumber.empty())
				.dealer(PlayerNumber.of(dealerPlayer.getNumber()))
				.bigBlind(PlayerNumber.of(newBigBlindPlayer.getNumber()))
				.smallBlind(PlayerNumber.of(newSmallBlindPlayer.getNumber()))
				.build();

		return updatedTable.dealCards();
	}

	public PokerTable playerMove(Integer playerNumber, Moves move, int betAmount) {
		final Player actionPlayer = getAllPlayers()
				.find(PlayerFunctions.byNumber(playerNumber))
				.getOrElseThrow(PlayerExceptions.PLAYER_NOT_FOUND);

		final Player playerAfterAction;

		switch (move) {
			case FOLD:
				playerAfterAction = actionPlayer.fold();
				break;
			case ALLIN:
				playerAfterAction = actionPlayer.allIn();
				break;
			case BET:
				playerAfterAction = actionPlayer.bet(betAmount);
				break;
			case CHECK:
				playerAfterAction = actionPlayer;
				break;
			case CALL:
				playerAfterAction = actionPlayer.bet(highestBetThisPhase() - actionPlayer.getBetAmountThisPhase());
				break;
			case RAISE:
				playerAfterAction = actionPlayer.bet(highestBetThisPhase() - actionPlayer.getBetAmountThisPhase()).bet(betAmount);
				break;
			default:
				throw new IllegalArgumentException(String.format("Unsupported action type: %s", move));
		}

		return this.toBuilder()
				.allPlayers(allPlayers.replace(actionPlayer, playerAfterAction.bettingTurnOver()))
				.movesThisPhase(movesThisPhase.put(actionPlayer.getNumber(), move))
				.build();
	}

	public boolean isNotPlayable() {
		return allPlayers
				.filter(Player::isInGame)
				.filter(player -> player.getChipsAmount() > 0)
				.length() < 2;
	}

	public PokerTable gameOver() {
		return toBuilder()
				.gameOver(true)
				.phase(Phase.OVER)
				.allPlayers(getAllPlayers().map(Player::gameOver))
				.build();
	}
}