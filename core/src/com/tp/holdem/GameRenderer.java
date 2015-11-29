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
    private List<Player> players;
    private List<Card> yourCards;
    private List<Card> cardsOnTable;
    private Texture cards;
    private int yourNumber;
    private TextureRegion reverse;
    private int[] positionX = {400, 141, 90, 105, 220, 420, 620, 750, 750, 637};
    private int[] positionY = {134, 140, 295, 460, 585, 565, 595, 465, 285, 128};
    public GameRenderer(GameWorld world){
    	bg = new TextureRegion(new Texture("data/pokerTable.jpg"), 0, 0, 1024, 780);
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
		font.getData().setScale(.25f);
    }
    
    public void render(float delta, float runTime) {
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();

        batcher.draw(bg, 0, 0, 1024, 780);
        batcher.enableBlending();
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
	        }
        }
        if(cardsOnTable!=null)
        {
        	for(int i=0;i<cardsOnTable.size();i++)
        	{
        		findCurrentCardTexture(cardsOnTable.get(i));
        		batcher.draw(currentCardTexture, 315+i*82,325);
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
    	else if(TAG.equals("C")){
    		cardsOnTable = response.getCards();
    	}
    	else if(TAG.equals("HCD")){
    		yourCards = response.getCards();
    	}
    }
}



















