package com.tp.holdem.server

import com.esotericsoftware.kryonet.Server
import com.tp.holdem.logic.game.GameHandler
import com.tp.holdem.logic.game.GameParams

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val server = Server()
        val sender = MessageSender(server)

        val gameParams = GameParams(
                bigBlindAmount = 40,
                smallBlindAmount = 20,
                playerCount = 3,
                port = 54555,
                startingChips = 1500
        )

        val gameHandler = GameHandler(gameParams)

        val listener = HoldemServerListener(sender, gameParams, gameHandler)

        val gameServer = KryoServer(server, gameParams, listener)
        gameServer.start()
    }
}
