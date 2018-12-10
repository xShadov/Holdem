package com.tp.holdem.menu;

import com.tp.holdem.client.strategy.FoldStrategy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class ButtonLimitListener implements ActionListener {
	private ButtonLimit button;
	private MainMenu menu;
	private ButtonStrategy strategy;

	public ButtonLimitListener(ButtonLimit button, MainMenu menu, ButtonStrategy strategy) {
		this.button = button;
		this.menu = menu;
		this.strategy = strategy;
	}

	public void actionPerformed(ActionEvent e) {
		if (menu.getLimit().equals("no-limit")) {
			button.setText("pot-limit");
			menu.setLimit("pot-limit");
			return;
		}
		if (menu.getLimit().equals("pot-limit")) {

			button.setText("fixed-limit");
			menu.setLimit("fixed-limit");
			if (menu.getStrategy().getName() == "Always All-in") {
				menu.setStrategy(new FoldStrategy());
				strategy.setText(menu.getStrategy().getName());
			}
			menu.getProperties().getContentPane().remove(menu.getPropPanel());
			menu.getPropertiesLayout().gridx = 0;
			menu.getPropertiesLayout().gridy = 1;
			menu.getPropertiesLayout().gridwidth = 1;
			menu.getPropPanel().add(menu.getFixedLess(), menu.getPropertiesLayout());
			menu.getPropertiesLayout().gridx = 1;
			menu.getPropertiesLayout().gridy = 1;
			menu.getPropertiesLayout().gridwidth = 2;
			menu.getPropPanel().add(menu.getFixedAmount(), menu.getPropertiesLayout());
			menu.getPropertiesLayout().gridx = 3;
			menu.getPropertiesLayout().gridy = 1;
			menu.getPropertiesLayout().gridwidth = 1;
			menu.getPropPanel().add(menu.getFixedMore(), menu.getPropertiesLayout());

			menu.getPropertiesLayout().gridx = 0;
			menu.getPropertiesLayout().gridy = 2;
			menu.getPropertiesLayout().gridwidth = 1;
			menu.getPropPanel().add(menu.getRaiseLess(), menu.getPropertiesLayout());
			menu.getPropertiesLayout().gridx = 1;
			menu.getPropertiesLayout().gridy = 2;
			menu.getPropertiesLayout().gridwidth = 2;
			menu.getPropPanel().add(menu.getFixedRaiseLabel(), menu.getPropertiesLayout());
			menu.getPropertiesLayout().gridx = 3;
			menu.getPropertiesLayout().gridy = 2;
			menu.getPropertiesLayout().gridwidth = 1;
			menu.getPropPanel().add(menu.getRaiseMore(), menu.getPropertiesLayout());
			menu.getProperties().getContentPane().add(menu.getPropPanel());
			menu.getProperties().pack();
			menu.setBoolLayout(true);
			return;
		}
		if (menu.getLimit().equals("fixed-limit")) {
			button.setText("no-limit");
			menu.setLimit("no-limit");
			menu.getProperties().getContentPane().remove(menu.getPropPanel());
			menu.getPropPanel().remove(menu.getFixedLess());
			menu.getPropPanel().remove(menu.getFixedAmount());
			menu.getPropPanel().remove(menu.getFixedMore());
			menu.getPropPanel().remove(menu.getRaiseLess());
			menu.getPropPanel().remove(menu.getFixedRaiseLabel());
			menu.getPropPanel().remove(menu.getRaiseMore());
			menu.getProperties().getContentPane().add(menu.getPropPanel());
			menu.getProperties().pack();
			menu.setBoolLayout(false);
			return;
		}
	}

};
