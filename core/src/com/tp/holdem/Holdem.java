package com.tp.holdem;

import com.badlogic.gdx.Game;

public class Holdem extends Game {
	
	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
