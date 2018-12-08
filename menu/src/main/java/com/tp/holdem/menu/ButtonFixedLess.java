package com.tp.holdem.menu;

import javax.swing.JButton;

public class ButtonFixedLess extends JButton {
	public ButtonFixedLess(MainMenu menu) {
		super("<");
		addActionListener(new ButtonFixedLessListener(menu));
	}
}
