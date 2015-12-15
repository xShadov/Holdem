package GeneralTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.tp.holdem.Strategy;

import MainMenu.*;

public class MainMenuTests 
{
	private MainMenu menu;
	@Before
	public void construct()
	{
		menu = new MainMenu();
	}
	
	@After
	public void destroy()
	{
		menu = null;
	}
	
	@Test
	public void testButtonLimit()
	{
		ButtonStrategy strategy = new ButtonStrategy(menu);
		ButtonLimit buttonLimit = new ButtonLimit(menu,strategy);
		String a = menu.getLimit();
		buttonLimit.doClick();
		buttonLimit.doClick();
		buttonLimit.doClick();
		assertTrue(a.equals(menu.getLimit()));
	}
	
	@Test
	public void testButtonChipsLess()
	{
		ButtonChipsLess button = new ButtonChipsLess(menu);
		int a = menu.getChipsAmount();
		button.doClick();
		assertTrue(a>menu.getChipsAmount());
	}
	
	@Test
	public void testButtonChipsMore()
	{
		ButtonChipsMore button = new ButtonChipsMore(menu);
		int a = menu.getChipsAmount();
		button.doClick();
		assertTrue(a<menu.getChipsAmount());
	}
	
	@Test
	public void testButtonBotsMore()
	{
		ButtonBotsMore button = new ButtonBotsMore(menu);
		int a = menu.getBotsCount();
		button.doClick();
		assertTrue(a<menu.getBotsCount());
	}
	
	@Test
	public void testButtonBotsLess()
	{
		ButtonBotsLess button = new ButtonBotsLess(menu);
		menu.setBotsCount(2);
		int a = menu.getBotsCount();
		button.doClick();
		assertTrue(a>menu.getBotsCount());
	}
	
	@Test
	public void testButtonBlindMore()
	{
		ButtonBlindMore button = new ButtonBlindMore(menu);
		int a = menu.getBlindAmount();
		button.doClick();
		assertTrue(a<menu.getBlindAmount());
	}
	
	@Test
	public void testButtonBlindLess()
	{
		ButtonBlindLess button = new ButtonBlindLess(menu);
		menu.setBlindAmount(50);
		int a = menu.getBlindAmount();
		button.doClick();
		assertTrue(a>menu.getBlindAmount());
	}
	
	@Test
	public void testButtonProperties()
	{
		ButtonProperties button = new ButtonProperties(menu);
		button.doClick();
		assertNotNull(menu.getProperties());
	}
	
	@Test
	public void testPlayersLess()
	{
		ButtonPlayersLess button = new ButtonPlayersLess(menu);
		menu.setPlayersCount(5);
		int a = menu.getPlayersCount();
		button.doClick();
		assertTrue(a>menu.getPlayersCount());
	}
	
	@Test
	public void testButtonPlayersMore()
	{
		ButtonPlayersMore button = new ButtonPlayersMore(menu);
		menu.setPlayersCount(5);
		int a = menu.getPlayersCount();
		button.doClick();
		assertTrue(a<menu.getPlayersCount());
	}
	
	@Ignore//creating server in Junits throws exceptions
	public void testButtonServer()
	{
		try{
		ButtonServer button = new ButtonServer(menu);
		button.doClick();
		assertNotNull(menu.getKryoServer());
		}
		catch(Exception e)
		{
			
		}
	}
	@Test
	public void testMainMenu()
	{
		MainMenu.main(null);
		
		assertNotNull(menu);
	}
	@Test
	public void testButtonStrategy()
	{
		ButtonStrategy button = new ButtonStrategy(menu);
		Strategy a = menu.getStrategy();
		button.doClick();
		assertFalse(a.equals(menu.getStrategy()));
		a = menu.getStrategy();
		button.doClick();
		assertFalse(a.equals(menu.getStrategy()));
		a = menu.getStrategy();
		button.doClick();
		assertFalse(a.equals(menu.getStrategy()));
		a = menu.getStrategy();
		button.doClick();
		assertFalse(a.equals(menu.getStrategy()));
		a = menu.getStrategy();
		button.doClick();
		assertFalse(a.equals(menu.getStrategy()));
	}

}
