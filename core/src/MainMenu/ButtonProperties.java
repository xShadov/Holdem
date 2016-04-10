package MainMenu;

import javax.swing.JButton;

public class ButtonProperties extends JButton {
	public ButtonProperties(MainMenu menu) {
		super("SERVER PROPERTIES");
		addActionListener(new ButtonPropertiesListener(menu));
	}

};