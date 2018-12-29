package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.Maps;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.model.message.dto.CardDTO;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CardDrawer {
	private static final int[] tableCardsPositionX = {275, 367, 459, 551, 643};
	private static final int tableCardsPositionY = 360;

	private static final int[] positionX = {539, 163, 64, 79, 210, 442, 637, 816, 828, 708};
	private static final int[] positionY = {113, 101, 294, 497, 632, 617, 628, 512, 293, 107};
	private static final int offsetX = 20;
	private static final int offsetY = -15;

	private final SpriteBatch batcher;
	private final GameState gameState;
	private final TextureAtlas cardTextures;
	private final TextureRegion cardReverse;
	private final Map<String, TextureAtlas.AtlasRegion> cardRegionMap = Maps.newHashMap();

	public CardDrawer(SpriteBatch batcher, GameState gameState, TextureAtlas textures) {
		this.batcher = batcher;
		this.gameState = gameState;
		this.cardTextures = textures;
		this.cardReverse = getRegion("red_back");
	}

	public void drawCards() {
		drawPlayersCards();
		drawTableCards();
	}

	private void drawTableCards() {
		AtomicInteger drawCount = new AtomicInteger(0);
		gameState.getCardsOnTable().forEach(card -> drawCard(card, tableCardsPositionX[drawCount.getAndIncrement()], tableCardsPositionY));
	}

	private void drawPlayersCards() {
		final AtomicInteger drawnCount = new AtomicInteger(0);

		drawCard(gameState.getCurrentPlayer().getHand().get(0), positionX[drawnCount.get()], positionY[drawnCount.get()]);
		drawCard(gameState.getCurrentPlayer().getHand().get(1), positionX[drawnCount.get()] + offsetX, positionY[drawnCount.get()] + offsetY);

		drawnCount.incrementAndGet();

		gameState.getOtherPlayers()
				.forEach(player -> {
					drawReverse(positionX[drawnCount.get()], positionY[drawnCount.get()]);
					drawReverse(positionX[drawnCount.get()] + offsetX, positionY[drawnCount.get()] + offsetY);

					drawnCount.incrementAndGet();
				});
	}

	private void drawCard(CardDTO card, int x, int y) {
		final Sprite sprite = findCurrentCardTexture(card);
		sprite.setScale(0.25f);
		sprite.setOrigin(0, 0);
		sprite.setOriginBasedPosition(x, y);
		sprite.draw(batcher);
	}

	private void drawReverse(int x, int y) {
		final Sprite sprite = new Sprite(cardReverse);
		sprite.setScale(0.25f);
		sprite.setOrigin(0, 0);
		sprite.setOriginBasedPosition(x, y);
		sprite.draw(batcher);
	}

	private Sprite findCurrentCardTexture(final CardDTO card) {
		return getRegion(card.code());
	}

	private Sprite getRegion(String code) {
		TextureAtlas.AtlasRegion region = cardRegionMap.get(code);

		if (region == null) {
			region = cardTextures.findRegion(code);
			cardRegionMap.put(code, region);
		}

		final Sprite sprite = new Sprite(region);
		sprite.setPosition(50, 50);
		return sprite;
	}
}
