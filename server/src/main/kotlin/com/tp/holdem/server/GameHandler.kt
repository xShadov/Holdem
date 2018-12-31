package com.tp.holdem.server

import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.message.PlayerActionMessage
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.extensions.*
import com.tp.holdem.model.PhaseStatus
import com.tp.holdem.model.Player
import com.tp.holdem.model.PlayerNumber
import com.tp.holdem.model.PokerTable
import java.util.concurrent.atomic.AtomicLong

internal class GameHandler(
        private val gameParams: GameParams,
        private var table: PokerTable = PokerTable.withBlinds(gameParams.bigBlindAmount, gameParams.smallBlindAmount)
) {
    private val log by lazyLogger()

    private val handCount = AtomicLong(-1)

    fun startGame(): PokerTable {
        if (table.allPlayers.size() != gameParams.playerCount)
            throw IllegalStateException("Game cannot be started - wrong number of players")

        log.debug(String.format("Starting game with %d players", gameParams.playerCount))

        this.table = table.preparePlayersForNewGame(gameParams.startingChips)

        return startRound()
    }

    fun startRound(): PokerTable {
        if (this.table.isNotPlayable()) {
            log.debug("Game is over")
            this.table = table.gameOver()
            return this.table
        }

        handCount.incrementAndGet()

        log.debug(String.format("Starting new round number: %d", handCount.get()))

        this.table = table.newRound(handCount)

        log.debug(String.format("Players ready for new round: %s", table.allPlayers.map { it.name }))

        return startPhase()
    }

    fun startPhase(): PokerTable {
        log.debug(String.format("Staring phase: %s", table.phase.nextPhase()))

        this.table = table.nextPhase()

        return table
    }

    fun handlePlayerMove(playerNumber: Int, content: PlayerActionMessage): PokerTable {
        log.debug(String.format("Handling player %d move: %s", playerNumber, content.move))

        this.table = table.playerMove(playerNumber, content.move, content.betAmount)

        val phaseStatus = table.phaseStatus()

        log.debug(String.format("Phase status is: %s", phaseStatus))

        if (phaseStatus == PhaseStatus.EVERYBODY_FOLDED || (phaseStatus == PhaseStatus.READY_FOR_NEXT && table.phase == Phase.RIVER)) {
            log.debug("Table round is over")
            return roundOver()
        }

        if (phaseStatus == PhaseStatus.EVERYBODY_ALL_IN) {
            this.table = table.showdownMode()
            return this.table
        }

        if (phaseStatus == PhaseStatus.READY_FOR_NEXT) {
            log.debug("Table phase is over")
            return startPhase()
        }

        log.debug("Finding next player to bet")
        this.table = table.nextPlayerToBet()

        return this.table
    }

    fun roundOver(): PokerTable {
        log.debug("Performing round-over operation")
        this.table = table.roundOver()

        return table
    }

    fun connectPlayer(): Player {
        log.debug("Connecting new player")
        val newPlayer = numberedPlayer()
        this.table = table.addPlayer(newPlayer)
        return newPlayer
    }

    fun disconnectPlayer(playerNumber: Int): PokerTable {
        log.debug(String.format("Disconnecting player: %d", playerNumber))
        this.table = table.playerLeft(PlayerNumber.of(playerNumber))
        return table
    }

    private fun numberedPlayer(): Player {
        return table.allPlayers
                .lastOption()
                .map { it.number }
                .map { number -> number + 1 }
                .map { Player.numbered(it) }
                .getOrElse { Player.numbered(0) }
    }
}
