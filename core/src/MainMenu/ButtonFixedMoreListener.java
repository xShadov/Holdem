package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFixedMoreListener implements ActionListener
{
	private MainMenu menu;
	public ButtonFixedMoreListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getFixedChips()<menu.getChipsAmount())
		{
			menu.setFixedChips(menu.getFixedChips()+5);
			menu.getFixedAmount().setText(String.valueOf("FixedLimit bets: "+menu.getFixedChips()));
		}
	}
}
