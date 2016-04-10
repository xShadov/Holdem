package com.tp.holdem;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameScreen implements Screen, InputProcessor {

	private transient final GameWorld world;
	private transient final GameRenderer renderer;
	private transient float runTime;
	private transient Stage stage;
	private transient KryoClient client;
	private transient List<TextButton> buttons;

	// This is the constructor, not the class declaration
	public GameScreen() {
		world = new GameWorld();
		renderer = new GameRenderer(world);
		try {
			client = new KryoClient(renderer);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		world.setRenderer(renderer);
		world.setClient(client);
		Gdx.input.setInputProcessor(this);
		buttons = world.getButtons();
	}

	@Override
	public void render(final float delta) {
		stage.act(delta);
		runTime += delta;
		world.update(delta);
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
		for (final TextButton button : buttons) {
			stage.addActor(button);
		}
		stage.addActor(world.getSlider());
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

	public List<TextButton> getButtons() {
		return buttons;
	}

	public void setButtons(final List<TextButton> buttons) {
		this.buttons = buttons;
	}

	@Override
	public boolean keyDown(final int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(final int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(final char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(final int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
