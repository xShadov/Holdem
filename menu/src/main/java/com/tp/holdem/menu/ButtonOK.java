package com.tp.holdem.menu;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ButtonOK extends JButton {
	public ButtonOK(JFrame properties, MainMenu menu) {
		super("OK");
		addActionListener(new ButtonOKListener(properties, menu));
	}
}
