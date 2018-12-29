package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.client.game.rendering.Fonts;

import java.util.concurrent.atomic.AtomicInteger;

public class ChipsDrawer {
	private static final int[] chipsPositionX = {507, 313, 274, 286, 358, 538, 631, 705, 738, 636};
	private static final int[] chipsPositionY = {273, 288, 374, 441, 501, 484, 484, 451, 370, 280};

	private final SpriteBatch batcher;
	private final GameState gameState;

	private final BitmapFont smallFont;
	private final TextureRegion smallStack;
	private final TextureRegion semiStack;
	private final TextureRegion bigStack;
	private final RangeMap<Integer, TextureRegion> amountToTexture;

	public ChipsDrawer(SpriteBatch batcher, GameState gameState, TextureAtlas commonTextures) {
		this.batcher = batcher;
		this.gameState = gameState;

		this.smallFont = Fonts.builder().type(Fonts.Types.JMH).size(14).color(Color.WHITE).generate();

		this.smallStack = getRegion(commonTextures, "smallStack");
		this.semiStack = getRegion(commonTextures, "mediumStack");
		this.bigStack = getRegion(commonTextures, "bigStack");

		this.amountToTexture = ImmutableRangeMap.<Integer, TextureRegion>builder()
				.put(Range.open(1, 300), smallStack)
				.put(Range.open(300, 1500), semiStack)
				.put(Range.open(1500, Integer.MAX_VALUE), bigStack)
				.build();
	}

	public void drawChips() {
		drawPot();
		drawPlayerBets();
	}

	private void drawPot() {
		final int potAmount = gameState.getPotAmount();
		final int potAmountThisPhase = gameState.getPotAmountThisPhase();

		if (potAmount > 0) {
			final TextureRegion texture = amountToTexture.get(potAmount);
			batcher.draw(texture, 550, 520);
			smallFont.draw(batcher, String.valueOf(potAmount), 550 + texture.getRegionWidth() - 25, 525);
		}

		if (potAmountThisPhase > 0) {
			final TextureRegion texture = amountToTexture.get(potAmountThisPhase);
			batcher.draw(texture, 650, 520);
			smallFont.draw(batcher, String.valueOf(potAmountThisPhase), 650 + texture.getRegionWidth() - 25, 525);
		}
	}

	private void drawPlayerBets() {
		final AtomicInteger drawCount = new AtomicInteger(0);
		gameState.getAllPlayers()
				.forEach(player -> {
					if (player.getBetAmountThisPhase() > 0) {
						final TextureRegion texture = amountToTexture.get(player.getBetAmountThisPhase());
						batcher.draw(texture, chipsPositionX[drawCount.get()], chipsPositionY[drawCount.get()]);
						smallFont.draw(batcher, String.valueOf(player.getBetAmountThisPhase()),
								chipsPositionX[drawCount.get()] + texture.getRegionWidth() - 25,
								chipsPositionY[drawCount.get()]
						);
					}

					drawCount.incrementAndGet();
				});
	}

	private TextureRegion getRegion(TextureAtlas textures, String code) {
		final TextureRegion region = textures.findRegion(code);

		final Sprite sprite = new Sprite(region);
		sprite.setPosition(30, 30);
		return sprite;
	}
}
