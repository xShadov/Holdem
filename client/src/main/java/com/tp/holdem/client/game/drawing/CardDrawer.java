package com.tp.holdem.client.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tp.holdem.client.game.GameState;
import com.tp.holdem.client.model.Card;
import com.tp.holdem.client.model.CardCoordinates;
import io.vavr.Tuple2;

import java.util.concurrent.atomic.AtomicInteger;

public class CardDrawer {
	private static final int[] tableCardsPositionX = {315, 397, 479, 561, 643};
	private static final int tableCardsPositionY = 360;

	private static final int[] positionX = {529, 163, 64, 79, 210, 442, 637, 816, 828, 708};
	private static final int[] positionY = {133, 121, 314, 497, 632, 617, 628, 512, 293, 127};
	private static final int offsetX = 20;
	private static final int offsetY = -15;

	private final Texture cardTextures = new Texture(Gdx.files.internal("data/cards.png"));
	private final TextureRegion cardReverse = new TextureRegion(new Texture(Gdx.files.internal("data/reverse.png")), 0, 0, 69, 94);

	private final SpriteBatch batcher;
	private final GameState gameState;

	public CardDrawer(SpriteBatch batcher, GameState gameState) {
		this.batcher = batcher;
		this.gameState = gameState;
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

	private TextureRegion findCurrentCardTexture(final Card card) {
		final Tuple2<Integer, Integer> coordinates = CardCoordinates.find(card);
		return new TextureRegion(cardTextures, coordinates._1, coordinates._2, 69, 94);
	}
}
