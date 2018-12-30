package com.tp.holdem.logic.extensions

import com.tp.holdem.model.Card
import com.tp.holdem.model.Player
import com.tp.holdem.model.PokerTable
import com.tp.holdem.common.message.dto.CardDTO
import com.tp.holdem.common.message.dto.CurrentPlayerDTO
import com.tp.holdem.common.message.dto.PlayerDTO
import com.tp.holdem.common.message.dto.PokerTableDTO

fun Card.toDTO(): CardDTO {
    return CardDTO.builder()
            .honour(honour)
            .suit(suit)
            .build()
}

fun Player.toPlayerDTO(): PlayerDTO {
    return PlayerDTO.builder()
            .allIn(allIn)
            .betAmount(betAmount)
            .betAmountThisPhase(betAmountThisPhase)
            .chipsAmount(chipsAmount)
            .folded(folded)
            .inGame(inGame)
            .name(name)
            .number(number)
            .build()
}

fun Player.toCurrentPlayerDTO(): CurrentPlayerDTO {
    return CurrentPlayerDTO.builder()
            .allIn(allIn)
            .betAmount(betAmount)
            .betAmountThisPhase(betAmountThisPhase)
            .chipsAmount(chipsAmount)
            .folded(folded)
            .inGame(inGame)
            .name(name)
            .number(number)
            .possibleMoves(possibleMoves.toJavaList())
            .maximumBet(maximumBet)
            .minimumBet(minimumBet)
            .hand(hand.map { it.toDTO() }.toJavaList())
            .build()
}

fun PokerTable.toDTO(): PokerTableDTO {
    return PokerTableDTO.builder()
            .phase(phase)
            .gameOver(gameOver)
            .potAmount(potAmount())
            .potAmountThisPhase(potAmountThisPhase())
            .smallBlindAmount(smallBlindAmount)
            .bigBlindAmount(bigBlindAmount)
            .dealer(getDealer().map { it.toPlayerDTO() }.orNull)
            .bigBlind(getBigBlind().map { it.toPlayerDTO() }.orNull)
            .smallBlind(getSmallBlind().map { it.toPlayerDTO() }.orNull)
            .allPlayers(allPlayers.map { it.toPlayerDTO() }.toJavaList())
            .bettingPlayer(getBettingPlayer().map { it.toPlayerDTO() }.orNull)
            .cardsOnTable(cardsOnTable.map { it.toDTO() }.toJavaList())
            .winnerPlayer(getWinnerPlayer().map { it.toPlayerDTO() }.orNull)
            .build()
}