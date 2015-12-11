package MainMenu;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ButtonOK extends JButton
{
	public ButtonOK(JFrame properties)
	{
		super("OK");
		addActionListener(new ButtonOKListener(properties));
	}
}
