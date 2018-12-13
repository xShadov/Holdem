package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.client.model.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class ButtonsDrawer {
	private static final int[] dealerPositionX = {448, 276, 210, 228, 303, 477, 660, 736, 738, 666};
	private static final int[] dealerPositionY = {237, 243, 330, 442, 502, 499, 516, 429, 313, 244};
	private static final int[] blindPositionX = {490, 315, 213, 246, 340, 519, 630, 748, 763, 637};
	private static final int[] blindPositionY = {235, 234, 369, 469, 540, 520, 532, 466, 342, 237};

	private final TextureRegion dealer = new TextureRegion(new Texture("data/dealer.png"), 0, 0, 50, 48);
	private final TextureRegion smallBlind = new TextureRegion(new Texture("data/smallBlind.png"), 0, 0, 35, 32);
	private final TextureRegion bigBlind = new TextureRegion(new Texture("data/bigBlind.png"), 0, 0, 36, 34);

	private final SpriteBatch batcher;
	private final GameState gameState;

	public ButtonsDrawer(SpriteBatch batcher, GameState gameState) {
		this.batcher = batcher;
		this.gameState = gameState;
	}

	public void drawButtons() {
		final AtomicInteger drawCount = new AtomicInteger(0);
		gameState.getAllPlayers().forEach(player -> drawPlayer(drawCount.getAndIncrement(), player));
	}

	private void drawPlayer(int index, Player player) {
		if (player.isHasDealerButton())
			batcher.draw(dealer, dealerPositionX[index], dealerPositionY[index]);
		if (player.isHasBigBlind())
			batcher.draw(bigBlind, blindPositionX[index], blindPositionY[index]);
		if (player.isHasSmallBlind())
			batcher.draw(smallBlind, blindPositionX[index], blindPositionY[index]);
	}
}
