package MainMenu;

import javax.swing.JButton;

public class ButtonBlindLess extends JButton {
	public ButtonBlindLess(MainMenu menu) {
		super("<");
		addActionListener(new ButtonBlindLessListener(menu));
	}
}
