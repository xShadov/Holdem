package com.tp.holdem.client.game.drawing;

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
	private static final int[] tableCardsPositionX = {315, 397, 479, 561, 643};
	private static final int tableCardsPositionY = 360;

	private static final int[] positionX = {529, 163, 64, 79, 210, 442, 637, 816, 828, 708};
	private static final int[] positionY = {133, 121, 314, 497, 632, 617, 628, 512, 293, 127};
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
		gameState.getCardsOnTable().forEach(card -> batcher.draw(findCurrentCardTexture(card), tableCardsPositionX[drawCount.getAndIncrement()], tableCardsPositionY));
	}

	private void drawPlayersCards() {
		final AtomicInteger drawnCount = new AtomicInteger(0);

		batcher.draw(
				findCurrentCardTexture(gameState.getCurrentPlayer().getHand().get(0)),
				positionX[drawnCount.get()],
				positionY[drawnCount.get()]
		);
		batcher.draw(
				findCurrentCardTexture(gameState.getCurrentPlayer().getHand().get(1)),
				positionX[drawnCount.get()] + offsetX,
				positionY[drawnCount.get()] + offsetY
		);

		drawnCount.incrementAndGet();

		gameState.getOtherPlayers()
				.forEach(player -> {
					batcher.draw(cardReverse, positionX[drawnCount.get()], positionY[drawnCount.get()]);
					batcher.draw(cardReverse, positionX[drawnCount.get()] + offsetX, positionY[drawnCount.get()] + offsetY);

					drawnCount.incrementAndGet();
				});
	}

	private TextureRegion findCurrentCardTexture(final CardDTO card) {
		return getRegion(card.code());
	}

	private TextureRegion getRegion(String code) {
		TextureAtlas.AtlasRegion region = cardRegionMap.get(code);

		if (region == null) {
			region = cardTextures.findRegion(code);
			cardRegionMap.put(code, region);
		}

		final Sprite sprite = new Sprite(region);
		sprite.setPosition(69, 94);
		return sprite;
	}
}
