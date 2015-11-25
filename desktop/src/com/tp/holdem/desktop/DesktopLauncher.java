package com.tp.holdem.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tp.holdem.Deck;
import com.tp.holdem.Holdem;
import com.tp.holdem.KryoClient;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "HoldemTP";
		config.width = 1024;
		config.height = 780;
		new LwjglApplication(new Holdem(), config);
	}
}
