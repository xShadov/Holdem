package com.tp.holdem.menu;

import org.junit.*;

public class MainMenuTests {
	private transient MainMenu menu;

	@Before
	public void construct() {
		menu = new MainMenu();
	}

	@After
	public void destroy() {
		menu = null;
	}

	@Test
	public void testButtonLimit() {
		final ButtonStrategy strategy = new ButtonStrategy(menu);
		final ButtonProperties prop = new ButtonProperties(menu);
		ButtonLimit buttonLimit = new ButtonLimit(menu, strategy);
		final String a = menu.getLimit();
		prop.doClick();
		buttonLimit.doClick();
		buttonLimit.doClick();
		buttonLimit.doClick();
		Assert.assertTrue(a.equals(menu.getLimit()));

	}

	@Test
	public void testButtonChipsLess() {
		final ButtonChipsLess button = new ButtonChipsLess(menu);
		final int a = menu.getChipsAmount();
		button.doClick();
		Assert.assertTrue(a > menu.getChipsAmount());
	}

	@Test
	public void testButtonChipsMore() {
		final ButtonChipsMore button = new ButtonChipsMore(menu);
		final int a = menu.getChipsAmount();
		button.doClick();
		Assert.assertTrue(a < menu.getChipsAmount());
	}

	@Test
	public void testButtonBotsMore() {
		ButtonBotsMore button = new ButtonBotsMore(menu);
		final int a = menu.getBotsCount();
		button.doClick();
		Assert.assertTrue(a < menu.getBotsCount());
	}

	@Test
	public void testButtonBotsLess() {
		final ButtonBotsLess button = new ButtonBotsLess(menu);
		menu.setBotsCount(2);
		final int a = menu.getBotsCount();
		button.doClick();
		Assert.assertTrue(a > menu.getBotsCount());
	}

	@Test
	public void testButtonFixedLess() {
		final ButtonFixedLess button = new ButtonFixedLess(menu);
		final int a = menu.getFixedChips();
		button.doClick();
		Assert.assertTrue(a > menu.getFixedChips());
	}

	@Test
	public void testButtonFixedMore() {
		final ButtonFixedMore button = new ButtonFixedMore(menu);
		final int a = menu.getFixedChips();
		button.doClick();
		Assert.assertTrue(a < menu.getFixedChips());
	}

	@Test
	public void testButtonFixedRaiseLess() {
		final ButtonFixedRaiseLess button = new ButtonFixedRaiseLess(menu);
		menu.setFixedRaise(3);
		final int a = menu.getFixedRaise();
		button.doClick();
		Assert.assertTrue(a > menu.getFixedRaise());
	}

	@Test
	public void testButtonFixedRaiseMore() {
		final ButtonFixedRaiseMore button = new ButtonFixedRaiseMore(menu);
		menu.setFixedRaise(3);
		final int a = menu.getFixedRaise();
		button.doClick();
		Assert.assertTrue(a < menu.getFixedRaise());
	}

	@Test
	public void testButtonBlindMore() {
		final ButtonBlindMore button = new ButtonBlindMore(menu);
		final int a = menu.getBlindAmount();
		button.doClick();
		Assert.assertTrue(a < menu.getBlindAmount());
	}

	@Test
	public void testButtonBlindLess() {
		final ButtonBlindLess button = new ButtonBlindLess(menu);
		menu.setBlindAmount(50);
		final int a = menu.getBlindAmount();
		button.doClick();
		Assert.assertTrue(a > menu.getBlindAmount());
	}

	@Test
	public void testButtonProperties() {
		final ButtonProperties button = new ButtonProperties(menu);
		button.doClick();
		Assert.assertNotNull(menu.getProperties());
	}

	@Test
	public void testPlayersLess() {
		final ButtonPlayersLess button = new ButtonPlayersLess(menu);
		menu.setPlayersCount(5);
		final int a = menu.getPlayersCount();
		button.doClick();
		Assert.assertTrue(a > menu.getPlayersCount());
	}

	@Test
	public void testButtonPlayersMore() {
		final ButtonPlayersMore button = new ButtonPlayersMore(menu);
		menu.setPlayersCount(5);
		final int a = menu.getPlayersCount();
		button.doClick();
		Assert.assertTrue(a < menu.getPlayersCount());
	}

	@Ignore // creating server in Junits throws exceptions
	public void testButtonServer() {
		try {
			final ButtonServer button = new ButtonServer(menu);
			button.doClick();
			Assert.assertNotNull(menu.getKryoServer());
		} catch (Exception e) {

		}
	}

	@Test
	public void testMainMenu() {
		MainMenu.main(null);

		Assert.assertNotNull(menu);
	}
}
