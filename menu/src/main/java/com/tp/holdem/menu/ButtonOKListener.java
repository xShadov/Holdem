package com.tp.holdem.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

class ButtonOKListener implements ActionListener {
	private JFrame properties;
	private MainMenu menu;

	public ButtonOKListener(JFrame properties, MainMenu menu) {
		this.properties = properties;
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		properties.dispose();
	}

};
