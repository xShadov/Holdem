package com.tp.holdem.menu;

import com.tp.holdem.client.architecture.KryoServer;
import com.tp.holdem.client.strategy.AllInStrategy;
import com.tp.holdem.client.strategy.Strategy;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainMenu extends JFrame {
	private ExecutorService gameExecutor = Executors.newSingleThreadExecutor();
	private Future<?> server;
	private KryoServer kryo;
	private JFrame properties;
	private JPanel propertiesPanel;

	private ButtonServer server1;
	private ButtonProperties properties1;

	private JPanel masterPanel1;
	private GridBagConstraints layout1;

	private ButtonFixedLess fixedLess;
	private ButtonFixedMore fixedMore;
	private ButtonFixedRaiseLess raiseLess;
	private ButtonFixedRaiseMore raiseMore;
	private GridBagConstraints propertiesLayout;

	private ButtonBlindLess blindLess;
	private ButtonBlindMore blindMore;
	private ButtonChipsLess chipsLess;
	private ButtonChipsMore chipsMore;
	private ButtonPlayersLess playersLess;
	private ButtonPlayersMore playersMore;
	private ButtonBotsLess botsLess;
	private ButtonBotsMore botsMore;
	private ButtonStrategy strategy;
	private ButtonOK ok;
	private ButtonLimit limit;

	private boolean layout = false;

	private int playersC = 2;
	private int botsC = 0;
	private String limitt = "no-limit";
	private Strategy botStrategy;
	private int blindA = 20;
	private int playersChips = 1500;
	private int fixedChips = 40;
	private int fixedRaise = 5;
	private Label playersCount = new Label(String.valueOf(playersC) + " players");
	private Label botsCount = new Label(String.valueOf(botsC) + " bots");
	private Label blindAmount = new Label(String.valueOf("Small blind: " + blindA));
	private Label chipsAmount = new Label(String.valueOf("Starting chips: " + playersChips));
	private Label fixedAmount = new Label(String.valueOf("FixedLimit bets: " + fixedChips));
	private Label fixedRaiseLabel = new Label(String.valueOf("Max number of raises: " + fixedRaise));

	public static void main(String args[]) {
		checkThreads();
		MainMenu menu = new MainMenu();
		checkThreads();
		menu.setVisible(true);
	}

	public static void checkThreads() {
		ThreadGroup mainThreadGroup = Thread.currentThread().getThreadGroup();
		ThreadGroup systemThreadGroup = mainThreadGroup.getParent();
		systemThreadGroup.list();
	}

	public void startServer() {
		if (server != null)
			server.cancel(true);
		server = gameExecutor.submit(new Runnable() {
			public void run() {
				try {
					kryo = new KryoServer(playersC, botsC, limitt, botStrategy, blindA, playersChips, fixedChips,
							fixedRaise);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public Label getPlayersCountLabel() {
		return playersCount;
	}

	public Label getBotsCountLabel() {
		return botsCount;
	}

	public Label getBlindAmountLabel() {
		return blindAmount;
	}

	public Label getFixedAmount() {
		return fixedAmount;
	}

	public Label getChipsAmountLabel() {
		return chipsAmount;
	}

	public Label getFixedRaiseLabel() {
		return fixedRaiseLabel;
	}

	public int getFixedRaise() {
		return fixedRaise;
	}

	public void setFixedRaise(int fixedRaise) {
		this.fixedRaise = fixedRaise;
	}

	public boolean getBoolLayout() {
		return layout;
	}

	public void setBoolLayout(boolean layout) {
		this.layout = layout;
	}

	public GridBagConstraints getPropertiesLayout() {
		return propertiesLayout;
	}

	public ButtonFixedLess getFixedLess() {
		return fixedLess;
	}

	public ButtonFixedMore getFixedMore() {
		return fixedMore;
	}

	public ButtonFixedRaiseMore getRaiseMore() {
		return raiseMore;
	}

	public ButtonFixedRaiseLess getRaiseLess() {
		return raiseLess;
	}

	public Future<?> getKryoServer() {
		return server;
	}

	public JPanel getPropPanel() {
		return propertiesPanel;
	}

	public int getFixedChips() {
		return fixedChips;
	}

	public void setFixedChips(int fixedChips) {
		this.fixedChips = fixedChips;
	}

	public int getBlindAmount() {
		return blindA;
	}

	public void setBlindAmount(int blindA) {
		this.blindA = blindA;
	}

	public int getChipsAmount() {
		return playersChips;
	}

	public void setChipsAmount(int playersChips) {
		this.playersChips = playersChips;
	}

	public void setPlayersCount(int playersC) {
		this.playersC = playersC;
	}

	public int getPlayersCount() {
		return playersC;
	}

	public void setBotsCount(int botsC) {
		this.botsC = botsC;
	}

	public int getBotsCount() {
		return botsC;
	}

	public void setLimit(String limit) {
		this.limitt = limit;
	}

	public String getLimit() {
		return limitt;
	}

	public void setStrategy(Strategy botStrategy) {
		this.botStrategy = botStrategy;
	}

	public Strategy getStrategy() {
		return botStrategy;
	}

	public JFrame getProperties() {
		return properties;
	}

	public MainMenu() {
		super("Texas Hold'em");

		setBounds(300, 300, 220, 244);
		addWindowListener(new MyWindowListener());
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

		botStrategy = new AllInStrategy();

		server1 = new ButtonServer(this);
		properties1 = new ButtonProperties(this);

		masterPanel1 = new JPanel(new GridBagLayout());
		layout1 = new GridBagConstraints();

		layout1.fill = GridBagConstraints.BOTH;
		layout1.weightx = 1;
		layout1.weighty = 1;
		layout1.gridx = 0;
		layout1.gridy = 0;
		layout1.gridwidth = 1;
		masterPanel1.add(server1, layout1);
		layout1.gridx = 0;
		layout1.gridy = 1;
		layout1.gridwidth = 1;
		masterPanel1.add(properties1, layout1);

		this.getContentPane().add(masterPanel1);
		setResizable(false);
	}

	public void properties() {
		properties = new JFrame("Properties");
		properties.setBounds(520, 300, 244, 260);
		properties.addWindowListener(new PropertiesWindowListener(properties));

		fixedLess = new ButtonFixedLess(this);
		fixedMore = new ButtonFixedMore(this);
		raiseLess = new ButtonFixedRaiseLess(this);
		raiseMore = new ButtonFixedRaiseMore(this);
		blindLess = new ButtonBlindLess(this);
		blindMore = new ButtonBlindMore(this);
		chipsLess = new ButtonChipsLess(this);
		chipsMore = new ButtonChipsMore(this);
		playersLess = new ButtonPlayersLess(this);
		playersMore = new ButtonPlayersMore(this);
		botsLess = new ButtonBotsLess(this);
		botsMore = new ButtonBotsMore(this);
		strategy = new ButtonStrategy(this);
		ok = new ButtonOK(properties, this);
		limit = new ButtonLimit(this, strategy);

		propertiesLayout = new GridBagConstraints();
		propertiesPanel = new JPanel(new GridBagLayout());

		propertiesLayout.fill = GridBagConstraints.BOTH;
		propertiesLayout.weightx = 1;
		propertiesLayout.weighty = 1;
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 0;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(limit, propertiesLayout);

		if (layout) {
			propertiesLayout.gridx = 0;
			propertiesLayout.gridy = 1;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(fixedLess, propertiesLayout);
			propertiesLayout.gridx = 1;
			propertiesLayout.gridy = 1;
			propertiesLayout.gridwidth = 2;
			propertiesPanel.add(fixedAmount, propertiesLayout);
			propertiesLayout.gridx = 3;
			propertiesLayout.gridy = 1;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(fixedMore, propertiesLayout);

			propertiesLayout.gridx = 0;
			propertiesLayout.gridy = 2;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(raiseLess, propertiesLayout);
			propertiesLayout.gridx = 1;
			propertiesLayout.gridy = 2;
			propertiesLayout.gridwidth = 2;
			propertiesPanel.add(fixedRaiseLabel, propertiesLayout);
			propertiesLayout.gridx = 3;
			propertiesLayout.gridy = 2;
			propertiesLayout.gridwidth = 1;
			propertiesPanel.add(raiseMore, propertiesLayout);
		}

		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 3;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(chipsLess, propertiesLayout);
		propertiesLayout.gridx = 2;
		propertiesLayout.gridy = 3;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(chipsAmount, propertiesLayout);
		propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 3;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(chipsMore, propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 4;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(blindLess, propertiesLayout);
		propertiesLayout.gridx = 2;
		propertiesLayout.gridy = 4;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(blindAmount, propertiesLayout);
		propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 4;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(blindMore, propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 5;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(playersLess, propertiesLayout);
		propertiesLayout.gridx = 2;
		propertiesLayout.gridy = 5;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(playersCount, propertiesLayout);
		propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 5;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(playersMore, propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 6;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(botsLess, propertiesLayout);
		propertiesLayout.gridx = 2;
		propertiesLayout.gridy = 6;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(botsCount, propertiesLayout);
		propertiesLayout.gridx = 3;
		propertiesLayout.gridy = 6;
		propertiesLayout.gridwidth = 1;
		propertiesPanel.add(botsMore, propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 7;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(strategy, propertiesLayout);
		propertiesLayout.gridx = 0;
		propertiesLayout.gridy = 8;
		propertiesLayout.gridwidth = 4;
		propertiesPanel.add(ok, propertiesLayout);

		properties.getContentPane().add(propertiesPanel);
		setResizable(false);
		properties.pack();
		properties.setVisible(true);
	}
}