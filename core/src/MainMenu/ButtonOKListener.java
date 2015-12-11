package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

class ButtonOKListener implements ActionListener
{
	private JFrame properties;
	public ButtonOKListener(JFrame properties)
	{
		this.properties=properties;
	}
	public void actionPerformed(ActionEvent e)
	{
		properties.dispose();
	}
	
};
