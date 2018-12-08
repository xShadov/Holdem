package com.tp.holdem.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPropertiesListener implements ActionListener {
	private MainMenu menu;

	public ButtonPropertiesListener(MainMenu menu) {
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		menu.properties();
	}

};
