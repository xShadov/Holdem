package MainMenu;

import javax.swing.JButton;

public class ButtonLimit extends JButton
{
	public ButtonLimit(MainMenu menu,ButtonStrategy strategy)
	{
		super(menu.getLimit());
		addActionListener(new ButtonLimitListener(this,menu,strategy));
	}
}
