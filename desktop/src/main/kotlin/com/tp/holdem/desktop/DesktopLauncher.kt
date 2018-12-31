package com.tp.holdem.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.tp.holdem.client.Holdem

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.title = "HoldemTP"
        config.width = 1024
        config.height = 700

        LwjglApplication(Holdem(), config)
    }
}
