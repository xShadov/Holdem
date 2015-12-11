package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonBlindLessListener implements ActionListener
{
	private MainMenu menu;
	public ButtonBlindLessListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getBlindAmount()>20)
		{
			menu.setBlindAmount(menu.getBlindAmount()-5);
			menu.blindAmount.setText(String.valueOf("Small blind: "+menu.getBlindAmount()));
		}
	}
	
};
