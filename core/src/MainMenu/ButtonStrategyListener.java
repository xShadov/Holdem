package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.tp.holdem.AllInStrategy;
import com.tp.holdem.Easy;
import com.tp.holdem.FoldStrategy;
import com.tp.holdem.Hard;
import com.tp.holdem.Medium;

class ButtonStrategyListener implements ActionListener {
	ButtonStrategy button;
	MainMenu menu;

	public ButtonStrategyListener(ButtonStrategy button, MainMenu menu) {
		this.button = button;
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		if (button.getText().equals("Always All-in")) {
			menu.setStrategy(new FoldStrategy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Always Fold")) {
			menu.setStrategy(null);
			button.setText("Random");
			return;
		}
		if (button.getText().equals("Random")) {
			menu.setStrategy(new Easy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Easy")) {
			menu.setStrategy(new Medium());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Medium")) {
			menu.setStrategy(new Hard());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if (button.getText().equals("Hard")) {
			if (menu.getLimit() != "fixed-limit") {
				menu.setStrategy(new AllInStrategy());
				button.setText(menu.getStrategy().getName());
			} else {
				menu.setStrategy(new FoldStrategy());
				button.setText(menu.getStrategy().getName());
			}
			return;
		}
	}

};