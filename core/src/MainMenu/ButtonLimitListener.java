package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonLimitListener implements ActionListener
{
	private ButtonLimit button;
	private MainMenu menu;
	public ButtonLimitListener(ButtonLimit button,MainMenu menu) 
	{
		this.button=button;
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getLimit().equals("no-limit"))
		{
			button.setText("pot-limit");
			menu.setLimit("pot-limit");
			return;
		}
		if(menu.getLimit().equals("pot-limit"))
		{
			button.setText("fixed-limit");
			menu.setLimit("fixed-limit");
			return;
		}
		if(menu.getLimit().equals("fixed-limit"))
		{
			button.setText("no-limit");
			menu.setLimit("no-limit");
			return;
		}
	}
	
};
