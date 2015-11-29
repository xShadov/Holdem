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
    private BitmapFont font, font2;
    private Table table;
    private boolean waitingForAll;
    private String waitingMessage = "Nope";
    private int pot = 0;
    private List<Player> players;
    private List<Card> yourCards;
    private List<Card> cardsOnTable;
    private Texture cards;
    private int yourNumber=0;
    private TextureRegion reverse, bigBlind, smallBlind, dealer, box;
    private int[] positionX = {529, 163, 64, 79, 210, 442, 637, 816, 828, 708};
    private int[] positionY = {133, 121, 314, 497, 632, 617, 628, 512, 293, 127};
    private int[] dealerPositionX = {448, 276, 210, 228, 303, 477, 660, 736, 738, 666};
    private int[] dealerPositionY = {237, 243, 330, 442, 502, 499, 516, 429, 313, 244};
    private int[] blindPositionX = {490, 315, 213, 246, 340, 519, 630, 748, 763, 637};
    private int[] blindPositionY = {235, 234, 369, 469, 540, 520, 532, 466, 342, 237};
    private int[] boxPositionX = {364, 136, 21, 45, 168, 405, 597, 777, 813, 678};
    private int[] boxPositionY = {120, 112, 301, 484, 616, 603, 612, 498, 227, 117};
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
        box = new TextureRegion(new Texture(Gdx.files.internal("data/infoBox.png")), 0, 0, 160, 96);
        reverse = new TextureRegion(new Texture(Gdx.files.internal("data/reverse.png")), 0, 0, 69, 94);
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font.getData().setScale(.54f);
		font2 = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font2.getData().setScale(.80f);
    }
    
    public void render(float delta, float runTime) {
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        
        batcher.draw(bg, 0, 0, 1024, 780);
        batcher.enableBlending();
        
        if(waitingForAll){
        	font2.draw(batcher, waitingMessage, 300, 500);
        }
        if(players!=null){
	        for(int i=0; i<players.size();i++){
	        	if(players.get((i+yourNumber)%players.size()).getNumber()==yourNumber){
		        	findCurrentCardTexture(yourCards.get(0));
		        	batcher.draw(currentCardTexture, positionX[i], positionY[i]);
		        	findCurrentCardTexture(yourCards.get(1));
		        	batcher.draw(currentCardTexture, positionX[i]+20, positionY[i]-15);
	        	}
	        	else{
	        		batcher.draw(reverse, positionX[i], positionY[i]);
	        		batcher.draw(reverse, positionX[i]+20, positionY[i]-15);
	        	}
	        	if(players.get((i+yourNumber)%players.size()).isHasDealerButton()){
	        		batcher.draw(dealer, dealerPositionX[i], dealerPositionY[i]);
	        	}
	        	if(players.get((i+yourNumber)%players.size()).isHasSmallBlind()){
	        		batcher.draw(smallBlind, blindPositionX[i], blindPositionY[i]);
	        	}
	        	else if(players.get((i+yourNumber)%players.size()).isHasBigBlind()){
	        		batcher.draw(bigBlind, blindPositionX[i], blindPositionY[i]);
	        	}
        		batcher.draw(box, boxPositionX[i], boxPositionY[i]);
        		font.draw(batcher, players.get((i+yourNumber)%players.size()).getName(), boxPositionX[i]+18, boxPositionY[i]+75);
        		font.draw(batcher, "Chips: "+players.get((i+yourNumber)%players.size()).getChipsAmount(), boxPositionX[i]+18, boxPositionY[i]+30);
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



















