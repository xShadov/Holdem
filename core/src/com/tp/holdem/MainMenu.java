package com.tp.holdem;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class MyWindowListener extends WindowAdapter
{
    public void windowClosing(WindowEvent e) {System.exit(0);}
};



class PropertiesWindowListener extends WindowAdapter
{
	JFrame properties;
	PropertiesWindowListener(JFrame properties)
	{
		this.properties=properties;
	}
	public void windowClosing(WindowEvent e) {properties.dispose();}
    public void windowDeactivated(WindowEvent e) {properties.dispose();}
}

//Server starts but MainMenu is waiting for the server to finish what its doing, making menu unusable after start of the server.
class ButtonServerListener implements ActionListener
{
	private static KryoServer kryo;
	
	public ButtonServerListener() {	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		try
		{
			kryo = new KryoServer();
			runServer();			
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	private static void runServer()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				kryo.main(new String[0]);
			}
		}
				
				
									);
	}
	
};

class ButtonServer extends JButton
{
	public ButtonServer()
	{
		super("START SERVER");
		addActionListener(new ButtonServerListener());
	}
};

class ButtonPropertiesListener implements ActionListener
{
	MainMenu menu;
	public ButtonPropertiesListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		menu.properties();
	}
	
};

class ButtonProperties extends JButton
{
	public ButtonProperties(MainMenu menu)
	{
		super("SERVER PROPERTIES");
		addActionListener(new ButtonPropertiesListener(menu));
	}
	
};

class ButtonClientListener implements ActionListener
{
	public ButtonClientListener()
	{
		
	}
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
};

class ButtonClient extends JButton
{
	public ButtonClient()
	{
		super("START CLIENT");
		addActionListener(new ButtonClientListener());
	}
	
};

class ButtonLimitListener implements ActionListener
{
	ButtonLimit button;
	MainMenu menu;
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

class ButtonLimit extends JButton
{
	public ButtonLimit(MainMenu menu)
	{
		super("no-limit");
		addActionListener(new ButtonLimitListener(this,menu));
	}
}

class ButtonPlayersLessListener implements ActionListener
{
	MainMenu menu;
	public ButtonPlayersLessListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getPlayersCount()>0)
		{
			menu.setPlayersCount(menu.getPlayersCount()-1);
			menu.playersCount.setText(String.valueOf(menu.getPlayersCount()));
		}
	}
	
};

class ButtonPlayersLess extends JButton
{
	public ButtonPlayersLess(MainMenu menu)
	{
		super("<");
		addActionListener(new ButtonPlayersLessListener(menu));
	}
}

class ButtonPlayersMoreListener implements ActionListener
{
	MainMenu menu;
	public ButtonPlayersMoreListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getPlayersCount()<10)
		{
			menu.setPlayersCount(menu.getPlayersCount()+1);
			menu.playersCount.setText(String.valueOf(menu.getPlayersCount()));
		}
	}
	
};

class ButtonPlayersMore extends JButton
{
	public ButtonPlayersMore(MainMenu menu)
	{
		super(">");
		addActionListener(new ButtonPlayersMoreListener(menu));
	}
}

class ButtonBotsLessListener implements ActionListener
{
	MainMenu menu;
	public ButtonBotsLessListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getBotsCount()>0)
		{
			menu.setBotsCount(menu.getBotsCount()-1);
			menu.botsCount.setText(String.valueOf(menu.getBotsCount()));
		}
	}
	
};

class ButtonBotsLess extends JButton
{
	
	public ButtonBotsLess(MainMenu menu)
	{
		super("<");
		addActionListener(new ButtonBotsLessListener(menu));
	}
}

class ButtonBotsMoreListener implements ActionListener
{
	MainMenu menu;
	public ButtonBotsMoreListener(MainMenu menu)
	{
		this.menu=menu;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(menu.getBotsCount()<10)
		{
			menu.setBotsCount(menu.getBotsCount()+1);
			menu.botsCount.setText(String.valueOf(menu.getBotsCount()));
		}
	}
	
};

class ButtonBotsMore extends JButton
{
	public ButtonBotsMore(MainMenu menu)
	{
		super(">");
		addActionListener(new ButtonBotsMoreListener(menu));
	}
}

class ButtonOKListener implements ActionListener
{
	JFrame properties;
	public ButtonOKListener(JFrame properties)
	{
		this.properties=properties;
	}
	public void actionPerformed(ActionEvent e)
	{
		properties.dispose();
	}
	
};

class ButtonOK extends JButton
{
	public ButtonOK(JFrame properties)
	{
		super("OK");
		addActionListener(new ButtonOKListener(properties));
	}
}


public class MainMenu extends JFrame
{
	private int playersC = 3;
	private int botsC = 0;
	private String limit = "no-limit";
	Label playersCount = new Label("3");
	Label botsCount = new Label("0");
	
	public void setPlayersCount(int playersC)
	{
		this.playersC = playersC;
	}
	
	public int getPlayersCount()
	{
		return playersC;
	}
	
	public void setBotsCount(int botsC)
	{
		this.botsC = botsC;
	}
	
	public int getBotsCount()
	{
		return botsC;
	}
	
	public void setLimit(String limit)
	{
		this.limit = limit;
	}
	
	public String getLimit()
	{
		return limit;
	}
	
	public MainMenu()
	{
		super("Texas Hold'em");
		
		setBounds(300,300,220,244);
		addWindowListener(new MyWindowListener());
		setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
		
		ButtonServer server = new ButtonServer();
		ButtonProperties properties = new ButtonProperties(this);
		ButtonClient client = new ButtonClient();
		
		JPanel masterPanel = new JPanel(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		
		layout.fill = GridBagConstraints.BOTH;
		layout.weightx = 1;
		layout.weighty = 1;
		layout.gridx = 0;
		layout.gridy = 0;
		layout.gridwidth = 1;
		masterPanel.add(server,layout);
		layout.gridx = 0;
		layout.gridy = 1;
		layout.gridwidth = 1;
        masterPanel.add(properties,layout);
        layout.gridx = 0;
        layout.gridy = 2;
        layout.gridwidth = 1;
        masterPanel.add(client,layout);
        
        this.getContentPane().add(masterPanel);
        setResizable(false);
	}
	
	public void properties()
	{		
		JFrame properties = new JFrame("Properties");
		properties.setBounds(520,300,220,244);
		properties.addWindowListener(new PropertiesWindowListener(properties));

		ButtonLimit limit = new ButtonLimit(this);
		ButtonPlayersLess playersLess = new ButtonPlayersLess(this);
		ButtonPlayersMore playersMore = new ButtonPlayersMore(this);
		ButtonBotsLess botsLess = new ButtonBotsLess(this);
		ButtonBotsMore botsMore = new ButtonBotsMore(this);
		ButtonOK ok = new ButtonOK(properties);
		
		JPanel propertiesPanel = new JPanel(new GridBagLayout());
		GridBagConstraints propertiesLayout = new GridBagConstraints();
		
		propertiesLayout.fill = GridBagConstraints.BOTH;
		propertiesLayout.weightx = 1;
		propertiesLayout.weighty = 1;
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 0;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(limit,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 1;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(playersLess,propertiesLayout);
		propertiesLayout.gridx = 2;
        propertiesLayout.gridy = 1;
        propertiesLayout.gridwidth = 1;
        propertiesPanel.add(playersCount,propertiesLayout);
        propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 1;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(playersMore,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 2;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(botsLess,propertiesLayout);
		propertiesLayout.gridx = 2;
        propertiesLayout.gridy = 2;
        propertiesLayout.gridwidth = 1;
        propertiesPanel.add(botsCount,propertiesLayout);
        propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 2;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(botsMore,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 4;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(ok,propertiesLayout);
		
		properties.getContentPane().add(propertiesPanel);
        setResizable(false);
        properties.setVisible(true);
	}
	
	public static void main(String args[])
	{
		MainMenu menu = new MainMenu();
		menu.setVisible(true);
	}
	
	
}
