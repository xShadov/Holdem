package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.model.game.Player;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class InfoBoxDrawer {
	private static final int[] boxPositionX = {364, 136, 21, 45, 168, 405, 597, 777, 813, 678};
	private static final int[] boxPositionY = {120, 112, 301, 484, 616, 603, 612, 498, 227, 117};

	private final TextureRegion box = new TextureRegion(new Texture(Gdx.files.internal("data/infoBox.png")), 0, 0, 160, 96);
	private final TextureRegion boxOff = new TextureRegion(new Texture(Gdx.files.internal("data/infoBoxOff.png")), 0, 0, 160, 96);
	private final TextureRegion boxFold = new TextureRegion(new Texture(Gdx.files.internal("data/infoBoxFold.png")), 0, 0, 160, 96);
	private final BitmapFont smallFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);

	{
		smallFont.getData().setScale(.4f);
	}

	private final BiFunction<Boolean, Boolean, TextureRegion> boxFinder = (inGame, folded) -> {
		if (inGame && !folded)
			return box;
		if (inGame && folded)
			return boxFold;
		return boxOff;
	};

	private final SpriteBatch batcher;
	private final GameState gameState;

	public InfoBoxDrawer(SpriteBatch batcher, GameState gameState) {
		this.batcher = batcher;
		this.gameState = gameState;
	}

	public void drawInfoBoxes() {
		final AtomicInteger drawCount = new AtomicInteger(0);
		gameState.getAllPlayers().forEach(player -> drawPlayer(drawCount.getAndIncrement(), player));
	}

	private void drawPlayer(int index, Player player) {
		batcher.draw(boxFinder.apply(player.isInGame(), player.isFolded()), boxPositionX[index], boxPositionY[index]);
		smallFont.draw(batcher, player.getName(), boxPositionX[index] + 18, boxPositionY[index] + 81);
		smallFont.draw(batcher, "Chips" + player.getChipsAmount(), boxPositionX[index] + 18, boxPositionY[index] + 54);
		smallFont.draw(batcher, "Bet:" + player.getBetAmount() + "/" + player.getChipsAmount(), boxPositionX[index] + 18, boxPositionY[index] + 27);
	}
}
