package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonPlayersMoreListener implements ActionListener {
	private MainMenu menu;

	public ButtonPlayersMoreListener(MainMenu menu) {
		this.menu = menu;
	}

	public void actionPerformed(ActionEvent e) {
		if (menu.getPlayersCount() < 10) {
			if (menu.getPlayersCount() + menu.getBotsCount() < 10) {
				menu.setPlayersCount(menu.getPlayersCount() + 1);
				menu.getPlayersCountLabel().setText(String.valueOf(menu.getPlayersCount()) + " players");
			}
		}
	}

};
