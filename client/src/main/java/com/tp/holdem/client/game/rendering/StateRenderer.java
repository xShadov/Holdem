package com.tp.holdem.client.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.client.game.drawing.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateRenderer implements Renderer {
	private final CardDrawer cardDrawer;
	private final TableDrawer tableDrawer;
	private final ChipsDrawer chipsDrawer;
	private final InfoBoxDrawer infoBoxDrawer;
	private final ButtonsDrawer buttonsDrawer;
	private final SpotlightDrawer spotlightDrawer;

	private final GameState gameState;

	private final SpriteBatch batcher;
	private final BitmapFont mediumFont, bigFont;

	public StateRenderer(GameState gameState, SpriteBatch batcher) {
		this.batcher = batcher;

		mediumFont = Fonts.builder().type(Fonts.Types.JMH).size(30).color(Color.WHITE).generate();
		bigFont = Fonts.builder().type(Fonts.Types.JMH).size(45).color(Color.WHITE).generate();

		final TextureAtlas cardTextures = new TextureAtlas(Gdx.files.internal("data/cards.pack"));
		final TextureAtlas commonTextures = new TextureAtlas(Gdx.files.internal("data/common.pack"));

		this.gameState = gameState;
		this.cardDrawer = new CardDrawer(batcher, gameState, cardTextures);
		this.tableDrawer = new TableDrawer(batcher);
		this.chipsDrawer = new ChipsDrawer(batcher, gameState, commonTextures);
		this.infoBoxDrawer = new InfoBoxDrawer(batcher, gameState, commonTextures);
		this.buttonsDrawer = new ButtonsDrawer(batcher, gameState, commonTextures);
		this.spotlightDrawer = new SpotlightDrawer(batcher, gameState);
	}

	@Override
	public void render(final float delta, final float runTime) {
		drawGameState();
	}

	private void drawGameState() {
		tableDrawer.drawTable();

		if (gameState.isCurrentPlayerWaiting())
			mediumFont.draw(batcher, "Waiting for all players", 300, 500);

		if (gameState.isGameStarted()) {
			spotlightDrawer.drawSpotlight();

			cardDrawer.drawCards();

			buttonsDrawer.drawButtons();

			infoBoxDrawer.drawInfoBoxes();

			chipsDrawer.drawChips();
		}

		if (gameState.hasWinner()) {
			if (gameState.isCurrentPlayerWinner())
				bigFont.draw(batcher, "YOU WIN!", 320, 550);
			else
				bigFont.draw(batcher, String.format("%s WON!", gameState.getWinnerPlayer().getName()), 320, 550);
		}
	}
}
