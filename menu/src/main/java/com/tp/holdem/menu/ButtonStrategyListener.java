package com.tp.holdem.menu;

import com.tp.holdem.core.strategy.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonStrategyListener implements ActionListener {
	ButtonStrategy button;
	MainMenu menu;

	public ButtonStrategyListener(ButtonStrategy button, MainMenu menu) {
		this.button = button;
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		if (button.getText().equals("Always All-in")) {
			menu.setStrategy(new FoldStrategy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Always Fold")) {
			menu.setStrategy(null);
			button.setText("Random");
			return;
		}
		if (button.getText().equals("Random")) {
			menu.setStrategy(new EasyStrategy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Easy")) {
			menu.setStrategy(new MediumStrategy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Medium")) {
			menu.setStrategy(new HardStrategy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Hard")) {
			if (menu.getLimit() != "fixed-limit") {
				menu.setStrategy(new AllInStrategy());
				button.setText(menu.getStrategy().getName());
			} else {
				menu.setStrategy(new FoldStrategy());
				button.setText(menu.getStrategy().getName());
			}
			return;
		}
	}

};