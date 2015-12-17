package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class GameWorld {
	
	private transient GameRenderer renderer;
    private final transient TextButton checkButton, betButton, foldButton, raiseButton, callButton, allInButton;
    private transient final Slider slider;
    private final transient Skin skin;
    private final Skin buttonsSkin;
    private final transient BitmapFont font;
    private final transient Table table;
    private final transient TextureAtlas atlas;
    private transient SampleRequest request;
    private List<TextButton> buttons;
    private transient KryoClient client;
    private transient int betValue = 0;
    
    public GameWorld(){
    	buttons = new ArrayList<TextButton>();
    	atlas = new TextureAtlas("data/button.pack");
    	buttonsSkin = new Skin(atlas);
    	table = new Table(buttonsSkin);
    	table.setBounds(380, 19, 250, 50);
    	font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
    	skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    	slider = new Slider(0, 1500, 1, false, skin);
		slider.setAnimateDuration(0.01f);
		
		slider.addListener(new ChangeListener() {
			public void changed (final ChangeEvent event, final Actor actor) {
				betValue = (int) slider.getValue();
			}
		});
		
		slider.setPosition(300, 80);
		slider.setWidth(400);
		
		final TextButtonStyle textButtonStyle = new TextButtonStyle();
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
    	
    	checkButton.pad(10);
    	checkButton.getLabel().setFontScale(0.45f);
    	checkButton.setBounds(460, 19, 78, 40);

    	checkButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(final InputEvent event, final float x, final float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("CHECK", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible(true);
    		}
    	} );
    	
    	foldButton.pad(10);
    	foldButton.getLabel().setFontScale(0.45f);
    	foldButton.setBounds(380, 19, 78, 40);

    	foldButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(final InputEvent event, final float x, final float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("FOLD", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible(true);
    		}
    	} );
    	
    	betButton.pad(10);
    	betButton.getLabel().setFontScale(0.45f);
    	betButton.setBounds(540, 19, 78, 40);

    	betButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(final InputEvent event, final float x, final float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
    				request = new SampleRequest("BET", betValue, renderer.getYourNumber());
    				client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible(true);
    		}
    	} );
    	
    	callButton.pad(10);
    	callButton.getLabel().setFontScale(0.45f);
    	callButton.setBounds(460, 19, 78, 40);

    	callButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(final InputEvent event, final float x, final float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("CALL", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible(true);
    		}
    	} );
    	
    	allInButton.pad(10);
    	allInButton.getLabel().setFontScale(0.45f);
    	allInButton.setBounds(460, 19, 78, 40);

    	allInButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(final InputEvent event, final float x, final float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("ALLIN", renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible(true);
    		}
    	} );
    	
    	raiseButton.pad(10);
    	raiseButton.getLabel().setFontScale(0.45f);
    	raiseButton.setBounds(540, 19, 78, 40);
    	raiseButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(final InputEvent event, final float x, final float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("RAISE", betValue, renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible(true);
    		}
    	} );
    	
    	setButtonsInvisible(true);
    	buttons.add(raiseButton);
    	buttons.add(foldButton);
    	buttons.add(checkButton);
    	buttons.add(allInButton);
    	buttons.add(callButton);
    	buttons.add(betButton);
    }
    
	protected void setButtonsInvisible(final boolean dummy) {
		if(renderer!=null && dummy){
			renderer.setTurnToBet(-1);
		}
    	raiseButton.setDisabled(true);
    	raiseButton.setVisible(false);
    	allInButton.setDisabled(true);
    	allInButton.setVisible(false);
    	callButton.setDisabled(true);
    	callButton.setVisible(false);
    	betButton.setDisabled(true);
    	betButton.setVisible(false);
    	foldButton.setDisabled(true);
    	foldButton.setVisible(false);
    	checkButton.setDisabled(true);
    	checkButton.setVisible(false);
    	slider.setVisible(false);
    	slider.setDisabled(true);
	}

	public void update(final float delta) {

    }
	
	public void setRenderer(final GameRenderer renderer) {
        this.renderer = renderer;
    }

	public List<TextButton> getButtons() {
		return buttons;
	}

	public void setButtons(final List<TextButton> buttons) {
		this.buttons = buttons;
	}

	public void setClient(final KryoClient client) {
		this.client = client;
	}

	public Slider getSlider() {
		return slider;
	}

	public void manageButtons(final List<String> possibleOptions) {
		for(int i=0; i<possibleOptions.size()-1; i++){
			if(possibleOptions.get(i).equals("ALLIN")){
				allInButton.setVisible(true);
				allInButton.setDisabled(false);
			}
			else if(possibleOptions.get(i).equals("CALL")){
				callButton.setVisible(true);
				callButton.setDisabled(false);
			} else if(possibleOptions.get(i).equals("FOLD")){
				foldButton.setVisible(true);
				foldButton.setDisabled(false);
			} else if(possibleOptions.get(i).equals("RAISE")){
				raiseButton.setVisible(true);
				raiseButton.setDisabled(false);
			} else if(possibleOptions.get(i).equals("CHECK")){
				checkButton.setVisible(true);
				checkButton.setDisabled(false);
			} else if(possibleOptions.get(i).equals("BET")){
				betButton.setVisible(true);
				betButton.setDisabled(false);
			}
		}
	}
}
