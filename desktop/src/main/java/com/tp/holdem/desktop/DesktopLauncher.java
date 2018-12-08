package com.tp.holdem.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tp.holdem.core.Holdem;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "HoldemTP";
		config.width = 1024;
		config.height = 700;
		
		new LwjglApplication(new Holdem(), config);
	}
}
