package com.tp.holdem.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tp.holdem.client.architecture.bus.Action;
import com.tp.holdem.client.architecture.bus.ActionBus;
import com.tp.holdem.client.architecture.model.action.ActionType;
import com.tp.holdem.client.architecture.model.action.PlayerBetAction;
import com.tp.holdem.client.architecture.model.action.PlayerRaiseAction;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class GameElements {
	private final ActionBus withWatcher;
	private final Map<ActionType, TextButton> moveButtons;
	private final Slider slider;

	public GameElements(ActionBus withWatcher) {
		this.withWatcher = withWatcher;

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
				ActionType.RAISE, raiseButton,
				ActionType.FOLD, foldButton,
				ActionType.CHECK, checkButton,
				ActionType.ALLIN, allInButton,
				ActionType.CALL, callButton,
				ActionType.BET, betButton
		);
		moveButtons.values().forEach(button -> {
			button.pad(10);
			button.getLabel().setFontScale(0.45f);
		});
		setButtonsInvisible();

		checkButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(ActionType.CHECK));
				setButtonsInvisible();
			}
		});

		foldButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(ActionType.FOLD));
				setButtonsInvisible();
			}
		});

		betButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				final PlayerBetAction betRequest = PlayerBetAction.from((int) slider.getValue());
				withWatcher.message(Action.from(ActionType.BET, betRequest));
				setButtonsInvisible();
			}
		});

		callButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(ActionType.CALL));
				setButtonsInvisible();
			}
		});

		allInButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				withWatcher.message(Action.simple(ActionType.ALLIN));
				setButtonsInvisible();
			}
		});

		raiseButton.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				final PlayerRaiseAction raiseRequest = PlayerRaiseAction.from((int) slider.getValue());
				withWatcher.message(Action.from(ActionType.RAISE, raiseRequest));
				setButtonsInvisible();
			}
		});
	}

	public void manageButtons(final List<ActionType> possibleOptions) {
		possibleOptions
				.flatMap(moveButtons::get)
				.forEach(button -> {
					button.setVisible(true);
					button.setDisabled(false);
				});
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
}
