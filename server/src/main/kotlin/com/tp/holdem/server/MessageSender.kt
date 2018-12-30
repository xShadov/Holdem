package com.tp.holdem.server

import com.esotericsoftware.kryonet.Server
import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.logger
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.toCurrentPlayerDTO
import com.tp.holdem.logic.toDTO
import com.tp.holdem.common.message.Message
import com.tp.holdem.common.message.MessageType
import com.tp.holdem.common.message.UpdateStateMessage

internal class MessageSender(private val server: Server) {
    private val log by lazyLogger()

    fun sendStateUpdate(connectedPlayers: ConnectedPlayers, table: PokerTable) {
        log.debug(String.format("Sending state update to everyone"))

        val message = UpdateStateMessage.builder()
                .table(table.toDTO())
                .build()

        log.debug(String.format("Players on poker table: %s", table.allPlayers.map { it.number }))
        connectedPlayers.forEach { connection, playerNumber ->
            log.debug(String.format("Sending message to player: %d", playerNumber))

            val currentPlayerDTO = table.allPlayers
                    .find { (number) -> number == playerNumber }
                    .map { it.toCurrentPlayerDTO() }
                    .getOrElseThrow { IllegalStateException("Player not found") }

            val modifiedResponse = message.toBuilder()
                    .currentPlayer(currentPlayerDTO)
                    .build()

            server.sendToTCP(connection!!, Message.from(MessageType.UPDATE_STATE, modifiedResponse))
        }
    }

    fun sendSingle(connectionId: Int, message: Message) {
        log.debug(String.format("Sending message %s to single connection: %d", message.messageType, connectionId))

        server.sendToTCP(connectionId, message)
    }
}
