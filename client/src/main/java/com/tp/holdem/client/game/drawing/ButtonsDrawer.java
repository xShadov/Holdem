package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.common.message.dto.PlayerDTO;

import java.util.concurrent.atomic.AtomicInteger;

public class ButtonsDrawer {
	private static final int[] dealerPositionX = {448, 276, 210, 228, 303, 477, 660, 736, 738, 666};
	private static final int[] dealerPositionY = {237, 243, 330, 442, 502, 499, 516, 429, 313, 244};
	private static final int[] blindPositionX = {490, 315, 213, 246, 340, 519, 630, 748, 763, 637};
	private static final int[] blindPositionY = {235, 234, 369, 469, 540, 520, 532, 466, 342, 237};

	private final SpriteBatch batcher;
	private final GameState gameState;
	private final TextureAtlas commonTextures;

	private final TextureRegion dealer;
	private final TextureRegion smallBlind;
	private final TextureRegion bigBlind;

	public ButtonsDrawer(SpriteBatch batcher, GameState gameState, TextureAtlas commonTextures) {
		this.batcher = batcher;
		this.gameState = gameState;
		this.commonTextures = commonTextures;

		this.dealer = getRegion("dealer");
		this.smallBlind = getRegion("smallBlind");
		this.bigBlind = getRegion("bigBlind");
	}

	public void drawButtons() {
		final AtomicInteger drawCount = new AtomicInteger(0);
		gameState.getAllPlayers().forEach(player -> drawPlayer(drawCount.getAndIncrement(), player));
	}

	private void drawPlayer(int index, PlayerDTO player) {
		if (gameState.getTable().getDealer().getNumber() == player.getNumber())
			batcher.draw(dealer, dealerPositionX[index], dealerPositionY[index]);
		if (gameState.getTable().getBigBlind().getNumber() == player.getNumber())
			batcher.draw(bigBlind, blindPositionX[index], blindPositionY[index]);
		if (gameState.getTable().getSmallBlind().getNumber() == player.getNumber())
			batcher.draw(smallBlind, blindPositionX[index], blindPositionY[index]);
	}

	private TextureRegion getRegion(String code) {
		final TextureRegion region = commonTextures.findRegion(code);

		final Sprite sprite = new Sprite(region);
		sprite.setPosition(50, 50);
		return sprite;
	}
}
