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

	private float runTime = 0;
	private GameRenderer renderer;
    private float timeChecker;
    private TextButton checkButton, betButton, foldButton, raiseButton, callButton, allInButton;
    private Slider slider;
    private Skin skin;
    private Skin buttonsSkin;
    private BitmapFont font;
    private Table table;
    private TextureAtlas atlas;
    private SampleRequest request;
    private List<TextButton> buttons;
    private KryoClient client;
    private int betValue = 0;
    
    public GameWorld(){
    	buttons = new ArrayList<TextButton>();
    	atlas = new TextureAtlas("data/button.pack");
    	buttonsSkin = new Skin(atlas);
    	table = new Table(buttonsSkin);
    	table.setBounds(830, 0, 189, 154);
    	font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
    	skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    	slider = new Slider(0, 1500, 1, false, skin);
		slider.setAnimateDuration(0.01f);
		
		slider.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				betValue = (int) slider.getValue();
			}
		});
		
		slider.setPosition(330, 90);
		slider.setWidth(400);
		
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
				setButtonsInvisible();
    		}
    	} );
    	
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
				setButtonsInvisible();
    		}
    	} );
    	
    	betButton.pad(10);
    	betButton.getLabel().setFontScale(0.45f);
    	betButton.setBounds(840, 12, 78, 40);

    	betButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
    				request = new SampleRequest("BET", betValue, renderer.getYourNumber());
    				client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible();
    		}
    	} );
    	
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
				setButtonsInvisible();
    		}
    	} );
    	
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
				setButtonsInvisible();
    		}
    	} );
    	
    	raiseButton.pad(10);
    	raiseButton.getLabel().setFontScale(0.45f);
    	raiseButton.setBounds(924, 12, 78, 40);
    	raiseButton.addListener(new ClickListener() 
    	{
    		@Override
    		public void clicked(InputEvent event,float x, float y)
    		{
    			if(renderer.getTurnToBet()==renderer.getYourNumber()){
	    			request = new SampleRequest("RAISE", betValue, renderer.getYourNumber());
					client.getSimulationClient().sendTCP(request);
    			}
				setButtonsInvisible();
    		}
    	} );
    	
    	setButtonsInvisible();
    	buttons.add(raiseButton);
    	buttons.add(foldButton);
    	buttons.add(checkButton);
    	buttons.add(allInButton);
    	buttons.add(callButton);
    	buttons.add(betButton);
    }
    
	protected void setButtonsInvisible() {
		if(renderer!=null){
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

	public void update(float delta) {

    }
	
	public void setRenderer(GameRenderer renderer) {
        this.renderer = renderer;
    }

	public List<TextButton> getButtons() {
		return buttons;
	}

	public void setButtons(List<TextButton> buttons) {
		this.buttons = buttons;
	}

	public void setClient(KryoClient client) {
		this.client = client;
	}

	public Slider getSlider() {
		return slider;
	}

	public void manageButtons(int maxBetOnTable, int yourNumber, List<Player> players, PokerTable pokerTable) {
		if(maxBetOnTable == players.get(yourNumber).getBetAmountThisRound()){
			checkButton.setVisible(true);
			checkButton.setDisabled(false);
			raiseButton.setVisible(true);
			raiseButton.setDisabled(false);
		}
		else if(maxBetOnTable>players.get(yourNumber).getBetAmountThisRound()){
			if(maxBetOnTable>=players.get(yourNumber).getChipsAmount())
			{
				allInButton.setVisible(true);
				allInButton.setDisabled(false);
			} else { 
				callButton.setVisible(true);
				callButton.setDisabled(false);
				raiseButton.setVisible(true);
				raiseButton.setDisabled(false);
			}
		}
		else if(maxBetOnTable == 0){
			betButton.setVisible(true);
			betButton.setDisabled(false);
			if(pokerTable.getSmallBlindAmount()>=players.get(yourNumber).getChipsAmount())
			{
				allInButton.setVisible(true);
				allInButton.setDisabled(false);
			} else { 
				raiseButton.setVisible(true);
				raiseButton.setDisabled(false);
			}
		}
		foldButton.setVisible(true);
		foldButton.setDisabled(false);
	}
}
