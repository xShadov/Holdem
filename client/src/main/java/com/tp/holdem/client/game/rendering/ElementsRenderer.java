package com.tp.holdem.client.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.tp.holdem.client.game.GameElements;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class ElementsRenderer implements Renderer {
	private GameElements gameElements;

	private final SpriteBatch batcher;
	private final BitmapFont smallFont;

	private static final Function<Slider, String> SLIDER_VALUES = slider -> String.format("%d/%d", (int) slider.getValue(), (int) slider.getMaxValue());

	public ElementsRenderer(GameElements gameElements) {
		OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(false, 1024, 780);
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);

		smallFont = Fonts.builder().type(Fonts.Types.JMH).size(20).color(Color.WHITE).generate();

		this.gameElements = gameElements;
	}

	@Override
	public void render(final float delta, final float runTime) {
		batcher.begin();

		batcher.enableBlending();

		if (gameElements.slider().isVisible())
			smallFont.draw(batcher, String.format("Bet amount: %s", SLIDER_VALUES.apply(gameElements.slider())), 750, 95);

		batcher.end();
	}
}
