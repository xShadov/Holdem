package com.tp.holdem;

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
    private Table table;
    private TextButton checkButton, betButton, foldButton, raiseButton, callButton, allInButton;
    private Skin buttonsSkin;
    private BitmapFont font;
    private TextureAtlas atlas;
    private SampleRequest request;
    private KryoClient client;
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
        Gdx.input.setInputProcessor(this);
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
    	
    	font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
    	atlas = new TextureAtlas("data/button.pack");
    	buttonsSkin = new Skin(atlas);
    	table = new Table(buttonsSkin);
    	table.setBounds(830, 0, 189, 154);

    	Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    	final Slider slider = new Slider(0, 10, 1, false, skin);
		slider.setAnimateDuration(0.01f);
		
		slider.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.log("UITest", "slider: " + slider.getValue());
			}
		});
		
		slider.setPosition(500, 400);
		slider.setWidth(400);
		
		stage.addActor(slider);
    	
    	TextButtonStyle textButtonStyle = new TextButtonStyle();
    	textButtonStyle.up = buttonsSkin.getDrawable("button_up");
    	textButtonStyle.down = buttonsSkin.getDrawable("button_down");
    	textButtonStyle.pressedOffsetX = 1;
    	textButtonStyle.pressedOffsetY = 1;
    	textButtonStyle.font = font;
    	
    	
    	checkButton = new TextButton("CHECK", textButtonStyle);
    	foldButton = new TextButton("FOLD", textButtonStyle);
    	betButton = new TextButton("BET", textButtonStyle);
    	callButton = new TextButton("CALL", textButtonStyle);
    	allInButton = new TextButton("ALL IN", textButtonStyle);
    	raiseButton = new TextButton("RAISE", textButtonStyle);
    	
    	checkButton.setVisible(false);
    	
    	checkButton.pad(10);
    	checkButton.getLabel().setFontScale(0.45f);
    	checkButton.setBounds(840, 100, 78, 40);
    	checkButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("CHECK", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
    		}
    	} );
    	stage.addActor(checkButton);
    	
    	
    	foldButton.pad(10);
    	foldButton.getLabel().setFontScale(0.45f);
    	foldButton.setBounds(840, 56, 78, 40);
    	foldButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("FOLD", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
    		}
    	} );
    	stage.addActor(foldButton);
    	
    	betButton.pad(10);
    	betButton.getLabel().setFontScale(0.45f);
    	betButton.setBounds(840, 12, 78, 40);
    	betButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
    				request = new SampleRequest("BET", renderer.getMaxBetOnTable(), renderer.getYourNumber());
    				client.getSimulationClient().sendTCP(request);
    			}
    		}
    	} );
    	stage.addActor(betButton);
    	
    	callButton.pad(10);
    	callButton.getLabel().setFontScale(0.45f);
    	callButton.setBounds(924, 100, 78, 40);
    	callButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("CALL", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
    		}
    	} );
    	stage.addActor(callButton);
    	
    	allInButton.pad(10);
    	allInButton.getLabel().setFontScale(0.45f);
    	allInButton.setBounds(924, 56, 78, 40);
    	allInButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("ALLIN", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
    		}
    	} );
    	stage.addActor(allInButton);
    	
    	raiseButton.pad(10);
    	raiseButton.getLabel().setFontScale(0.45f);
    	raiseButton.setBounds(924, 12, 78, 40);
    	raiseButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("RAISE", renderer.getMaxBetOnTable()*2, renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
    		}
    	} );
    	stage.addActor(raiseButton);
    	
    	
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

}
