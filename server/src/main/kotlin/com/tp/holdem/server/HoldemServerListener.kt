package com.tp.holdem.server

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.message.Message
import com.tp.holdem.common.message.MessageType
import com.tp.holdem.common.message.PlayerActionMessage
import com.tp.holdem.common.message.PlayerConnectMessage
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.utils.toCurrentPlayerDTO
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.number
import com.tp.holdem.logic.table.currentPhase
import com.tp.holdem.logic.table.currentlyBetting
import com.tp.holdem.logic.table.inShowdownMode

internal class HoldemServerListener(
        private val sender: MessageSender,
        private val params: GameParams,
        private val gameHandler: GameHandler
) : Listener() {
    private var connectedPlayers = ConnectedPlayers.empty()
    private var expectActionFrom = PlayerNumber.empty()

    private val log by lazyLogger()

    @Synchronized
    override fun received(connection: Connection, message: Any) {
        if (message is Message) {
            log.debug("Received message from ${connection.id}: $message")

            if (connection.id != expectActionFrom.number)
                throw IllegalStateException("Unexpected player sent action")

            val playerNumber = connectedPlayers.getConnected(connection.id)
                    .getOrElseThrow { IllegalStateException("Player not found") }

            if (message.messageType == MessageType.PLAYER_ACTION) {
                log.debug("Received action of type ${message.messageType} from player $playerNumber")

                val content = message.instance(PlayerActionMessage::class.java)

                var response = gameHandler.handlePlayerMove(playerNumber, content)

                if (response.inShowdownMode()) {
                    response = handleShowdown(response)
                }

                sender.sendStateUpdate(connectedPlayers, response)
                        .also { expectActionFrom = findExpectedResponder(response) }

                if (response.currentPhase() == Phase.OVER) {
                    waitAndStartNewRound()
                }
            }
        }
    }

    private fun handleShowdown(table: PokerTable): PokerTable {
        var response = table
        log.debug("Starting showdown")

        while (response.currentPhase() != Phase.RIVER) {
            response = gameHandler.startPhase()
                    .also { sender.sendStateUpdate(connectedPlayers, response) }
                    .also { expectActionFrom = findExpectedResponder(response) }
                    .also { Thread.sleep(1500) }
        }

        return gameHandler.roundOver()
    }

    private fun waitAndStartNewRound() {
        log.debug("Current round is over, sleeping for 5s and staring new round")

        Thread.sleep(5000)
                .also { startRound() }
    }

    @Synchronized
    override fun connected(con: Connection) {
        log.debug("Connection attempt: ${con.id}")

        val connected = tryConnectingPlayer(con)

        if (!connected) {
            return log.debug("Could not connect player")
        }

        if (enoughPlayers()) {
            log.debug("Enough players connected, starting game")
            startGame()
        }
    }

    @Synchronized
    override fun disconnected(con: Connection) {
        log.debug("Disconnection attempt: ${con.id}")

        if (!connectedPlayers.isConnected(con.id))
            throw IllegalStateException("Player is not connected")

        connectedPlayers.getConnected(con.id)
                .map(gameHandler::disconnectPlayer)
                .forEach { table -> sender.sendStateUpdate(connectedPlayers, table) }
    }

    private fun tryConnectingPlayer(con: Connection): Boolean {
        if (enoughPlayers()) {
            return sender.sendSingle(con.id, Message.from(MessageType.PLAYER_CONNECTION, PlayerConnectMessage.failure()))
                    .also { log.debug("Already enough players, could not connect") }
                    .let { false }
        }

        if (connectedPlayers.isConnected(con.id))
            throw IllegalStateException("Player already connected")

        return gameHandler.connectPlayer()
                .also { connectedPlayers = connectedPlayers.connect(con.id, it.number()) }
                .also { log.debug("Connected player: ${it.number()}") }
                .also { sender.sendSingle(con.id, Message.from(MessageType.PLAYER_CONNECTION, PlayerConnectMessage.success(it.toCurrentPlayerDTO()))) }
                .let { true }
    }

    private fun startGame() {
        log.debug("Staring new game")

        gameHandler.startGame()
                .also { sender.sendStateUpdate(connectedPlayers, it) }
                .also { expectActionFrom = findExpectedResponder(it) }
    }

    private fun startRound() {
        log.debug("Starting new round")

        gameHandler.startRound()
                .also { sender.sendStateUpdate(connectedPlayers, it) }
                .also { expectActionFrom = findExpectedResponder(it) }
    }

    private fun findExpectedResponder(response: PokerTable): PlayerNumber =
            when {
                response.currentPhase() == Phase.OVER -> PlayerNumber.empty()
                else -> PlayerNumber.of(connectedPlayers.getConnectionId(response.currentlyBetting()).getOrElse(-1))
            }

    private fun enoughPlayers(): Boolean = connectedPlayers.size() >= params.playerCount
}
