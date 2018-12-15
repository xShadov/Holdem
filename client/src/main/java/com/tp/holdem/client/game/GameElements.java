package com.tp.holdem.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tp.holdem.client.architecture.action.Action;
import com.tp.holdem.client.architecture.action.ActionBus;
import com.tp.holdem.client.architecture.message.ServerObservable;
import com.tp.holdem.model.game.Moves;
import com.tp.holdem.model.game.Player;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.UpdateStateMessage;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class GameElements implements ServerObservable {
	private final Map<Moves, TextButton> moveButtons;
	private final Slider slider;

	private final Batch batcher;

	public GameElements(ActionBus withWatcher) {
		final TextureAtlas atlas = new TextureAtlas("data/button.pack");
		final Skin buttonsSkin = new Skin(atlas);
		final Table table = new Table(buttonsSkin);
		table.setBounds(380, 19, 250, 50);

		final BitmapFont font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		final Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		slider = new Slider(0, 1500, 1, false, skin);
		slider.setAnimateDuration(0.01f);
		slider.setPosition(300, 80);
		slider.setWidth(400);

		batcher = new SpriteBatch();

		final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = buttonsSkin.getDrawable("button_up");
		textButtonStyle.down = buttonsSkin.getDrawable("button_down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = 1;
		textButtonStyle.font = font;

		final TextButton checkButton = new TextButton("CHECK", textButtonStyle);
		final TextButton foldButton = new TextButton("FOLD", textButtonStyle);
		final TextButton betButton = new TextButton("BET", textButtonStyle);
		final TextButton callButton = new TextButton("CALL", textButtonStyle);
		final TextButton allInButton = new TextButton("ALL IN", textButtonStyle);
		final TextButton raiseButton = new TextButton("RAISE", textButtonStyle);

		checkButton.setBounds(460, 19, 78, 40);
		foldButton.setBounds(380, 19, 78, 40);
		betButton.setBounds(540, 19, 78, 40);
		callButton.setBounds(460, 19, 78, 40);
		allInButton.setBounds(460, 19, 78, 40);
		raiseButton.setBounds(540, 19, 78, 40);

		moveButtons = HashMap.of(
				Moves.RAISE, raiseButton,
				Moves.FOLD, foldButton,
				Moves.CHECK, checkButton,
				Moves.ALLIN, allInButton,
				Moves.CALL, callButton,
				Moves.BET, betButton
		);
		moveButtons.values().forEach(button -> {
			button.pad(10);
			button.getLabel().setFontScale(0.45f);
		});
		setButtonsInvisible();

		checkButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(Moves.CHECK));
				setButtonsInvisible();
			}
		});

		foldButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(Moves.FOLD));
				setButtonsInvisible();
			}
		});

		betButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.from(Moves.BET, (int) slider.getValue()));
				setButtonsInvisible();
			}
		});

		callButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(Moves.CALL));
				setButtonsInvisible();
			}
		});

		allInButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(Moves.ALLIN));
				setButtonsInvisible();
			}
		});

		raiseButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.from(Moves.RAISE, (int) slider.getValue()));
				setButtonsInvisible();
			}
		});
	}

	public Slider slider() {
		return slider;
	}

	public List<Actor> actors() {
		return List.<Actor>ofAll(moveButtons.values()).append(slider);
	}

	private void setButtonsInvisible() {
		moveButtons.values().forEach(actor -> {
			actor.setVisible(false);
			actor.setDisabled(true);
		});
		slider.setVisible(false);
		slider.setDisabled(true);
	}

	@Override
	public void accept(Message message) {
		if (message.getMessageType() == MessageType.UPDATE_STATE) {
			handleUpdateState(message);
		}
	}

	private void handleUpdateState(Message message) {
		final UpdateStateMessage content = message.instance(UpdateStateMessage.class);

		final Player currentPlayer = content.getCurrentPlayer();
		final List<Moves> options = List.ofAll(currentPlayer.getPossibleMoves());

		options.flatMap(moveButtons::get)
				.forEach(button -> {
					button.setVisible(true);
					button.setDisabled(false);
				});

		if (options.contains(Moves.BET) || options.contains(Moves.RAISE)) {
			slider.setDisabled(false);
			slider.setVisible(true);

			slider.setRange(
					currentPlayer.getMinimumBet(),
					currentPlayer.getMaximumBet()
			);
		}
	}
}
