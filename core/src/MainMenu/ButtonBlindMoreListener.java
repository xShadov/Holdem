package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonBlindMoreListener implements ActionListener
{
	private MainMenu menu;
	public ButtonBlindMoreListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getFixedChips()>menu.getBlindAmount())
		{
			if(menu.getBlindAmount()<200)
			{
				menu.setBlindAmount(menu.getBlindAmount()+5);
				menu.blindAmount.setText(String.valueOf("Small blind: "+menu.getBlindAmount()));
			}
		}
	}
	
};
