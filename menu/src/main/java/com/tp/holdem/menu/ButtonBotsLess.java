package com.tp.holdem.menu;

import javax.swing.JButton;

public class ButtonBotsLess extends JButton {

	public ButtonBotsLess(MainMenu menu) {
		super("<");
		addActionListener(new ButtonBotsLessListener(menu));
	}
}
