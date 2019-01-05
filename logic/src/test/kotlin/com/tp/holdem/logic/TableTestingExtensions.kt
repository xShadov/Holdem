package com.tp.holdem.logic

import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.byNumber
import com.tp.holdem.logic.table.playerMove
import com.tp.holdem.logic.table.playerNumbers
import io.vavr.Tuple
import io.vavr.collection.List
import io.vavr.kotlin.getOrNull
import io.vavr.kotlin.toVavrList

fun PokerTable.Companion.sample(): PokerTable {
    return PokerTable.withBlinds(40, 20)
}

fun PokerTable.Companion.inPhase(phase: Phase): PokerTable {
    return PokerTable.sample().copy(phase = phase)
}

fun PokerTable.players(vararg player: Player): PokerTable = this.copy(
        allPlayers = List.of(*player)
)

fun PokerTable.bettingPlayer(number: Int, vararg moves: Moves, minimumBet: Int = 0, maximumBet: Int = 0): PokerTable {
    val player = allPlayers.byNumber(number)
    val modifiedPlayer = player.copy(
            possibleMoves = List.of(*moves),
            maximumBet = maximumBet,
            minimumBet = minimumBet
    )
    return this.copy(
            bettingPlayerNumber = PlayerNumber.of(number),
            allPlayers = allPlayers.replace(player, modifiedPlayer)
    )
}

fun PokerTable.everyoneMovedThisPhase(): PokerTable {
    return this.copy(
            latestMoves = playerNumbers()
                    .toMap { number -> Tuple.of(number, Moves.CALL) }
    )
}

fun PokerTable.everyoneHasTheSameBet(): PokerTable {
    return this.copy(
            allPlayers = allPlayers.map { it.copy(betAmountThisPhase = 100) }
    )
}

fun PokerTable.playersMovedThisPhase(vararg numbers: Int): PokerTable {
    return this.copy(
            latestMoves = List.ofAll(*numbers)
                    .map(::PlayerNumber)
                    .toMap { number -> Tuple.of(number, Moves.CALL) }
    )
}

fun PokerTable.notEveryoneHasTheSameBet(): PokerTable {
    val bets = List.of(100, 200, 300)
    return this.copy(
            allPlayers = allPlayers
                    .mapIndexed { index, player -> player.copy(betAmountThisPhase = bets.get(index % bets.size())) }
                    .toVavrList()
    )
}

fun PokerTable.playerMove(number: Int, move: Moves, betAmount: Int = 0): PokerTable {
    return playerMove(PlayerNumber.of(number), move, betAmount)
}

fun PokerTable.findPlayer(number: Int): Player {
    return allPlayers.byNumber(number)
}

fun PokerTable.isBetting(number: Int): Boolean {
    return this.bettingPlayerNumber.number == number
}

fun PokerTable.lastBetOf(number: Int): Moves? {
    return latestMoves.getOrNull(PlayerNumber.of(number))
}