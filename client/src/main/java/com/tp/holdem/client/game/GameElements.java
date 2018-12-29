package com.tp.holdem.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tp.holdem.client.architecture.action.Action;
import com.tp.holdem.client.architecture.action.ActionBus;
import com.tp.holdem.client.architecture.message.ServerObservable;
import com.tp.holdem.model.common.Moves;
import com.tp.holdem.model.message.Message;
import com.tp.holdem.model.message.MessageType;
import com.tp.holdem.model.message.UpdateStateMessage;
import com.tp.holdem.model.message.dto.CurrentPlayerDTO;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class GameElements implements ServerObservable {
	private final Map<Moves, TextButton> moveButtons;
	private final Slider slider;

	public GameElements(ActionBus withWatcher) {
		final Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		slider = new Slider(0, 1500, 1, false, skin);
		slider.setAnimateDuration(0.01f);
		slider.setPosition(700, 100);
		slider.setWidth(300);

		final TextButton checkButton = new TextButton("CHECK", skin);
		final TextButton foldButton = new TextButton("FOLD", skin);
		final TextButton betButton = new TextButton("BET", skin);
		final TextButton callButton = new TextButton("CALL", skin);
		final TextButton allInButton = new TextButton("ALL IN", skin);
		final TextButton raiseButton = new TextButton("RAISE", skin);

		checkButton.setBounds(800, 12, 100, 40);
		foldButton.setBounds(700, 12, 100, 40);
		betButton.setBounds(900, 12, 100, 40);
		callButton.setBounds(800, 12, 100, 40);
		allInButton.setBounds(800, 12, 100, 40);
		raiseButton.setBounds(900, 12, 100, 40);

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

		final CurrentPlayerDTO currentPlayer = content.getCurrentPlayer();
		final List<Moves> options = List.ofAll(currentPlayer.getPossibleMoves());

		setButtonsInvisible();

		options.flatMap(moveButtons::get)
				.forEach(button -> {
					button.setVisible(true);
					button.setDisabled(false);
				});

		if (options.contains(Moves.BET) || options.contains(Moves.RAISE)) {
			slider.setDisabled(false);
			slider.setVisible(true);

			slider.setStepSize(content.getTable().getSmallBlindAmount());
			slider.setRange(
					currentPlayer.getMinimumBet(),
					currentPlayer.getMaximumBet()
			);
		}
	}
}
