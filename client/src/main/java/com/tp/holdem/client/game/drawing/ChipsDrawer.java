package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.tp.holdem.client.game.GameState;

import java.util.concurrent.atomic.AtomicInteger;

public class ChipsDrawer {
	private static final int[] chipsPositionX = {507, 313, 274, 286, 358, 538, 631, 705, 738, 636};
	private static final int[] chipsPositionY = {273, 288, 374, 441, 501, 484, 484, 451, 370, 280};

	private final TextureRegion smallStack = new TextureRegion(new Texture("data/smallStack.png"), 0, 0, 28, 28);
	private final TextureRegion semiStack = new TextureRegion(new Texture("data/semiStack.png"), 0, 0, 28, 30);
	private final TextureRegion bigStack = new TextureRegion(new Texture("data/bigStack.png"), 0, 0, 31, 33);

	private final RangeMap<Integer, TextureRegion> amountToTexture = ImmutableRangeMap.<Integer, TextureRegion>builder()
			.put(Range.open(0, 300), smallStack)
			.put(Range.open(300, 1500), semiStack)
			.put(Range.open(1500, Integer.MAX_VALUE), bigStack)
			.build();

	private final SpriteBatch batcher;
	private final GameState gameState;

	public ChipsDrawer(SpriteBatch batcher, GameState gameState) {
		this.batcher = batcher;
		this.gameState = gameState;
	}

	public void drawChips() {
		final AtomicInteger drawCount = new AtomicInteger(0);

		batcher.draw(amountToTexture.get(gameState.getCurrentPlayer().getBetAmount()), chipsPositionX[drawCount.get()], chipsPositionY[drawCount.get()]);

		drawCount.incrementAndGet();

		gameState.getOtherPlayers()
				.forEach(player -> {
					batcher.draw(amountToTexture.get(player.getBetAmount()), chipsPositionX[drawCount.get()], chipsPositionY[drawCount.get()]);
					drawCount.incrementAndGet();
				});
	}
}
