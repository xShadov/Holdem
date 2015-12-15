package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonPlayersLessListener implements ActionListener
{
	private MainMenu menu;
	public ButtonPlayersLessListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getPlayersCount()>0)
		{ 
			if(menu.getPlayersCount()+menu.getBotsCount()>2 && menu.getPlayersCount()>1)
			{
				menu.setPlayersCount(menu.getPlayersCount()-1);
				menu.getPlayersCountLabel().setText(String.valueOf(menu.getPlayersCount()) + " players");
			}
		}
	}
	
};
