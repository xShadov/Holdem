package com.tp.holdem.client.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.vavr.collection.List;
import lombok.Value;

@Value
public class CompositeRenderer implements Renderer {
	private SpriteBatch batcher;
	private List<Renderer> renderers;

	public static CompositeRenderer of(SpriteBatch batcher, List<Renderer> renderers) {
		return new CompositeRenderer(batcher, renderers);
	}

	@Override
	public void render(float delta, float runtime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batcher.begin();

		renderers.forEach(renderer -> renderer.render(delta, runtime));

		batcher.end();
	}
}
