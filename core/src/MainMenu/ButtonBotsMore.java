package MainMenu;

import javax.swing.JButton;

public class ButtonBotsMore extends JButton {
	public ButtonBotsMore(MainMenu menu) {
		super(">");
		addActionListener(new ButtonBotsMoreListener(menu));
	}
}
