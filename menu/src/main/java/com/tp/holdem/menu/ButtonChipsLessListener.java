package com.tp.holdem.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonChipsLessListener implements ActionListener {
	private MainMenu menu;

	public ButtonChipsLessListener(MainMenu menu) {
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		if (menu.getFixedChips() < menu.getChipsAmount() - 250) {
			if (menu.getChipsAmount() > 1000) {
				menu.setChipsAmount(menu.getChipsAmount() - 250);
				menu.getChipsAmountLabel().setText("Starting chips: " + String.valueOf(menu.getChipsAmount()));
			}
		}
	}

};