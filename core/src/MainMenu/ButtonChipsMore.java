package MainMenu;

import javax.swing.JButton;

public class ButtonChipsMore extends JButton
{
	public ButtonChipsMore(MainMenu menu)
	{
		super(">");
		addActionListener(new ButtonChipsMoreListener(menu));
	}
}
