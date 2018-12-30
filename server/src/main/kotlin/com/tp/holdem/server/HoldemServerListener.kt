package com.tp.holdem.server

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.tp.holdem.common.lazyLogger
import com.tp.holdem.model.PokerTable
import com.tp.holdem.logic.extensions.toCurrentPlayerDTO
import com.tp.holdem.common.model.Phase
import com.tp.holdem.common.message.Message
import com.tp.holdem.common.message.MessageType
import com.tp.holdem.common.message.PlayerActionMessage
import com.tp.holdem.common.message.PlayerConnectMessage
import com.tp.holdem.logic.extensions.getBettingPlayer

internal class HoldemServerListener(
        private val sender: MessageSender,
        private val params: GameParams,
        private val gameHandler: GameHandler
) : Listener() {
    private var connectedPlayers = ConnectedPlayers.empty()
    private var expectActionFrom = -1

    private val log by lazyLogger()

    @Synchronized
    override fun received(connection: Connection, resp: Any) {
        if (resp is Message) {
            log.debug(String.format("Received message from %d: %s", connection.id, resp))

            if (connection.id != expectActionFrom)
                throw IllegalStateException("Unexpected player sent action")

            val playerNumber = connectedPlayers.getConnected(connection.id)
                    .getOrElseThrow { IllegalStateException("Player not found") }

            val message = resp

            if (message.messageType == MessageType.PLAYER_ACTION) {
                log.debug(String.format("Received action of type %s from player %d", message.messageType, playerNumber))

                val content = message.instance(PlayerActionMessage::class.java)

                var response = gameHandler.handlePlayerMove(playerNumber, content)

                if (response.showdown) {
                    response = handleShowdown(response)
                }

                sender.sendStateUpdate(connectedPlayers, response)
                expectActionFrom = findExpectedResponder(response)

                if (response.phase == Phase.OVER) {
                    waitAndStartNewRound()
                }
            }
        }
    }

    private fun handleShowdown(table: PokerTable): PokerTable {
        var response = table
        log.debug("Starting showdown")

        while (response.phase != Phase.RIVER) {
            response = gameHandler.startPhase()

            sender.sendStateUpdate(connectedPlayers, response)
            expectActionFrom = findExpectedResponder(response)

            try {
                Thread.sleep(1500)
            } catch (ex: InterruptedException) {
                throw RuntimeException(ex)
            }

        }

        return gameHandler.roundOver()
    }

    private fun waitAndStartNewRound() {
        log.debug("Current round is over, sleeping for 5s and staring new round")

        try {
            Thread.sleep(5000)
        } catch (ex: InterruptedException) {
            throw RuntimeException(ex)
        }

        startRound()
    }

    @Synchronized
    override fun connected(con: Connection) {
        log.debug(String.format("Connection attempt: %d", con.id))

        val connected = tryConnectingPlayer(con)

        if (!connected) {
            log.debug("Could not connect player")
            return
        }

        if (enoughPlayers()) {
            log.debug("Enough players connected, starting game")
            startGame()
        }
    }

    @Synchronized
    override fun disconnected(con: Connection) {
        log.debug(String.format("Dis-connection attempt: %d", con.id))

        if (!connectedPlayers.isConnected(con.id))
            throw IllegalStateException("Player is not connected")

        connectedPlayers.getConnected(con.id)
                .map { gameHandler.disconnectPlayer(it) }
                .forEach { table -> sender.sendStateUpdate(connectedPlayers, table) }
    }

    private fun tryConnectingPlayer(con: Connection): Boolean {
        if (enoughPlayers()) {
            log.debug("Already enough players, could not connect")

            val response = PlayerConnectMessage.failure()
            sender.sendSingle(con.id, Message.from(MessageType.PLAYER_CONNECTION, response))
            return false
        }

        if (connectedPlayers.isConnected(con.id))
            throw IllegalStateException("Player already connected")

        val player = gameHandler.connectPlayer()
        connectedPlayers = connectedPlayers.connect(con.id, player.number)

        log.debug(String.format("Connected player: %d", player.number))

        val connectionResponse = PlayerConnectMessage.success(player.toCurrentPlayerDTO())
        sender.sendSingle(con.id, Message.from(MessageType.PLAYER_CONNECTION, connectionResponse))
        return true
    }

    private fun startGame() {
        log.debug("Staring new game")

        val response = gameHandler.startGame()
        sender.sendStateUpdate(connectedPlayers, response)
        expectActionFrom = findExpectedResponder(response)
    }

    private fun startRound() {
        log.debug("Starting new round")

        val response = gameHandler.startRound()
        sender.sendStateUpdate(connectedPlayers, response)
        expectActionFrom = findExpectedResponder(response)
    }

    private fun findExpectedResponder(response: PokerTable): Int {
        return if (response.phase == Phase.OVER) -1 else response
                .getBettingPlayer()
                .map { it.number }
                .flatMap { connectedPlayers.getConnectionId(it) }
                .getOrElse(-1)

    }

    private fun enoughPlayers(): Boolean {
        return connectedPlayers.size() >= params.playerCount
    }
}
