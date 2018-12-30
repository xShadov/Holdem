package com.tp.holdem.logic.extensions

import com.tp.holdem.common.model.Phase
import com.tp.holdem.model.PhaseStatus
import com.tp.holdem.model.Player
import com.tp.holdem.model.PlayerNumber
import com.tp.holdem.model.PokerTable
import io.vavr.collection.HashMap

fun PokerTable.nextPhase(): PokerTable {
    val nextPhase = phase.nextPhase()
    if (nextPhase == Phase.PRE_FLOP)
        return goToPreFlopPhase()
    if (nextPhase == Phase.FLOP)
        return goToNextPhase(3)
    if (nextPhase == Phase.TURN || nextPhase == Phase.RIVER)
        return goToNextPhase(1)
    throw IllegalStateException("There is no next phase")
}

private fun PokerTable.goToPreFlopPhase(): PokerTable {
    val dealerPlayer = getDealer().assertFound()
    val bettingPlayer = dealerPlayer.firstBetInRound(this)

    return this.copy(
            phase = phase.nextPhase(),
            allPlayers = allPlayers.replace(dealerPlayer, bettingPlayer),
            bettingPlayer = PlayerNumber.of(bettingPlayer.number),
            movesThisPhase = HashMap.empty()
    )
}

private fun PokerTable.goToNextPhase(cards: Int): PokerTable {
    val preparedPlayers = allPlayers.map<Player> { it.prepareForNewPhase() }

    val updatedTable = this.copy(
            phase = phase.nextPhase(),
            allPlayers = preparedPlayers,
            cardsOnTable = cardsOnTable.appendAll(deck.drawCards(cards)),
            movesThisPhase = movesThisPhase.filterValues { it.goingToNextPhase() }
    )

    return updatedTable.nextPlayerToBetAfter(bigBlind.number)
}

fun PokerTable.nextPlayerToBet(): PokerTable {
    return nextPlayerToBetAfter(bettingPlayer.number)
}

private fun PokerTable.nextPlayerToBetAfter(playerNumber: Int): PokerTable {
    log.debug(String.format("Finding next to bet after: %d", playerNumber))

    val bettingPlayerIndex = allPlayers.indexOfNumber(playerNumber)
    var newBettingPlayer: Player
    var count = 1
    do {
        newBettingPlayer = allPlayers.get((bettingPlayerIndex + count++) % allPlayers.size())
    } while (newBettingPlayer.folded)

    val modifiedNewBettingPlayer = newBettingPlayer.betInPhase(this)

    return this.copy(
            allPlayers = allPlayers.replace(newBettingPlayer, modifiedNewBettingPlayer),
            bettingPlayer = PlayerNumber.of(modifiedNewBettingPlayer.number)
    )
}

fun PokerTable.phaseStatus(): PhaseStatus {
    val notAllInCount = allPlayers
            .filter { it.inGame }
            .count { player -> !player.allIn }

    val allPlayersMoved = movesThisPhase.size() == allPlayers.filter { it.inGame }.size()

    if (notAllInCount <= 1 && allPlayersMoved)
        return PhaseStatus.EVERYBODY_ALL_IN

    val notFoldedCount = allPlayers
            .filter { it.inGame }
            .count { player -> !player.folded }

    if (notFoldedCount == 1)
        return PhaseStatus.EVERYBODY_FOLDED

    val allBetsAreEqual = allPlayers
            .filter { it.playing() }
            .forAll { player -> player.allIn || player.betAmountThisPhase == highestBetThisPhase() }

    return if (allPlayersMoved && allBetsAreEqual) PhaseStatus.READY_FOR_NEXT else PhaseStatus.KEEP_GOING
}