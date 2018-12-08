package com.tp.holdem.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonBotsMoreListener implements ActionListener {
	private MainMenu menu;

	public ButtonBotsMoreListener(MainMenu menu) {
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		if (menu.getBotsCount() < 10) {
			if (menu.getPlayersCount() + menu.getBotsCount() < 10) {
				menu.setBotsCount(menu.getBotsCount() + 1);
				menu.getBotsCountLabel().setText(String.valueOf(menu.getBotsCount()) + " bots");
			}
		}
	}

};