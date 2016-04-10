package MainMenu;

import javax.swing.JButton;

public class ButtonPlayersMore extends JButton {
	public ButtonPlayersMore(MainMenu menu) {
		super(">");
		addActionListener(new ButtonPlayersMoreListener(menu));
	}
}
