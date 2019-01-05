package com.tp.holdem.logic.utils

import com.tp.holdem.common.message.dto.CardDTO
import com.tp.holdem.common.message.dto.CurrentPlayerDTO
import com.tp.holdem.common.message.dto.PlayerDTO
import com.tp.holdem.common.message.dto.PokerTableDTO
import com.tp.holdem.logic.table.*
import com.tp.holdem.logic.model.Card
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable

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
            .number(number.number)
            .build()
}

fun Player.toCurrentPlayerDTO(): CurrentPlayerDTO {
    return CurrentPlayerDTO.builder()
            .allIn(allIn)
            .betAmount(betAmount)
            .betAmountThisPhase(betAmountThisPhase)
            .chipsAmount(chipsAmount)
            .folded(folded)
            .hand(hand.map { it.toDTO() }.toJavaList())
            .inGame(inGame)
            .maximumBet(maximumBet)
            .minimumBet(minimumBet)
            .name(name)
            .number(number.number)
            .possibleMoves(possibleMoves.toJavaList())
            .build()
}

fun PokerTable.toDTO(): PokerTableDTO {
    return PokerTableDTO.builder()
            .allPlayers(allPlayers.map { it.toPlayerDTO() }.toJavaList())
            .bettingPlayer(getBettingPlayer().map { it.toPlayerDTO() }.orNull)
            .bigBlind(getBigBlind().map { it.toPlayerDTO() }.orNull)
            .bigBlindAmount(bigBlindAmount)
            .cardsOnTable(cardsOnTable.map { it.toDTO() }.toJavaList())
            .dealer(getDealer().map { it.toPlayerDTO() }.orNull)
            .gameOver(gameOver)
            .phase(phase)
            .potAmount(potAmount())
            .potAmountThisPhase(potAmountThisPhase())
            .smallBlind(getSmallBlind().map { it.toPlayerDTO() }.orNull)
            .smallBlindAmount(smallBlindAmount)
            .winnerPlayer(getWinnerPlayer().map { it.toPlayerDTO() }.orNull)
            .build()
}