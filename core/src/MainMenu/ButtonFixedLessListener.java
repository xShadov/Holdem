package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFixedLessListener implements ActionListener
{
	private MainMenu menu;
	public ButtonFixedLessListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getFixedChips()>menu.getBlindAmount())
		{
			menu.setFixedChips(menu.getFixedChips()-5);
			menu.fixedAmount.setText(String.valueOf("FixedLimit bets: "+menu.getFixedChips()));
		}
	}
}
