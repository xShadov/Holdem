package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tp.holdem.client.game.GameState;

public class SpotlightDrawer {
	private static final int[] boxPositionX = {274, 46, -69, -45, 78, 315, 507, 687, 723, 588};
	private static final int[] boxPositionY = {92, 84, 273, 456, 588, 575, 584, 470, 199, 89};

	private final TextureRegion spotlight = new TextureRegion(new Texture(Gdx.files.internal("data/spotlight.png")), 0, 0, 352, 740);

	private final SpriteBatch batcher;
	private final GameState gameState;

	public SpotlightDrawer(SpriteBatch batcher, GameState gameState) {
		this.batcher = batcher;
		this.gameState = gameState;
	}

	public void drawSpotlight() {
		gameState.getWinnerPlayer().orElse(gameState.getBettingPlayer())
				.map(gameState::relativePlayerNumber)
				.forEach(number -> batcher.draw(spotlight, boxPositionX[number], boxPositionY[number]));
	}
}
