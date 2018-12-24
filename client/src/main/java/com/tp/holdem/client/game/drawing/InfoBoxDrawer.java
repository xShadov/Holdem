package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.google.common.collect.Maps;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.model.game.Player;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class InfoBoxDrawer {
	private static final int[] boxPositionX = {364, 136, 21, 45, 168, 405, 597, 777, 813, 678};
	private static final int[] boxPositionY = {120, 112, 301, 484, 616, 603, 612, 498, 227, 117};

	private final SpriteBatch batcher;
	private final GameState gameState;
	private final TextureAtlas commonTextures;

	private final TextureRegion box;
	private final TextureRegion boxOff;
	private final TextureRegion boxFold;
	private final BitmapFont smallFont;

	private final BiFunction<Boolean, Boolean, TextureRegion> boxFinder;

	public InfoBoxDrawer(SpriteBatch batcher, GameState gameState, TextureAtlas commonTextures) {
		this.batcher = batcher;
		this.gameState = gameState;
		this.commonTextures = commonTextures;

		this.box = getRegion("infoBox");
		this.boxOff = getRegion("infoBoxOff");
		this.boxFold = getRegion("infoBoxFold");

		this.smallFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		this.smallFont.getData().setScale(.4f);

		this.boxFinder = (inGame, folded) -> {
			if (inGame && !folded)
				return box;
			if (inGame && folded)
				return boxFold;
			return boxOff;
		};
	}

	public void drawInfoBoxes() {
		final AtomicInteger drawCount = new AtomicInteger(0);
		gameState.getAllPlayers().forEach(player -> drawPlayer(drawCount.getAndIncrement(), player));
	}

	private void drawPlayer(int index, Player player) {
		batcher.draw(boxFinder.apply(player.isInGame(), player.isFolded()), boxPositionX[index], boxPositionY[index]);
		smallFont.draw(batcher, player.getName(), boxPositionX[index] + 18, boxPositionY[index] + 81);
		smallFont.draw(batcher, String.format("Chips:%d", player.getChipsAmount()), boxPositionX[index] + 18, boxPositionY[index] + 54);
		smallFont.draw(batcher, String.format("Bet:%d/%d", player.getBetAmount(), player.getChipsAmount()), boxPositionX[index] + 18, boxPositionY[index] + 27);
	}

	private TextureRegion getRegion(String code) {
		final TextureRegion region = commonTextures.findRegion(code);

		final Sprite sprite = new Sprite(region);
		sprite.setPosition(160, 100);
		return sprite;
	}
}
