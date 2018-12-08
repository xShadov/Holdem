package com.tp.holdem.menu;

import javax.swing.JButton;

public class ButtonFixedRaiseMore extends JButton {
	public ButtonFixedRaiseMore(MainMenu menu) {
		super(">");
		addActionListener(new ButtonFixedRaiseMoreListener(menu));
	}
}
