package MainMenu;

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

import com.tp.holdem.AllInStrategy;
import com.tp.holdem.Easy;
import com.tp.holdem.FoldStrategy;
import com.tp.holdem.Hard;
import com.tp.holdem.KryoServer;
import com.tp.holdem.Medium;
import com.tp.holdem.Strategy;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class MainMenu extends JFrame
{
	private ExecutorService gameExecutor = Executors.newSingleThreadExecutor();
	private Future<?> server;
	private KryoServer kryo;
	private JFrame properties;
	private JPanel propertiesPanel;
	
	private ButtonFixedLess fixedLess;
	private ButtonFixedMore fixedMore;
	private ButtonFixedRaiseLess raiseLess;
	private ButtonFixedRaiseMore raiseMore;
	private GridBagConstraints propertiesLayout;
	
	private boolean layout = false;
	
	private int playersC = 2;
	private int botsC = 0;
	private String limit = "no-limit";
	private Strategy botStrategy;
	private int blindA = 20;
	private int playersChips = 1500;
	private int fixedChips = 40;
	private int fixedRaise = 5;
	public boolean done = false;
	Label playersCount = new Label(String.valueOf(playersC)+ " players");
	Label botsCount = new Label(String.valueOf(botsC)+" bots");
	Label blindAmount = new Label(String.valueOf("Small blind: "+blindA));
	Label chipsAmount = new Label(String.valueOf("Starting chips: "+playersChips));
	Label fixedAmount = new Label(String.valueOf("FixedLimit bets: "+fixedChips));
	Label fixedRaiseLabel = new Label(String.valueOf("Max number of raises: "+fixedRaise));
	
	public static void main(String args[])
	{
		checkThreads();
		MainMenu menu = new MainMenu();
		checkThreads();
		menu.setVisible(true);
	}

	public static void checkThreads()
	{
		ThreadGroup mainThreadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup systemThreadGroup = mainThreadGroup.getParent();
        systemThreadGroup.list();
	}
	
	public void startServer()
	{
		if(server !=null) server.cancel(true);
		server = gameExecutor.submit(new Runnable()
		{
			public void run()
			{
					try {
						kryo = new KryoServer(playersC, botsC, limit,botStrategy,blindA,playersChips,fixedChips,fixedRaise);
						
					} catch (Exception e) {
						e.printStackTrace();
					}

			}
		});
	}
	
	public Label getFixedAmount()
	{
		return fixedAmount;
	}
	
	public Label getFixedRaiseLabel()
	{
		return fixedRaiseLabel;
	}
	
	public int getFixedRaise()
	{
		return fixedRaise;
	}
	
	public void setFixedRaise(int fixedRaise)
	{
		this.fixedRaise=fixedRaise;
	}
	
	public boolean getBoolLayout()
	{
		return layout;
	}
	
	public void setBoolLayout(boolean layout)
	{
		this.layout=layout;
	}
	
	public GridBagConstraints getPropertiesLayout()
	{
		return propertiesLayout;
	}
	
	public ButtonFixedLess getFixedLess()
	{
		return fixedLess;
	}
	
	public ButtonFixedMore getFixedMore()
	{
		return fixedMore;
	}
	
	public ButtonFixedRaiseMore getRaiseMore()
	{
		return raiseMore;
	}
	
	public ButtonFixedRaiseLess getRaiseLess()
	{
		return raiseLess;
	}
	
	public Future<?> getKryoServer()
	{
		return server;
	}
	
	public JPanel getPropPanel()
	{
		return propertiesPanel;
	}
	
	public int getFixedChips()
	{
		return fixedChips;
	}
	
	public void setFixedChips(int fixedChips)
	{
		this.fixedChips=fixedChips;
	}
	
	public int getBlindAmount()
	{
		return blindA;
	}
	
	public void setBlindAmount(int blindA)
	{
		this.blindA=blindA;
	}
	
	public int getChipsAmount()
	{
		return playersChips;
	}
	
	public void setChipsAmount(int playersChips)
	{
		this.playersChips=playersChips;
	}
	
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
	
	public void setStrategy(Strategy botStrategy)
	{
		this.botStrategy = botStrategy;
	}
	
	public Strategy getStrategy()
	{
		return botStrategy;
	}
	
	public JFrame getProperties()
	{
		return properties;
	}
	
	public MainMenu()
	{
		super("Texas Hold'em");
		
		setBounds(300,300,220,244);
		addWindowListener(new MyWindowListener());
		setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
		
		botStrategy = new AllInStrategy();
		
		ButtonServer server = new ButtonServer(this);
		ButtonProperties properties = new ButtonProperties(this);
		
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
        
        this.getContentPane().add(masterPanel);
        setResizable(false);
	}
	
	
	
	public void properties()
	{		
		properties = new JFrame("Properties");
		properties.setBounds(520,300,244,260);
		properties.addWindowListener(new PropertiesWindowListener(properties));

		
		fixedLess = new ButtonFixedLess(this);
		fixedMore = new ButtonFixedMore(this);
		raiseLess = new ButtonFixedRaiseLess(this);
		raiseMore = new ButtonFixedRaiseMore(this);
		ButtonBlindLess blindLess = new ButtonBlindLess(this);
		ButtonBlindMore blindMore = new ButtonBlindMore(this);
		ButtonChipsLess chipsLess = new ButtonChipsLess(this);
		ButtonChipsMore chipsMore = new ButtonChipsMore(this);
		ButtonPlayersLess playersLess = new ButtonPlayersLess(this);
		ButtonPlayersMore playersMore = new ButtonPlayersMore(this);
		ButtonBotsLess botsLess = new ButtonBotsLess(this);
		ButtonBotsMore botsMore = new ButtonBotsMore(this);
		ButtonStrategy strategy = new ButtonStrategy(this);
		ButtonOK ok = new ButtonOK(properties,this);
		ButtonLimit limit = new ButtonLimit(this,strategy);
		
		propertiesLayout = new GridBagConstraints();			
		propertiesPanel = new JPanel(new GridBagLayout());
		
		propertiesLayout.fill = GridBagConstraints.BOTH;
		propertiesLayout.weightx = 1;
		propertiesLayout.weighty = 1;
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 0;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(limit,propertiesLayout);
		
		if(layout)
		{
			propertiesLayout.gridx = 0;
			propertiesLayout.gridy = 1;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(fixedLess,propertiesLayout);
			propertiesLayout.gridx = 1;
			propertiesLayout.gridy = 1;
			propertiesLayout.gridwidth = 2;
			propertiesPanel.add(fixedAmount,propertiesLayout);
			propertiesLayout.gridx = 3;
			propertiesLayout.gridy = 1;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(fixedMore,propertiesLayout);
			
			propertiesLayout.gridx = 0;
			propertiesLayout.gridy = 2;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(raiseLess,propertiesLayout);
			propertiesLayout.gridx = 1;
			propertiesLayout.gridy = 2;
			propertiesLayout.gridwidth = 2;
			propertiesPanel.add(fixedRaiseLabel,propertiesLayout);
			propertiesLayout.gridx = 3;
			propertiesLayout.gridy = 2;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(raiseMore,propertiesLayout);
		}
		
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 3;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(chipsLess,propertiesLayout);
		propertiesLayout.gridx = 2;
        propertiesLayout.gridy = 3;
        propertiesLayout.gridwidth = 1;
        propertiesPanel.add(chipsAmount,propertiesLayout);
        propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 3;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(chipsMore,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 4;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(blindLess,propertiesLayout);
		propertiesLayout.gridx = 2;
        propertiesLayout.gridy = 4;
        propertiesLayout.gridwidth = 1;
        propertiesPanel.add(blindAmount,propertiesLayout);
        propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 4;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(blindMore,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 5;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(playersLess,propertiesLayout);
		propertiesLayout.gridx = 2;
        propertiesLayout.gridy = 5;
        propertiesLayout.gridwidth = 1;
        propertiesPanel.add(playersCount,propertiesLayout);
        propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 5;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(playersMore,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 6;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(botsLess,propertiesLayout);
		propertiesLayout.gridx = 2;
        propertiesLayout.gridy = 6;
        propertiesLayout.gridwidth = 1;
        propertiesPanel.add(botsCount,propertiesLayout);
        propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 6;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(botsMore,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 7;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(strategy,propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 8;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(ok,propertiesLayout);
		
		properties.getContentPane().add(propertiesPanel);
        setResizable(false);
        properties.pack();
        properties.setVisible(true);
	}
}