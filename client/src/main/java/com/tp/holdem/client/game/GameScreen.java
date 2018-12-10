package com.tp.holdem.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen, InputProcessor {
	private final GameWatcher watcher;
	private final GameRenderer renderer;
	private final GameElements gameElements;
	private final GameState gameState;

	private float runTime;
	private Stage stage;

	public GameScreen(GameWatcher world, GameRenderer renderer, GameElements gameElements, GameState gameState) {
		this.watcher = world;
		this.renderer = renderer;
		this.gameElements = gameElements;
		this.gameState = gameState;

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(final float delta) {
		stage.act(delta);
		runTime += delta;
		renderer.render(delta, runTime);
		stage.draw();
	}

	@Override
	public void resize(final int width, final int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void show() {
		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		gameElements.actors().forEach(stage::addActor);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(final int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(final int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(final char character) {
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		return false;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		return false;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(final int amount) {
		return false;
	}

}
