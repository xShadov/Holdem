package com.tp.holdem.server

import com.esotericsoftware.kryonet.Server
import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.message.Message
import com.tp.holdem.common.message.MessageType
import com.tp.holdem.common.message.UpdateStateMessage
import com.tp.holdem.logic.extensions.byNumber
import com.tp.holdem.logic.extensions.playerNames
import com.tp.holdem.logic.extensions.toCurrentPlayerDTO
import com.tp.holdem.logic.extensions.toDTO
import com.tp.holdem.model.PokerTable

internal class MessageSender(private val server: Server) {
    private val log by lazyLogger()

    fun sendStateUpdate(connectedPlayers: ConnectedPlayers, table: PokerTable) {
        log.debug("Sending state update to everyone")

        val message = UpdateStateMessage.builder()
                .table(table.toDTO())
                .build()

        log.debug("Players on poker table: ${table.playerNames()}")
        connectedPlayers.forEach { connection: Int, playerNumber: Int ->
            log.debug("Sending message to player: $playerNumber")

            val currentPlayerDTO = table.allPlayers
                    .byNumber(playerNumber)
                    .toCurrentPlayerDTO()

            val modifiedResponse = message.toBuilder()
                    .currentPlayer(currentPlayerDTO)
                    .build()

            server.sendToTCP(connection, Message.from(MessageType.UPDATE_STATE, modifiedResponse))
        }
    }

    fun sendSingle(connectionId: Int, message: Message) {
        log.debug("Sending message ${message.messageType} to single connection: $connectionId")

        server.sendToTCP(connectionId, message)
    }
}
