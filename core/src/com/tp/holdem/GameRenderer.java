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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameRenderer {

	private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private TextureRegion bg, currentCardTexture;
    private SpriteBatch batcher;
    private BitmapFont font, font2, font3;
    private PokerTable pokerTable;
    private boolean waitingForAll;
    private String waitingMessage = "Nope";
    private int winnerNumber = -1;
    private boolean tie = false;
    private int bigBlindAmount = 0;
    private int smallBlindAmount = 0;
    private int pot = 0;
    private int maxBetOnTable = 0;
    private int yourBetAmount = 0;
    private int fixedLimit = 0;
    private boolean gameOver = false;
    private List<String> possibleOptions;
    private String limitType;
    private int turnToBet;
    private List<Player> players;
    private List<Card> yourCards;
    private List<Card> cardsOnTable;
    private Texture cards;
    private int yourNumber=0;
    private List<TextButton> buttons;
    private TextureRegion reverse, bigBlind, smallBlind, dealer, box, boxOff, smallStack, semiStack, bigStack, spotlight;
    private int[] positionX = {529, 163, 64, 79, 210, 442, 637, 816, 828, 708};
    private int[] positionY = {133, 121, 314, 497, 632, 617, 628, 512, 293, 127};
    private int[] dealerPositionX = {448, 276, 210, 228, 303, 477, 660, 736, 738, 666};
    private int[] dealerPositionY = {237, 243, 330, 442, 502, 499, 516, 429, 313, 244};
    private int[] blindPositionX = {490, 315, 213, 246, 340, 519, 630, 748, 763, 637};
    private int[] blindPositionY = {235, 234, 369, 469, 540, 520, 532, 466, 342, 237};
    private int[] boxPositionX = {364, 136, 21, 45, 168, 405, 597, 777, 813, 678};
    private int[] boxPositionY = {120, 112, 301, 484, 616, 603, 612, 498, 227, 117};
    private int[] chipsPositionX = {507, 313, 274, 286, 358, 538, 631, 705, 738, 636 };
    private int[] chipsPositionY = {273, 288, 374, 441, 501, 484, 484, 451, 370, 280 };
    
    public GameRenderer(GameWorld world){
    	bg = new TextureRegion(new Texture("data/pokerTable.jpg"), 0, 0, 1024, 780);
    	dealer = new TextureRegion(new Texture("data/dealer.png"), 0, 0, 50, 48);
    	smallBlind = new TextureRegion(new Texture("data/smallBlind.png"), 0, 0, 35, 32);
    	bigBlind = new TextureRegion(new Texture("data/bigBlind.png"), 0, 0, 36, 34);
    	smallStack = new TextureRegion(new Texture("data/smallStack.png"), 0, 0, 28, 28);
    	semiStack = new TextureRegion(new Texture("data/semiStack.png"), 0, 0, 28, 30);
    	bigStack = new TextureRegion(new Texture("data/bigStack.png"), 0, 0, 31, 33);
    	myWorld = world;
    	cam = new OrthographicCamera();
        cam.setToOrtho(false, 1024, 780);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
        buttons = world.getButtons();
        cards = new Texture(Gdx.files.internal("data/cards.png"));
        box = new TextureRegion(new Texture(Gdx.files.internal("data/infoBox.png")), 0, 0, 160, 96);
        boxOff = new TextureRegion(new Texture(Gdx.files.internal("data/infoBoxOff.png")), 0, 0, 160, 96);
        reverse = new TextureRegion(new Texture(Gdx.files.internal("data/reverse.png")), 0, 0, 69, 94);
        spotlight = new TextureRegion(new Texture(Gdx.files.internal("data/spotlight.png")), 0, 0, 352, 740);
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font.getData().setScale(.4f);
		font2 = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font2.getData().setScale(.80f);
		font3 = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font3.getData().setScale(1.5f);
    }
    
    public void render(float delta, float runTime) {
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        
        batcher.draw(bg, 0, 0, 1024, 780);
        batcher.enableBlending();
        
        if(gameOver){
        	font3.draw(batcher, "SORRY, NOT ENOUGH PLAYERS", 100, 500);
        } else {
        	drawWinMessage();
        	drawWaitMessage();
        }
        if(players!=null){
	        drawSpotlight();
	        for(int i=0; i<players.size();i++){
	        	drawCards(i);
	        	drawDealerAndBlindButtons(i);
        		drawInfoBoxes(i);
	        	drawChips(i);
	        }
        }
        if(cardsOnTable!=null)
        {
        	for(int i=0;i<cardsOnTable.size();i++)
        	{
        		drawCardsOnTable(i);
        	}
        }
        batcher.end();
    }

	private void drawCardsOnTable(int i) {
		findCurrentCardTexture(cardsOnTable.get(i));
		batcher.draw(currentCardTexture, 315+i*82,360);
	}

	private void drawChips(int i) {
		if(players.get((i+yourNumber)%players.size()).getBetAmount()>0)
		{
			if(players.get((i+yourNumber)%players.size()).getBetAmount()<300){
				batcher.draw(smallStack, chipsPositionX[i], chipsPositionY[i]);
			}
			else if(players.get((i+yourNumber)%players.size()).getBetAmount()<1500){
				batcher.draw(semiStack, chipsPositionX[i], chipsPositionY[i]);
			}
			else{
				batcher.draw(bigStack, chipsPositionX[i], chipsPositionY[i]);
			}
		}
	}

	private void drawInfoBoxes(int i) {
		if(players.get((i+yourNumber)%players.size()).isInGame()){
			batcher.draw(box, boxPositionX[i], boxPositionY[i]);
		} else {
			batcher.draw(boxOff, boxPositionX[i], boxPositionY[i]);
		}
		font.draw(batcher, players.get((i+yourNumber)%players.size()).getName(), boxPositionX[i]+18, boxPositionY[i]+81);
		font.draw(batcher, "Chips: "+players.get((i+yourNumber)%players.size()).getChipsAmount(), boxPositionX[i]+18, boxPositionY[i]+54);
		font.draw(batcher,
				"Bet: "+players.get((i+yourNumber)%players.size()).getBetAmountThisRound()+"/"+players.get((i+yourNumber)%players.size()).getBetAmount(),
				boxPositionX[i]+18, boxPositionY[i]+27);
	}

	private void drawDealerAndBlindButtons(int i) {
		if(players.get((i+yourNumber)%players.size()).isHasDealerButton()){
			batcher.draw(dealer, dealerPositionX[i], dealerPositionY[i]);
		}
		if(players.get((i+yourNumber)%players.size()).isHasSmallBlind()){
			batcher.draw(smallBlind, blindPositionX[i], blindPositionY[i]);
		}
		else if(players.get((i+yourNumber)%players.size()).isHasBigBlind()){
			batcher.draw(bigBlind, blindPositionX[i], blindPositionY[i]);
		}
	}

	private void drawCards(int i) {
		if(players.get((i+yourNumber)%players.size()).getNumber()==yourNumber && players.get((i+yourNumber)%players.size()).isInGame()){
			findCurrentCardTexture(yourCards.get(0));
			batcher.draw(currentCardTexture, positionX[i], positionY[i]);
			findCurrentCardTexture(yourCards.get(1));
			batcher.draw(currentCardTexture, positionX[i]+20, positionY[i]-15);
		}
		else{
			if(players.get((i+yourNumber)%players.size()).isInGame()){
				batcher.draw(reverse, positionX[i], positionY[i]);
				batcher.draw(reverse, positionX[i]+20, positionY[i]-15);
			}
		}
	}
	
	private void drawSpotlight() {
		for(int i=0; i<players.size();i++){
			if(players.get((i+yourNumber)%players.size()).getNumber() == turnToBet){
				if(yourNumber == turnToBet){
					batcher.draw(spotlight, positionX[i]-250, positionY[i]-45);
					font.draw(batcher, myWorld.getSlider().getValue()+"", myWorld.getSlider().getX()+20, myWorld.getSlider().getY());
					break;
				} else {
					batcher.draw(spotlight, positionX[i]-130, positionY[i]-40);
					break;
				}
			}
		}
	}

	private void drawWaitMessage() {
		if(waitingForAll){
        	font2.draw(batcher, waitingMessage, 300, 500);
        }
	}

	private void drawWinMessage() {
		if(winnerNumber!=-1){
        	if(winnerNumber == yourNumber){
        		font3.draw(batcher, "YOU WIN!", 320, 550);
        	}
        	else{
        		font3.draw(batcher, players.get(winnerNumber).getName()+" WON!", 320, 550);
        	}
        }
        else if(tie){
        	font3.draw(batcher, "TIE!", 320, 550);
        }
	}
    
    public void findCurrentCardTexture(Card card){
        currentCardTexture = new TextureRegion(cards, card.getxCordination(), card.getyCordination(), 69, 94);
    }
    
    public void changesOccurred(String TAG, SampleResponse response){
    	if(TAG.equals("R")){
    		players = response.getPlayers();
    	}
    	else if(TAG.equals("N")){
    		gameOver = false;
    		yourNumber = response.getNumber();
    	}
    	else if(TAG.equals("T")){
    		pokerTable = response.getTable();
    		cardsOnTable = pokerTable.getCardList();
    		pot = pokerTable.getPot();
    		smallBlindAmount = pokerTable.getSmallBlindAmount();
    		bigBlindAmount = pokerTable.getBigBlindAmount();
    		limitType = pokerTable.getLimitType();
    		fixedLimit = pokerTable.getFixedLimit();
    	}
    	else if(TAG.equals("HCD")){
    		gameOver = false;
    		waitingForAll = false;
    		tie = false;
    		winnerNumber = -1;
    		yourCards = response.getCards();
    	}
    	else if(TAG.equals("W")){
    		waitingForAll = true;
    		waitingMessage = response.getText();
    	}
    	else if(TAG.equals("B")){
    		turnToBet = response.getNumber();
    		possibleOptions = response.getPossibleOptions();
    		maxBetOnTable = response.getMaxBetOnTable();
    		if(turnToBet == yourNumber){
    			myWorld.manageButtons(possibleOptions);
    			myWorld.getSlider().setValue(smallBlindAmount);
        		myWorld.getSlider().setStepSize(smallBlindAmount);
        		myWorld.getSlider().setVisible(true);
        		myWorld.getSlider().setDisabled(false);
        		if(players!=null && players.get(yourNumber).getChipsAmount()>=smallBlindAmount){
    				if(limitType.equals("no-limit")){
        				myWorld.getSlider().setRange(smallBlindAmount, 
        						players.get(yourNumber).getChipsAmount()-(maxBetOnTable-players.get(yourNumber).getBetAmountThisRound()));
    				} else if(limitType.equals("pot-limit")){
    					if(players.get(yourNumber).getChipsAmount()-(maxBetOnTable-players.get(yourNumber).getBetAmountThisRound())<pot){
    						myWorld.getSlider().setRange(smallBlindAmount, 
	        						players.get(yourNumber).getChipsAmount()-(maxBetOnTable-players.get(yourNumber).getBetAmountThisRound()));
    					} else {
    						myWorld.getSlider().setRange(smallBlindAmount, pot);
    					}
    				} 
    				else{
    					if(players.get(yourNumber).getChipsAmount()-(maxBetOnTable-players.get(yourNumber).getBetAmountThisRound())
    							<fixedLimit){
    						myWorld.getSlider().setRange(smallBlindAmount, 
	        						players.get(yourNumber).getChipsAmount()-(maxBetOnTable-players.get(yourNumber).getBetAmountThisRound()));
    					} else{
    						myWorld.getSlider().setRange(smallBlindAmount, fixedLimit);
    					}
					}
    			}
    		} else {
    			myWorld.setButtonsInvisible(false);
    		}
    	}
    	else if(TAG.equals("OW")){
    		winnerNumber = response.getNumber();
    	}
    	else if(TAG.equals("MW")){
    		tie = true;
    	}
    	else if(TAG.equals("GO")){
    		gameOver = true;
    	}
    }

	public int getYourNumber() {
		return yourNumber;
	}
	
	public int getTurnToBet() {
		return turnToBet;
	}
	
	public int getYourBetAmount() {
		return yourBetAmount;
	}
	
	public int getMaxBetOnTable() {
		return maxBetOnTable;
	}
	
	public void setTurnToBet(int turnToBet){
		this.turnToBet = turnToBet;
	}

	public List<String> getPossibleOptions() {
		return possibleOptions;
	}
}



















