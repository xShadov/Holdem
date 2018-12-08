package com.tp.holdem.menu;

import javax.swing.JButton;

public class ButtonBlindMore extends JButton {
	public ButtonBlindMore(MainMenu menu) {
		super(">");
		addActionListener(new ButtonBlindMoreListener(menu));
	}
}