package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFixedRaiseLessListener implements ActionListener 
{
	private MainMenu menu;
	public ButtonFixedRaiseLessListener(MainMenu menu)
	{
		this.menu=menu;
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(menu.getFixedRaise()>2)
		{
			menu.setFixedRaise(menu.getFixedRaise()-1);
			menu.getFixedRaiseLabel().setText(String.valueOf("Max number of raises: "+menu.getFixedRaise()));
		}
	}

}
