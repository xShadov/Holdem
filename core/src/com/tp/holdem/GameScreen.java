package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;



public class GameScreen implements Screen, InputProcessor {

	private GameWorld world;
    private GameRenderer renderer;
    private float runTime;
    private Stage stage;
    private KryoClient client;
    private Skin skin;
    private List<TextButton> buttons;
   
    // This is the constructor, not the class declaration
    public GameScreen() {
    	world = new GameWorld();
    	renderer = new GameRenderer(world);
    	try{
    		client = new KryoClient(renderer);
    	} catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
    	world.setRenderer(renderer);
    	world.setClient(client);
        Gdx.input.setInputProcessor(this);
        buttons = world.getButtons();
    }

    @Override
    public void render(float delta) {
    	stage.act(delta);
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    	stage.getViewport().update(width, height);
    }

    @Override
    public void show() {
    	
    	stage = new Stage();
    	Gdx.input.setInputProcessor(stage);
    	for(TextButton button : buttons){
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<TextButton> getButtons() {
		return buttons;
	}

	public void setButtons(List<TextButton> buttons) {
		this.buttons = buttons;
	}

}
