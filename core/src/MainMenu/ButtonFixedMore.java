package MainMenu;

import javax.swing.JButton;

public class ButtonFixedMore extends JButton
{
	public ButtonFixedMore(MainMenu menu)
	{
		super(">");
		addActionListener(new ButtonFixedMoreListener(menu));
	}
}
