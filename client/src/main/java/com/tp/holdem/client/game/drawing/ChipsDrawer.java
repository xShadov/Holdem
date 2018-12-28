package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.tp.holdem.client.game.GameState;

import java.util.concurrent.atomic.AtomicInteger;

public class ChipsDrawer {
	private static final int[] chipsPositionX = {507, 313, 274, 286, 358, 538, 631, 705, 738, 636};
	private static final int[] chipsPositionY = {273, 288, 374, 441, 501, 484, 484, 451, 370, 280};

	private final SpriteBatch batcher;
	private final GameState gameState;
	private final TextureAtlas commonTextures;

	private final BitmapFont smallFont;
	private final TextureRegion smallStack;
	private final TextureRegion semiStack;
	private final TextureRegion bigStack;
	private final RangeMap<Integer, TextureRegion> amountToTexture;

	public ChipsDrawer(SpriteBatch batcher, GameState gameState, TextureAtlas commonTextures) {
		this.batcher = batcher;
		this.gameState = gameState;
		this.commonTextures = commonTextures;

		this.smallFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		this.smallFont.getData().setScale(.6f);

		this.smallStack = getRegion("smallStack");
		this.semiStack = getRegion("mediumStack");
		this.bigStack = getRegion("bigStack");

		this.amountToTexture = ImmutableRangeMap.<Integer, TextureRegion>builder()
				.put(Range.open(1, 300), smallStack)
				.put(Range.open(300, 1500), semiStack)
				.put(Range.open(1500, Integer.MAX_VALUE), bigStack)
				.build();
	}

	public void drawChips() {
		drawPot();

		final AtomicInteger drawCount = new AtomicInteger(0);
		gameState.getAllPlayers()
				.forEach(player -> {
					if (player.getBetAmountThisPhase() > 0)
						batcher.draw(amountToTexture.get(player.getBetAmountThisPhase()), chipsPositionX[drawCount.get()], chipsPositionY[drawCount.get()]);

					drawCount.incrementAndGet();
				});
	}

	private void drawPot() {
		smallFont.draw(batcher, String.format("Whole pot: %d", gameState.getTable().getPotAmount()), 350, 550);
		smallFont.draw(batcher, String.format("Phase pot: %d", gameState.getTable().getPotAmountThisPhase()), 350, 520);
	}

	private TextureRegion getRegion(String code) {
		final TextureRegion region = commonTextures.findRegion(code);

		final Sprite sprite = new Sprite(region);
		sprite.setPosition(30, 30);
		return sprite;
	}
}
