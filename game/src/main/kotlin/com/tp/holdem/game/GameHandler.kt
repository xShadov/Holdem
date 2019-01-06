package com.tp.holdem.game

import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.message.PlayerActionMessage
import com.tp.holdem.logic.table.*
import com.tp.holdem.logic.model.PhaseStatus
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import java.util.concurrent.atomic.AtomicLong

class GameHandler(
        private val gameParams: GameParams,
        private var table: PokerTable = PokerTable.withBlinds(gameParams.bigBlindAmount, gameParams.smallBlindAmount)
) {
    private val log by lazyLogger()

    private val handCount = AtomicLong(-1)

    fun startGame(): PokerTable {
        if (table.playerCount() != gameParams.playerCount)
            throw IllegalStateException("Game cannot be started - wrong number of players")

        return this.table.newGame(gameParams.startingChips)
                .also { log.debug("Starting game with ${gameParams.playerCount} players") }
                .also { this.table = it }
                .let { this.startRound() }
    }

    fun startRound(): PokerTable {
        if (this.table.notEnoughPlayersWithChips()) {
            return this.table.gameOver()
                    .also { log.debug("Game is over") }
                    .also { this.table = it }
        }

        return handCount.incrementAndGet()
                .also { log.debug("Starting new round number: ${handCount.get()}") }
                .let { this.table.newRound(handCount) }
                .also { log.debug("Players ready for new round: ${table.playerNames()}") }
                .also { this.table = it }
                .let { this.startPhase() }
    }

    fun startPhase(): PokerTable {
        log.debug("Staring phase: ${table.currentPhase().nextPhase()}")
        return this.table.goToNextPlayingPhase()
                .also { this.table = it }
    }

    fun handlePlayerMove(playerNumber: PlayerNumber, content: PlayerActionMessage): PokerTable {
        log.debug("Handling player $playerNumber move: ${content.move}")

        this.table = table.playerMove(playerNumber, content.move, content.betAmount)

        return when (this.table.phaseStatus().also { log.debug("Phase status is: $it") }) {
            PhaseStatus.ROUND_OVER -> {
                roundOver().also { log.debug("Table round is over") }
            }
            PhaseStatus.READY_FOR_SHOWDOWN -> {
                this.table.startShowdown()
                        .also { log.debug("Table in showdown mode") }
                        .also { this.table = it }
            }
            PhaseStatus.READY_FOR_NEXT -> {
                startPhase().also { log.debug("Table phase is over") }
            }
            PhaseStatus.KEEP_GOING -> {
                log.debug("Finding next player to bet")
                this.table.nextPlayerToBet()
                        .also { this.table = it }
            }
        }
    }

    fun roundOver(): PokerTable {
        log.debug("Performing round-over operation")
        return table.roundOver()
                .also { this.table = it }
    }

    fun connectPlayer(): Player {
        return numberedPlayer()
                .also { log.debug("Connecting new player") }
                .also { this.table = this.table.addPlayer(it) }

    }

    fun disconnectPlayer(playerNumber: PlayerNumber): PokerTable {
        log.debug("Disconnecting player: $playerNumber")
        return table.playerLeft(playerNumber)
    }

    private fun numberedPlayer(): Player {
        return table.latestPlayer()
                .map { it.number }
                .map { number -> number + 1 }
                .map { Player.numbered(PlayerNumber.of(it)) }
                .getOrElse { Player.numbered(PlayerNumber.of(0)) }
    }
}
