package MainMenu;

import javax.swing.JButton;

public class ButtonStrategy extends JButton {
	public ButtonStrategy(MainMenu menu) {
		super(menu.getStrategy().getName());
		addActionListener(new ButtonStrategyListener(this, menu));
	}

};
