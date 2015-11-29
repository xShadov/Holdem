package com.tp.holdem;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameRenderer {

	private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private TextureRegion bg, currentCardTexture;
    private SpriteBatch batcher;
    private BitmapFont font;
    private Table table;
    private boolean waitingForAll;
    private String waitingMessage = "Nope";
    private int pot = 0;
    private List<Player> players;
    private List<Card> yourCards;
    private List<Card> cardsOnTable;
    private Texture cards;
    private int yourNumber=0;
    private TextureRegion reverse, bigBlind, smallBlind, dealer;
    private int[] positionX = {400, 141, 90, 105, 220, 420, 620, 750, 750, 637};
    private int[] positionY = {134, 140, 295, 460, 585, 565, 595, 465, 285, 128};
    private int[] dealerPositionX = {415, 255, 253, 280, 363, 517, 682, 682, 678, 582};
    private int[] dealerPositionY = {232, 253, 382, 498, 526, 507, 526, 412, 291, 241};
    private int[] blindPositionX = {480, 307, 253, 274, 314, 457, 625, 679, 696, 640};
    private int[] blindPositionY = {241, 231, 330, 450, 553, 517, 534, 468, 345, 259};
    public GameRenderer(GameWorld world){
    	bg = new TextureRegion(new Texture("data/pokerTable.jpg"), 0, 0, 1024, 780);
    	dealer = new TextureRegion(new Texture("data/dealer.png"), 0, 0, 50, 48);
    	smallBlind = new TextureRegion(new Texture("data/smallBlind.png"), 0, 0, 35, 32);
    	bigBlind = new TextureRegion(new Texture("data/bigBlind.png"), 0, 0, 36, 34);
    	myWorld = world;
    	cam = new OrthographicCamera();
        cam.setToOrtho(false, 1024, 780);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        cards = new Texture(Gdx.files.internal("data/cards.png"));
        reverse = new TextureRegion(new Texture(Gdx.files.internal("data/reverse.png")), 0, 0, 69, 94);
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font.getData().setScale(.80f);
    }
    
    public void render(float delta, float runTime) {
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        
        batcher.draw(bg, 0, 0, 1024, 780);
        batcher.enableBlending();
        
        if(waitingForAll){
        	font.draw(batcher, waitingMessage, 250, 500);
        }
        if(players!=null){
	        for(int i=0; i<players.size();i++){
	        	if(players.get(i).getNumber()==yourNumber){
		        	findCurrentCardTexture(yourCards.get(0));
		        	batcher.draw(currentCardTexture, positionX[i]+15, positionY[i]+2);
		        	findCurrentCardTexture(yourCards.get(1));
		        	batcher.draw(currentCardTexture, positionX[i]+82+15, positionY[i]+2);
	        	}
	        	else{
	        		batcher.draw(reverse, positionX[i]+15, positionY[i]+2);
	        		batcher.draw(reverse, positionX[i]+82+15, positionY[i]+2);
	        	}
	        	if(players.get(i).isHasDealerButton()){
	        		batcher.draw(dealer, dealerPositionX[i], dealerPositionY[i]);
	        	}
	        	if(players.get(i).isHasSmallBlind()){
	        		batcher.draw(smallBlind, blindPositionX[i], blindPositionY[i]);
	        	}
	        	else if(players.get(i).isHasBigBlind()){
	        		batcher.draw(bigBlind, blindPositionX[i], blindPositionY[i]);
	        	}
	        }
        }
        if(cardsOnTable!=null)
        {
        	for(int i=0;i<cardsOnTable.size();i++)
        	{
        		findCurrentCardTexture(cardsOnTable.get(i));
        		batcher.draw(currentCardTexture, 315+i*82,360);
        	}
        }
        batcher.end();
    }
    
    public void findCurrentCardTexture(Card card){
        currentCardTexture = new TextureRegion(cards, card.getxCordination(), card.getyCordination(), 69, 94);
    }
    
    public void changesOccurred(String TAG, SampleResponse response){
    	if(TAG.equals("R")){
    		players = response.getPlayers();
    	}
    	else if(TAG.equals("N")){
    		yourNumber = response.getNumber();
    	}
    	else if(TAG.equals("T")){
    		table = response.getTable();
    		cardsOnTable = table.getCardList();
    		pot = table.getPot();
    	}
    	else if(TAG.equals("HCD")){
    		waitingForAll = false;
    		yourCards = response.getCards();
    	}
    	else if(TAG.equals("W")){
    		waitingForAll = true;
    		waitingMessage = response.getText();
    	}
    }
}



















