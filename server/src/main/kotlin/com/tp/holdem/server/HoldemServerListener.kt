package com.tp.holdem.server

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.message.Message
import com.tp.holdem.common.message.MessageType
import com.tp.holdem.common.message.PlayerActionMessage
import com.tp.holdem.common.message.PlayerConnectMessage
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.extensions.toCurrentPlayerDTO
import com.tp.holdem.model.PokerTable

internal class HoldemServerListener(
        private val sender: MessageSender,
        private val params: GameParams,
        private val gameHandler: GameHandler
) : Listener() {
    private var connectedPlayers = ConnectedPlayers.empty()
    private var expectActionFrom = -1

    private val log by lazyLogger()

    @Synchronized
    override fun received(connection: Connection, message: Any) {
        if (message is Message) {
            log.debug("Received message from ${connection.id}: ${message}")

            if (connection.id != expectActionFrom)
                throw IllegalStateException("Unexpected player sent action")

            val playerNumber = connectedPlayers.getConnected(connection.id)
                    .getOrElseThrow { IllegalStateException("Player not found") }

            if (message.messageType == MessageType.PLAYER_ACTION) {
                log.debug("Received action of type ${message.messageType} from player $playerNumber")

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

            Thread.sleep(1500)
        }

        return gameHandler.roundOver()
    }

    private fun waitAndStartNewRound() {
        log.debug("Current round is over, sleeping for 5s and staring new round")

        Thread.sleep(5000)

        startRound()
    }

    @Synchronized
    override fun connected(con: Connection) {
        log.debug("Connection attempt: ${con.id}")

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
        log.debug("Disconnection attempt: ${con.id}")

        if (!connectedPlayers.isConnected(con.id))
            throw IllegalStateException("Player is not connected")

        connectedPlayers.getConnected(con.id)
                .map(gameHandler::disconnectPlayer)
                .forEach { table -> sender.sendStateUpdate(connectedPlayers, table) }
    }

    private fun tryConnectingPlayer(con: Connection): Boolean {
        if (enoughPlayers()) {
            log.debug("Already enough players, could not connect")

            sender.sendSingle(con.id, Message.from(MessageType.PLAYER_CONNECTION, PlayerConnectMessage.failure()))
            return false
        }

        if (connectedPlayers.isConnected(con.id))
            throw IllegalStateException("Player already connected")

        val player = gameHandler.connectPlayer()
        connectedPlayers = connectedPlayers.connect(con.id, player.number)

        log.debug("Connected player: ${player.number}")

        sender.sendSingle(con.id, Message.from(MessageType.PLAYER_CONNECTION, PlayerConnectMessage.success(player.toCurrentPlayerDTO())))
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

    private fun findExpectedResponder(response: PokerTable): Int =
            when {
                response.phase == Phase.OVER -> -1
                else -> connectedPlayers.getConnectionId(response.bettingPlayer).getOrElse(-1)
            }

    private fun enoughPlayers(): Boolean = connectedPlayers.size() >= params.playerCount
}
