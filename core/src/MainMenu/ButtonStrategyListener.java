package MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.tp.holdem.AllInStrategy;
import com.tp.holdem.Easy;
import com.tp.holdem.FoldStrategy;
import com.tp.holdem.Hard;
import com.tp.holdem.Medium;

class ButtonStrategyListener implements ActionListener
{
	ButtonStrategy button;
	MainMenu menu;
	public ButtonStrategyListener(ButtonStrategy button,MainMenu menu)
	{
		this.button=button;
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getStrategy().getName().equals("Always All-in") )
		{
			menu.setStrategy(new FoldStrategy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if(menu.getStrategy().getName().equals("Always Fold") )
		{
			menu.setStrategy(new Easy());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if(menu.getStrategy().getName().equals("Easy") )
		{
			menu.setStrategy(new Medium());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if(menu.getStrategy().getName().equals("Medium") )
		{
			menu.setStrategy(new Hard());
			button.setText(menu.getStrategy().getName());
			return;
		}
		if(menu.getStrategy().getName().equals("Hard") )
		{
			menu.setStrategy(new AllInStrategy());
			button.setText(menu.getStrategy().getName());
			return;
		}
	}
	
};