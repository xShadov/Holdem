package MainMenu;

import javax.swing.JButton;

public class ButtonPlayersLess extends JButton
{
	public ButtonPlayersLess(MainMenu menu)
	{
		super("<");
		addActionListener(new ButtonPlayersLessListener(menu));
	}
}