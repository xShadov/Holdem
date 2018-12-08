package com.tp.holdem.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonBotsLessListener implements ActionListener {
	private MainMenu menu;

	public ButtonBotsLessListener(MainMenu menu) {
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {

		if (menu.getBotsCount() > 0) {
			if (menu.getPlayersCount() + menu.getBotsCount() > 2) {
				menu.setBotsCount(menu.getBotsCount() - 1);
				menu.getBotsCountLabel().setText(String.valueOf(menu.getBotsCount()) + " bots");
			}
		}
	}

};
