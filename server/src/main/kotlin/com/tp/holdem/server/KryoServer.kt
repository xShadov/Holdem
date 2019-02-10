package com.tp.holdem.server

import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import com.tp.holdem.common.message.*
import com.tp.holdem.common.message.dto.CardDTO
import com.tp.holdem.common.message.dto.CurrentPlayerDTO
import com.tp.holdem.common.message.dto.PlayerDTO
import com.tp.holdem.common.message.dto.PokerTableDTO
import com.tp.holdem.common.model.Honour
import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import com.tp.holdem.common.model.Suit
import com.tp.holdem.game.GameParams
import java.util.concurrent.atomic.AtomicInteger

internal class KryoServer(
        private val server: Server,
        private val params: GameParams,
        private val serverVavrListener: Listener

) : Runnable {
    private val port: Int = params.port

    init {
        val registerCount = AtomicInteger(16)

        val kryo = server.kryo
        kryo.register(ArrayList::class.java, registerCount.getAndIncrement())
        kryo.register(List::class.java, registerCount.getAndIncrement())
        kryo.register(Honour::class.java, registerCount.getAndIncrement())
        kryo.register(Suit::class.java, registerCount.getAndIncrement())
        kryo.register(Phase::class.java, registerCount.getAndIncrement())
        kryo.register(PlayerDTO::class.java, registerCount.getAndIncrement())
        kryo.register(CurrentPlayerDTO::class.java, registerCount.getAndIncrement())
        kryo.register(CardDTO::class.java, registerCount.getAndIncrement())
        kryo.register(PokerTableDTO::class.java, registerCount.getAndIncrement())

        kryo.register(Message::class.java, registerCount.getAndIncrement())
        kryo.register(MessageType::class.java, registerCount.getAndIncrement())
        kryo.register(PlayerConnectMessage::class.java, registerCount.getAndIncrement())
        kryo.register(UpdateStateMessage::class.java, registerCount.getAndIncrement())
        kryo.register(Moves::class.java, registerCount.getAndIncrement())
        kryo.register(PlayerActionMessage::class.java, registerCount.getAndIncrement())
    }

    fun start() {
        server.addListener(serverVavrListener)

        server.bind(port)
        server.start()

        Thread(this).start()
    }

    @Synchronized
    override fun run() {
        while (true) {
            Thread.sleep(500)
        }
    }
}