package com.tp.holdem.client.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import io.vavr.collection.List;
import lombok.Value;

@Value
public class CompositeRenderer implements Renderer {
	private List<Renderer> renderers;

	public static CompositeRenderer of(List<Renderer> renderers) {
		return new CompositeRenderer(renderers);
	}

	@Override
	public void render(float delta, float runtime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderers.forEach(renderer -> renderer.render(delta, runtime));
	}
}
