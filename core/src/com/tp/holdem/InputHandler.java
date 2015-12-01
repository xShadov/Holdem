package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor 
{
    private List<ActionButton> menuButtons;
    private ActionButton checkButton;
	private ActionButton callButton;
	private ActionButton betButton;
	private ActionButton raiseButton;
	private ActionButton allInButton;
	private ActionButton foldButton;
	private SampleRequest request;
	private KryoClient client;
	private GameRenderer renderer;
	
    public InputHandler(KryoClient client, GameRenderer renderer) 
    {
		this.client = client;
		this.renderer=renderer;
        menuButtons = new ArrayList<ActionButton>();
		//parameters depending on textures
        checkButton = new ActionButton(924, 12, 78, 40, null, null);
        callButton = new ActionButton(924, 12, 78, 40, null, null);
        betButton = new ActionButton(840,12,78,40,null,null);
        raiseButton = new ActionButton(840,12,78,40,null,null);
        allInButton = new ActionButton(840,12,78,40,null,null);
		foldButton = new ActionButton(756,12,78,40,null,null);
		
		menuButtons.add(checkButton);
		menuButtons.add(callButton);
		menuButtons.add(betButton);
		menuButtons.add(raiseButton);
		menuButtons.add(allInButton);
		menuButtons.add(foldButton);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) 
    {
		
		checkButton.isTouchDown(screenX, screenY);
		callButton.isTouchDown(screenX, screenY);
		betButton.isTouchDown(screenX, screenY);
		raiseButton.isTouchDown(screenX, screenY);
		allInButton.isTouchDown(screenX, screenY);
		foldButton.isTouchDown(screenX, screenY);
		
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) 
    {

		if (checkButton.isTouchUp(screenX, screenY)) 
		{ 
			request = new SampleRequest("CHECK", renderer.getYourNumber());
			client.getSimulationClient().sendTCP(request);
			return true;
		}
		else if (callButton.isTouchUp(screenX, screenY)) 
		{
			request = new SampleRequest("CALL", renderer.getYourNumber());
			client.getSimulationClient().sendTCP(request);
			return true;
		}
		else if (betButton.isTouchUp(screenX, screenY)) 
		{
			request = new SampleRequest("BET", renderer.getMaxBetOnTable(), renderer.getYourNumber());
			client.getSimulationClient().sendTCP(request);
			return true;
		}
		else if (raiseButton.isTouchUp(screenX, screenY)) 
		{
			request = new SampleRequest("RAISE", renderer.getMaxBetOnTable()*2, renderer.getYourNumber());
			client.getSimulationClient().sendTCP(request);
			return true;
		}
		else if (allInButton.isTouchUp(screenX, screenY)) 
		{
			request = new SampleRequest("ALLIN", renderer.getYourNumber());
			client.getSimulationClient().sendTCP(request);
			return true;
		}
		else if (foldButton.isTouchUp(screenX, screenY)) 
		{
			request = new SampleRequest("FOLD", renderer.getYourNumber());
			client.getSimulationClient().sendTCP(request);
			return true;
		}
        return false;
    }

    public List<ActionButton> getMenuButtons() 
    {
        return menuButtons;
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