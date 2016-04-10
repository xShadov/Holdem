package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonChipsMoreListener implements ActionListener {
	private MainMenu menu;

	public ButtonChipsMoreListener(MainMenu menu) {
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		if (menu.getChipsAmount() < 50000) {
			menu.setChipsAmount(menu.getChipsAmount() + 250);
			menu.getChipsAmountLabel().setText("Starting chips: " + String.valueOf(menu.getChipsAmount()));
		}
	}

};
