package com.tp.holdem;

public class GameWorld {

	private float runTime = 0;
	private GameRenderer renderer;
    private float timeChecker;
	public void update(float delta) {
        runTime += delta;
        timeChecker+=delta;
        if(timeChecker>10){
        	timeChecker = 0;
        }
    }
	
	public void setRenderer(GameRenderer renderer) {
        this.renderer = renderer;
    }
}
