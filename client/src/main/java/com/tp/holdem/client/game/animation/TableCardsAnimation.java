package com.tp.holdem.client.game.animation;

import com.tp.holdem.client.game.GameState;
import com.tp.holdem.model.message.dto.CardDTO;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.util.function.Function;
import java.util.stream.IntStream;

public class TableCardsAnimation {
	private static final int[] finalPositionX = {275, 367, 459, 551, 643};
	private static final int finalPositionY = 360;

	private final GameState gameState;
	private final int[] framePositionsY;
	private final int frames;

	private boolean drawing = false;
	private int currentFrame = 0;
	private int previousCardCount = 0;

	public TableCardsAnimation(int frames, GameState gameState) {
		this.frames = frames;
		this.gameState = gameState;
		this.framePositionsY = IntStream.iterate(1024, number -> number - ((1024 - finalPositionY) / frames)).limit(frames).toArray();
	}

	public Map<CardDTO, Tuple2<Integer, Integer>> positions() {
		int newCardCount = gameState.getCardsOnTable().size();

		if (newCardCount == 0)
			previousCardCount = 0;

		if (newCardCount > previousCardCount) {
			drawing = true;
			previousCardCount = newCardCount;
		}

		updateAnimation();
		return drawing ? frameBasedCoordinates(newCardCount) : finalCoordinates();
	}

	private Map<CardDTO, Tuple2<Integer, Integer>> frameBasedCoordinates(int cardCount) {
		final Function<Tuple2<CardDTO, Integer>, Tuple2<Integer, Integer>> framePositionMapper = indexBasedMapper(cardCount);

		return List.ofAll(gameState.getCardsOnTable())
				.zipWithIndex()
				.toMap(tuple -> tuple._1, framePositionMapper);
	}

	private Map<CardDTO, Tuple2<Integer, Integer>> finalCoordinates() {
		return List.ofAll(gameState.getCardsOnTable())
				.zipWithIndex()
				.toMap(tuple -> tuple._1, tuple -> Tuple.of(finalPositionX[tuple._2], finalPositionY));
	}

	private Function<Tuple2<CardDTO, Integer>, Tuple2<Integer, Integer>> indexBasedMapper(int cardCount) {
		return tuple -> {
			final Tuple2<Integer, Integer> finalPosition = Tuple.of(finalPositionX[tuple._2], finalPositionY);
			final Tuple2<Integer, Integer> frameBasedPosition = Tuple.of(finalPositionX[tuple._2], framePositionsY[currentFrame]);

			if (cardCount == 3)
				return frameBasedPosition;
			if (cardCount == 4)
				return tuple._2 < 3 ? finalPosition : frameBasedPosition;
			return tuple._2 < 4 ? finalPosition : frameBasedPosition;
		};
	}

	private void updateAnimation() {
		if (drawing) {
			currentFrame++;
			if (currentFrame == frames) {
				currentFrame = 0;
				drawing = false;
			}
		}
	}
}
