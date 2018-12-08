package com.tp.holdem.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonServerListener implements ActionListener {
	private MainMenu menu;

	public ButtonServerListener(MainMenu menu) {
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		menu.startServer();
		menu.checkThreads();
	}
};
