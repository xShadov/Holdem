package com.tp.holdem.core;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tp.holdem.core.model.Card;
import com.tp.holdem.core.model.Player;
import com.tp.holdem.core.model.PokerTable;
import com.tp.holdem.core.model.SampleResponse;

public class GameRenderer {

	private transient final GameWorld myWorld;
	private transient final OrthographicCamera cam;
	private transient final TextureRegion bg;
	private transient final SpriteBatch batcher;
	private transient final BitmapFont font, font2, font3;
	private transient PokerTable pokerTable;
	private transient boolean waitingForAll;
	private transient String waitingMessage = "Nope";
	private transient int winnerNumber = -1;
	private transient boolean tie = false;
	private transient int smallBlindAmount = 0;
	private transient int pot = 0;
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
	private transient Texture cards;
	private transient int yourNumber = 0;
	private transient TextureRegion reverse, bigBlind, smallBlind, dealer, box, boxOff, boxFold, smallStack, semiStack,
			bigStack, spotlight;
	private transient final int[] positionX = { 529, 163, 64, 79, 210, 442, 637, 816, 828, 708 };
	private transient final int[] positionY = { 133, 121, 314, 497, 632, 617, 628, 512, 293, 127 };
	private transient final int[] dealerPositionX = { 448, 276, 210, 228, 303, 477, 660, 736, 738, 666 };
	private transient final int[] dealerPositionY = { 237, 243, 330, 442, 502, 499, 516, 429, 313, 244 };
	private transient final int[] blindPositionX = { 490, 315, 213, 246, 340, 519, 630, 748, 763, 637 };
	private transient final int[] blindPositionY = { 235, 234, 369, 469, 540, 520, 532, 466, 342, 237 };
	private transient final int[] boxPositionX = { 364, 136, 21, 45, 168, 405, 597, 777, 813, 678 };
	private transient final int[] boxPositionY = { 120, 112, 301, 484, 616, 603, 612, 498, 227, 117 };
	private transient final int[] chipsPositionX = { 507, 313, 274, 286, 358, 538, 631, 705, 738, 636 };
	private transient final int[] chipsPositionY = { 273, 288, 374, 441, 501, 484, 484, 451, 370, 280 };

	public GameRenderer(final GameWorld world) {
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

		cards = new Texture(Gdx.files.internal("data/cards.png"));
		box = new TextureRegion(new Texture(Gdx.files.internal("data/infoBox.png")), 0, 0, 160, 96);
		boxOff = new TextureRegion(new Texture(Gdx.files.internal("data/infoBoxOff.png")), 0, 0, 160, 96);
		boxFold = new TextureRegion(new Texture(Gdx.files.internal("data/infoBoxFold.png")), 0, 0, 160, 96);
		reverse = new TextureRegion(new Texture(Gdx.files.internal("data/reverse.png")), 0, 0, 69, 94);
		spotlight = new TextureRegion(new Texture(Gdx.files.internal("data/spotlight.png")), 0, 0, 352, 740);
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font.getData().setScale(.4f);
		font2 = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font2.getData().setScale(.80f);
		font3 = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		font3.getData().setScale(1.5f);
	}

	public void render(final float delta, final float runTime) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batcher.begin();

		batcher.draw(bg, 0, 0, 1024, 780);
		batcher.enableBlending();

		if (gameOver) {
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
		}
		batcher.end();
	}

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
	}

	private void drawCardsOnTable(final int i) {
		batcher.draw(findCurrentCardTexture(cardsOnTable.get(i)), 315 + i * 82, 360);
	}

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
	}

	public TextureRegion findCurrentCardTexture(final Card card) {
		return new TextureRegion(cards, card.getxCordination(), card.getyCordination(), 69, 94);
	}

	public void changesOccurred(final String TAG, final SampleResponse response) {
		if (TAG.equals("R")) {
			players = response.getPlayers();
		} else if (TAG.equals("N")) {
			gameOver = false;
			yourNumber = response.getNumber();
		} else if (TAG.equals("T")) {
			pokerTable = response.getTable();
			cardsOnTable = pokerTable.getCardsOnTable();
			pot = pokerTable.getPot();
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
						} else if (limitType.equals("pot-limit")) {
							if (players.get(yourNumber).getChipsAmount()
									- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()) < pot) {
								myWorld.getSlider().setRange(smallBlindAmount, players.get(yourNumber).getChipsAmount()
										- (maxBetOnTable - players.get(yourNumber).getBetAmountThisRound()));
							} else {
								myWorld.getSlider().setRange(smallBlindAmount, pot);
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
	}
}
