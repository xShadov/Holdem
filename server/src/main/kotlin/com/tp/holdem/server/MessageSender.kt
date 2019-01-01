package com.tp.holdem.server

import com.esotericsoftware.kryonet.Server
import com.tp.holdem.common.lazyLogger
import com.tp.holdem.common.message.Message
import com.tp.holdem.common.message.MessageType
import com.tp.holdem.common.message.UpdateStateMessage
import com.tp.holdem.logic.players.byNumber
import com.tp.holdem.logic.table.playerNames
import com.tp.holdem.logic.utils.toCurrentPlayerDTO
import com.tp.holdem.logic.utils.toDTO
import com.tp.holdem.model.PokerTable

internal class MessageSender(private val server: Server) {
    private val log by lazyLogger()

    fun sendStateUpdate(connectedPlayers: ConnectedPlayers, table: PokerTable) {
        log.debug("Sending state update to everyone")

        val message = UpdateStateMessage.builder()
                .table(table.toDTO())
                .build()

        connectedPlayers.forEach { connection: Int, playerNumber: Int ->
            val currentPlayerDTO = table.allPlayers
                    .byNumber(playerNumber)
                    .toCurrentPlayerDTO()
                    .also { log.debug("Sending message to player: ${it.name}") }

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
