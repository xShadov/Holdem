package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFixedRaiseMoreListener implements ActionListener 
{
	private MainMenu menu;
	public ButtonFixedRaiseMoreListener(MainMenu menu)
	{
		this.menu=menu;
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(menu.getFixedRaise()<5)
		{
			menu.setFixedRaise(menu.getFixedRaise()+1);
			menu.fixedRaiseLabel.setText(String.valueOf("Max number of raises: "+menu.getFixedRaise()));
		}
	}

}
