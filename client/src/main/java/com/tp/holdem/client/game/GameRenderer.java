package com.tp.holdem.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.base.Preconditions;
import com.tp.holdem.client.architecture.bus.Action;
import com.tp.holdem.client.architecture.bus.GameObservable;
import com.tp.holdem.client.architecture.model.action.ActionType;
import com.tp.holdem.client.architecture.model.common.PlayerConnectMessage;
import com.tp.holdem.client.game.drawing.CardDrawer;
import com.tp.holdem.client.game.drawing.ChipsDrawer;
import com.tp.holdem.client.game.drawing.TableDrawer;
import com.tp.holdem.client.model.*;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class GameRenderer {
	private CardDrawer cardDrawer;
	private TableDrawer tableDrawer;
	private ChipsDrawer chipsDrawer;

	private GameState gameState;
	//private transient final GameWatcher myWorld;
	/*private transient PokerTable pokerTable;
	private transient boolean waitingForAll;
	private transient String waitingMessage = "Nope";
	private transient int winnerNumber = -1;
	private transient boolean tie = false;
	private transient int smallBlindAmount = 0;
	private transient int potAmount = 0;
	private transient boolean revealed = false;
	private transient List<List<Card>> revealedCards;
	private transient int maxBetOnTable = 0;
	private transient int fixedLimit = 0;
	private transient boolean gameOver = false;
	private transient List<String> possibleOptions;
	private transient String limitType;
	private transient int turnToBet;
	private transient List<Player> players;
	private transient List<Card> yourCards;
	private transient List<Card> cardsOnTable;
	private transient int yourNumber = 0;*/

	private final SpriteBatch batcher;
	private final TextureRegion bigBlind, smallBlind, dealer, box, boxOff, boxFold, spotlight;
	private final BitmapFont smallFont, mediumFont, bigFont;

	private final int[] dealerPositionX = {448, 276, 210, 228, 303, 477, 660, 736, 738, 666};
	private final int[] dealerPositionY = {237, 243, 330, 442, 502, 499, 516, 429, 313, 244};
	private final int[] blindPositionX = {490, 315, 213, 246, 340, 519, 630, 748, 763, 637};
	private final int[] blindPositionY = {235, 234, 369, 469, 540, 520, 532, 466, 342, 237};
	private final int[] boxPositionX = {364, 136, 21, 45, 168, 405, 597, 777, 813, 678};
	private final int[] boxPositionY = {120, 112, 301, 484, 616, 603, 612, 498, 227, 117};


	public GameRenderer(GameState gameState) {
		this.gameState = gameState;


		dealer = new TextureRegion(new Texture("data/dealer.png"), 0, 0, 50, 48);
		smallBlind = new TextureRegion(new Texture("data/smallBlind.png"), 0, 0, 35, 32);
		bigBlind = new TextureRegion(new Texture("data/bigBlind.png"), 0, 0, 36, 34);


		OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(false, 1024, 780);
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);


		box = new TextureRegion(new Texture(Gdx.files.internal("data/infoBox.png")), 0, 0, 160, 96);
		boxOff = new TextureRegion(new Texture(Gdx.files.internal("data/infoBoxOff.png")), 0, 0, 160, 96);
		boxFold = new TextureRegion(new Texture(Gdx.files.internal("data/infoBoxFold.png")), 0, 0, 160, 96);

		spotlight = new TextureRegion(new Texture(Gdx.files.internal("data/spotlight.png")), 0, 0, 352, 740);
		smallFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		smallFont.getData().setScale(.4f);
		mediumFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		mediumFont.getData().setScale(.80f);
		bigFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		bigFont.getData().setScale(1.5f);

		this.cardDrawer = new CardDrawer(batcher, gameState);
		this.tableDrawer = new TableDrawer(batcher);
		this.chipsDrawer = new ChipsDrawer(batcher, gameState);
	}

	public void render(final float delta, final float runTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batcher.begin();

		tableDrawer.drawTable();

		batcher.enableBlending();

		/*if (gameOver) {
			font3.draw(batcher, "SORRY, NOT ENOUGH PLAYERS", 100, 500);
		} else {
			drawWinMessage();
			drawWaitMessage();
		}
		if (players != null) {
			drawSpotlight();
			for (int i = 0; i < players.size(); i++) {
				drawCards(i);
				drawDealerAndBlindButtons(i);
				drawInfoBoxes(i);
				drawChips(i);
				drawRevealedCards(i);
			}
		}
		if (cardsOnTable != null) {
			for (int i = 0; i < cardsOnTable.size(); i++) {
				drawCardsOnTable(i);
			}
		}*/

		drawGameState();

		batcher.end();
	}

	private void drawGameState() {
		if (gameState.isCurrentPlayerWaiting())
			mediumFont.draw(batcher, "Waiting for all players", 300, 500);

		if (gameState.isGameStarted()) {
			cardDrawer.drawCards();

			chipsDrawer.drawChips();
		}
	}

/*
	private void drawRevealedCards(final int i) {
		if (revealed) {
			if (players.get((i + yourNumber) % players.size()).isInGame()
					&& !players.get((i + yourNumber) % players.size()).isFolded()) {
				batcher.draw(findCurrentCardTexture(revealedCards.get((i + yourNumber) % players.size()).get(0)),
						positionX[i], positionY[i]);
				batcher.draw(findCurrentCardTexture(revealedCards.get((i + yourNumber) % players.size()).get(1)),
						positionX[i] + 20, positionY[i] - 15);
			}
		}
	}*/

	//private void drawCardsOnTable(final int i) {
	//	batcher.draw(findCurrentCardTexture(cardsOnTable.get(i)), 315 + i * 82, 360);
	//}
/*
	private void drawChips(final int i) {
		if (players.get((i + yourNumber) % players.size()).getBetAmount() > 0) {
			if (players.get((i + yourNumber) % players.size()).getBetAmount() < 300) {
				batcher.draw(smallStack, chipsPositionX[i], chipsPositionY[i]);
			} else if (players.get((i + yourNumber) % players.size()).getBetAmount() < 1500) {
				batcher.draw(semiStack, chipsPositionX[i], chipsPositionY[i]);
			} else {
				batcher.draw(bigStack, chipsPositionX[i], chipsPositionY[i]);
			}
		}
	}

	private void drawInfoBoxes(final int i) {
		if (players.get((i + yourNumber) % players.size()).isInGame()
				&& !players.get((i + yourNumber) % players.size()).isFolded()) {
			batcher.draw(box, boxPositionX[i], boxPositionY[i]);
		} else if (players.get((i + yourNumber) % players.size()).isInGame()
				&& players.get((i + yourNumber) % players.size()).isFolded()) {
			batcher.draw(boxFold, boxPositionX[i], boxPositionY[i]);
		} else {
			batcher.draw(boxOff, boxPositionX[i], boxPositionY[i]);
		}
		font.draw(batcher, players.get((i + yourNumber) % players.size()).getName(), boxPositionX[i] + 18,
				boxPositionY[i] + 81);
		font.draw(batcher, "Chips: " + players.get((i + yourNumber) % players.size()).getChipsAmount(),
				boxPositionX[i] + 18, boxPositionY[i] + 54);
		font.draw(batcher,
				"Bet: " + players.get((i + yourNumber) % players.size()).getBetAmountThisRound() + "/"
						+ players.get((i + yourNumber) % players.size()).getBetAmount(),
				boxPositionX[i] + 18, boxPositionY[i] + 27);

	}

	private void drawDealerAndBlindButtons(final int i) {
		if (players.get((i + yourNumber) % players.size()).isHasDealerButton()) {
			batcher.draw(dealer, dealerPositionX[i], dealerPositionY[i]);
		}
		if (players.get((i + yourNumber) % players.size()).isHasSmallBlind()) {
			batcher.draw(smallBlind, blindPositionX[i], blindPositionY[i]);
		} else if (players.get((i + yourNumber) % players.size()).isHasBigBlind()) {
			batcher.draw(bigBlind, blindPositionX[i], blindPositionY[i]);
		}
	}

	private void drawCards(final int i) {
		if (!revealed) {
			if (players.get((i + yourNumber) % players.size()).getNumber() == yourNumber
					&& players.get((i + yourNumber) % players.size()).isInGame()) {
				batcher.draw(findCurrentCardTexture(yourCards.get(0)), positionX[i], positionY[i]);
				batcher.draw(findCurrentCardTexture(yourCards.get(1)), positionX[i] + 20, positionY[i] - 15);
			} else {
				if (players.get((i + yourNumber) % players.size()).isInGame()) {
					batcher.draw(reverse, positionX[i], positionY[i]);
					batcher.draw(reverse, positionX[i] + 20, positionY[i] - 15);
				}
			}
		}
	}

	private void drawSpotlight() {
		if (!revealed || winnerNumber == -1) {
			for (int i = 0; i < players.size(); i++) {
				if (players.get((i + yourNumber) % players.size()).getNumber() == turnToBet) {
					if (yourNumber == turnToBet) {
						batcher.draw(spotlight, boxPositionX[i] - 90, boxPositionY[i] - 28);
						if (myWorld.getSlider().isVisible()) {
							font.draw(batcher, myWorld.getSlider().getValue() + "", myWorld.getSlider().getX() + 20,
									myWorld.getSlider().getY());
						}
						break;
					} else {
						batcher.draw(spotlight, boxPositionX[i] - 90, boxPositionY[i] - 28);
						break;
					}
				}
			}
		} else {
			batcher.draw(spotlight, boxPositionX[winnerNumber] - 90, boxPositionY[winnerNumber] - 28);
		}
	}

	private void drawWaitMessage() {
		if (waitingForAll) {
			font2.draw(batcher, waitingMessage, 300, 500);
		}
	}

	private void drawWinMessage() {
		if (winnerNumber != -1) {
			if (winnerNumber == yourNumber) {
				font3.draw(batcher, "YOU WIN!", 320, 550);
			} else {
				font3.draw(batcher, players.get(winnerNumber).getName() + " WON!", 320, 550);
			}
		} else if (tie) {
			font3.draw(batcher, "TIE!", 320, 550);
		}
	}*/

/*	public void changesOccurred(final String TAG, final SimpleServerResponse response) {
		if (TAG.equals("R")) {
			players = response.getPlayers();
		} else if (TAG.equals("N")) {
			gameOver = false;
			yourNumber = response.getNumber();
		} else if (TAG.equals("T")) {
			pokerTable = response.getTable();
			cardsOnTable = pokerTable.getCardsOnTable();
			potAmount = pokerTable.getPotAmount();
			smallBlindAmount = pokerTable.getSmallBlindAmount();
			limitType = pokerTable.getLimitType();
			fixedLimit = pokerTable.getFixedLimit();
		} else if (TAG.equals("HCD")) {
			gameOver = false;
			revealed = false;
			waitingForAll = false;
			tie = false;
			winnerNumber = -1;
			yourCards = response.getCards();
		} else if (TAG.equals("W")) {
			waitingForAll = true;
			waitingMessage = response.getText();
		} else if (TAG.equals("RC")) {
			if (response.isRevealed()) {
				revealed = true;
				revealedCards = response.getCards2();
			} else {
				revealed = false;
				revealedCards = null;
			}
		} else if (TAG.equals("B")) {
			turnToBet = response.getNumber();
			possibleOptions = response.getPossibleOptions();
			maxBetOnTable = response.getMaxBetOnTable();
			if (turnToBet == yourNumber) {
				myWorld.manageButtons(possibleOptions);
				myWorld.getSlider().setValue(smallBlindAmount);
				myWorld.getSlider().setStepSize(smallBlindAmount);
				myWorld.getSlider().setVisible(true);
				myWorld.getSlider().setDisabled(false);
				if (players != null && players.get(yourNumber).getChipsAmount() >= smallBlindAmount) {
					if (!myWorld.getButtons().get(0).isVisible() && !myWorld.getButtons().get(5).isVisible()) {
						myWorld.getSlider().setVisible(false);
						myWorld.getSlider().setDisabled(true);
					} else {
						if (limitType.equals("no-limit")) {
							myWorld.getSlider().setRange(smallBlindAmount, players.get(yourNumber).getChipsAmount()
									- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()));
						} else if (limitType.equals("potAmount-limit")) {
							if (players.get(yourNumber).getChipsAmount()
									- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()) < potAmount) {
								myWorld.getSlider().setRange(smallBlindAmount, players.get(yourNumber).getChipsAmount()
										- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()));
							} else {
								myWorld.getSlider().setRange(smallBlindAmount, potAmount);
							}
						} else {
							if (players.get(yourNumber).getChipsAmount()
									- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()) < fixedLimit) {
								myWorld.getSlider().setRange(
										players.get(yourNumber).getChipsAmount()
												- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()),
										players.get(yourNumber).getChipsAmount()
												- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()));
							} else {
								myWorld.getSlider().setRange(fixedLimit, fixedLimit);
							}
						}
					}
				}
			} else {
				myWorld.setButtonsInvisible(false);
			}
		} else if (TAG.equals("OW")) {
			winnerNumber = response.getNumber();
		} else if (TAG.equals("MW")) {
			tie = true;
		} else if (TAG.equals("GO")) {
			gameOver = true;
		}
	}

	public int getYourNumber() {
		return yourNumber;
	}

	public int getTurnToBet() {
		return turnToBet;
	}

	public int getMaxBetOnTable() {
		return maxBetOnTable;
	}

	public void setTurnToBet(final int turnToBet) {
		this.turnToBet = turnToBet;
	}

	public List<String> getPossibleOptions() {
		return possibleOptions;
	}*/

}
