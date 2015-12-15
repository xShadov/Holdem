package ServerClientTests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.Card;
import com.tp.holdem.Deck;
import com.tp.holdem.Player;
import com.tp.holdem.PokerTable;
import com.tp.holdem.SampleRequest;
import com.tp.holdem.KryoServer;

public class ServerTests {

	Class<?> c;
	Method[] allMethods;
	
	@Before
	public void setUp() throws ClassNotFoundException{
		c = Class.forName("com.tp.holdem.KryoServer");
		allMethods = c.getDeclaredMethods();
	}
	  
	@After
	public void tearDown(){
		c = null;
		allMethods = null;
	}
		
		    
	@Test                                       
	public final void testAllFoldedExceptBetPlayer() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		for(Method methodz : allMethods){
			if(methodz.getName().equals("everyoneFoldedExceptBetPlayer")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(0);
		player1.setFolded(true);
		Player player2 = new Player(1);
		player2.setFolded(false);
		players.add(player1);
		players.add(player2);
		Object t = c.newInstance();
		Object o = method.invoke(t, players);
		assertTrue((Boolean) o);
	}  
	
	@Test                                       
	public final void testTimeToCheckOneWinner() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(500);
    	Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	players.add(player1);
    	players.add(player2);
    	List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Spade"));
    	table.addCard(new Card("9", "Spade"));
    	table.addCard(new Card("8", "Spade"));
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		Field chap2 = c.getDeclaredField("playersWithHiddenCards");
		chap2.setAccessible(true);
		chap2.set(t, playersWithHiddenCards);
		Object o = method.invoke(t, players, table);
		List<Player> oList = (List<Player>) o;
    	assertEquals(1000, oList.get(0).getChipsAmount());
	}  
	
	@Test                                       
	public final void testfindAmountChipsForAllInPot() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("findAmountChipsForAllInPot")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		
		Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(900);
    	Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	players.add(player1);
    	players.add(player2);
    	List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	for(Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				methodz.setAccessible(true);
				Object t = c.newInstance();
				Field chap = c.getDeclaredField("server");
				chap.setAccessible(true);
				chap.set(t, server);
				Field chap2 = c.getDeclaredField("playersWithHiddenCards");
				chap2.setAccessible(true);
				chap2.set(t, playersWithHiddenCards);
				Object o = methodz.invoke(t, players, table);
			}
		}
    	player1.setBetAmount(900);
    	player2.setBetAmount(500);
    	Object t = c.newInstance();
		Object o = method.invoke(t, 0, 900, players);
		assertEquals(1400, Integer.parseInt(o.toString()));
	}  
	
	
	@Test                                       
	public final void testhowManyPeopleInSamePot() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("howManyPeopleInSamePot")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(500);
    	Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	player1.setInGame(true);
    	player2.setInGame(true);
    	players.add(player1);
    	players.add(player2);
    	List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	for(Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				methodz.setAccessible(true);
				Object t = c.newInstance();
				Field chap = c.getDeclaredField("server");
				chap.setAccessible(true);
				chap.set(t, server);
		    	Field chap2 = c.getDeclaredField("playersWithHiddenCards");
				chap2.setAccessible(true);
				chap2.set(t, playersWithHiddenCards);
				Object o = methodz.invoke(t, players, table);
			}
		}
    	Object t = c.newInstance();
		Object o = method.invoke(t, 0, players);
		assertEquals(2, Integer.parseInt(o.toString()));
	} 
	
	@Test                                       
	public final void testFirstAndLastToBet() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("setFirstAndLastToBet")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	Player player3 = new Player(2);
    	player1.setInGame(true);
    	player2.setInGame(true);
    	player1.setAllIn(false);
    	player2.setAllIn(false);
    	player1.setFolded(false);
    	player2.setFolded(true);
    	player3.setFolded(false);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	Object t = c.newInstance();
    	Field chap = c.getDeclaredField("bidingCount");
		chap.setAccessible(true);
		chap.set(t, 1);
		Field chap2 = c.getDeclaredField("numPlayers");
		chap2.setAccessible(true);
		chap2.set(t, 3);
		Field chap3 = c.getDeclaredField("turnPlayer");
		chap3.setAccessible(true);
		chap3.set(t, 0);
		Object o = method.invoke(t, players);
		Field betPlayer = c.getDeclaredField("betPlayer");
		betPlayer.setAccessible(true);
		Field lastToBet = c.getDeclaredField("lastToBet");
		lastToBet.setAccessible(true);
    	assertEquals(2, betPlayer.getInt(t));
    	assertEquals(0, lastToBet.getInt(t));
    	
    	chap.set(t, 2);
    	chap3.set(t, 1);
    	o = method.invoke(t, players);
    	assertEquals(2, betPlayer.getInt(t));
    	assertEquals(0, lastToBet.getInt(t));
	} 
	
	@Test                                       
	public final void testNotEnoughPlayers() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("notEnoughPlayers")){
				method = methodz;
				method.setAccessible(true);
			}
		}
	
		Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	player1.setInGame(false);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	for(Method methodz : allMethods){
			if(methodz.getName().equals("resetAfterRound")){
				methodz.setAccessible(true);
				Object t = c.newInstance();
				Field chap = c.getDeclaredField("server");
				Field chap2 = c.getDeclaredField("handsCounter");
				chap.setAccessible(true);
				chap.set(t, server);
				chap2.setAccessible(true);
				chap2.set(t, 0);
				Object o = methodz.invoke(t, players);
			}
		}
    	Object t = c.newInstance();
		Object o = method.invoke(t, players);
    	assertTrue((Boolean) o);

    	player1.setInGame(true);
    	player2.setInGame(true);
    	o = method.invoke(t, players);
    	assertFalse((Boolean) o);
	} 
	
	@Test                                       
	public final void testNewHand() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("initiateNewHand")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(1);
    	player1.setChipsAmount(1500);
    	Player player2 = new Player(2);
    	player2.setChipsAmount(2500);
    	players.add(player1);
    	players.add(player2);
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("limitType");
		chap.setAccessible(true);
		chap.set(t, "no-limit");
		Field chap2 = c.getDeclaredField("bigBlindAmount");
		chap2.setAccessible(true);
		chap2.set(t, 40);
		Field chap3 = c.getDeclaredField("smallBlindAmount");
		chap3.setAccessible(true);
		chap3.set(t, 20);
		Field chap4 = c.getDeclaredField("server");
		chap4.setAccessible(true);
		chap4.set(t, server);
		Field chap5 = c.getDeclaredField("turnPlayer");
		chap5.setAccessible(true);
		chap5.set(t, 0);
		Field chap6 = c.getDeclaredField("numPlayers");
		chap6.setAccessible(true);
		chap6.set(t, 2);
		Field chap7 = c.getDeclaredField("playersWithHiddenCards");
		chap7.setAccessible(true);
		chap7.set(t, players);
		Object o = method.invoke(t, players);
		Field table = c.getDeclaredField("pokerTable");
		table.setAccessible(true);
		PokerTable pokerTable = (PokerTable) table.get(t);
		Field deckz = c.getDeclaredField("deck");
		deckz.setAccessible(true);
		Deck deck = (Deck) deckz.get(t);
		assertEquals(20, pokerTable.getSmallBlindAmount());
    	assertEquals(40, pokerTable.getBigBlindAmount());
    	assertEquals(48, deck.getCards().size());
    	assertTrue(players.get(0).isHasDealerButton());
    	assertTrue(players.get(0).isHasBigBlind());
    	assertTrue(players.get(1).isHasSmallBlind());
    	assertEquals(40, players.get(0).getBetAmount());
    	assertEquals(40, players.get(0).getBetAmountThisRound());
    	assertEquals(20, players.get(1).getBetAmount());
    	assertEquals(20, players.get(1).getBetAmountThisRound());
    	assertEquals(60, pokerTable.getPot());
    	assertEquals(1, chap5.get(t));
    	Field bid = c.getDeclaredField("bidingCount");
		bid.setAccessible(true);
		int bidingCount = Integer.parseInt(bid.get(t).toString());
    	assertEquals(1, bidingCount);
    	Field bet = c.getDeclaredField("maxBetOnTable");
		bet.setAccessible(true);
		int maxBet = Integer.parseInt(bet.get(t).toString());
    	assertEquals(40, maxBet);
	} 
	
	@Test                                       
	public final void testNewHandWithNoChips() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("initiateNewHand")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(1);
    	player1.setChipsAmount(15);
    	Player player2 = new Player(2);
    	player2.setChipsAmount(10);
    	players.add(player1);
    	players.add(player2);
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("limitType");
		chap.setAccessible(true);
		chap.set(t, "no-limit");
		Field chap2 = c.getDeclaredField("bigBlindAmount");
		chap2.setAccessible(true);
		chap2.set(t, 40);
		Field chap3 = c.getDeclaredField("smallBlindAmount");
		chap3.setAccessible(true);
		chap3.set(t, 20);
		Field chap4 = c.getDeclaredField("server");
		chap4.setAccessible(true);
		chap4.set(t, server);
		Field chap5 = c.getDeclaredField("turnPlayer");
		chap5.setAccessible(true);
		chap5.set(t, 0);
		Field chap6 = c.getDeclaredField("numPlayers");
		chap6.setAccessible(true);
		chap6.set(t, 2);
		Field chap7 = c.getDeclaredField("playersWithHiddenCards");
		chap7.setAccessible(true);
		chap7.set(t, players);
		Object o = method.invoke(t, players);
		Field table = c.getDeclaredField("pokerTable");
		table.setAccessible(true);
		PokerTable pokerTable = (PokerTable) table.get(t);
		Field deckz = c.getDeclaredField("deck");
		deckz.setAccessible(true);
		Deck deck = (Deck) deckz.get(t);
		assertEquals(20, pokerTable.getSmallBlindAmount());
    	assertEquals(40, pokerTable.getBigBlindAmount());
    	assertEquals(48, deck.getCards().size());
    	assertTrue(players.get(0).isHasDealerButton());
    	assertTrue(players.get(0).isHasBigBlind());
    	assertTrue(players.get(1).isHasSmallBlind());
    	assertEquals(15, players.get(0).getBetAmount());
    	assertEquals(15, players.get(0).getBetAmountThisRound());
    	assertEquals(10, players.get(1).getBetAmount());
    	assertEquals(10, players.get(1).getBetAmountThisRound());
    	assertEquals(25, pokerTable.getPot());
    	assertEquals(1, chap5.get(t));
    	Field bid = c.getDeclaredField("bidingCount");
		bid.setAccessible(true);
		int bidingCount = Integer.parseInt(bid.get(t).toString());
    	assertEquals(1, bidingCount);
    	Field bet = c.getDeclaredField("maxBetOnTable");
		bet.setAccessible(true);
		int maxBet = Integer.parseInt(bet.get(t).toString());
    	assertEquals(15, maxBet);
    	assertTrue(players.get(0).isAllIn());
    	assertTrue(players.get(1).isAllIn());
	} 
	
	@Test                                       
	public final void testNewHandWithExactAmountOfChips() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("initiateNewHand")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(1);
    	player1.setChipsAmount(40);
    	Player player2 = new Player(2);
    	player2.setChipsAmount(20);
    	players.add(player1);
    	players.add(player2);
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("limitType");
		chap.setAccessible(true);
		chap.set(t, "no-limit");
		Field chap2 = c.getDeclaredField("bigBlindAmount");
		chap2.setAccessible(true);
		chap2.set(t, 40);
		Field chap3 = c.getDeclaredField("smallBlindAmount");
		chap3.setAccessible(true);
		chap3.set(t, 20);
		Field chap4 = c.getDeclaredField("server");
		chap4.setAccessible(true);
		chap4.set(t, server);
		Field chap5 = c.getDeclaredField("turnPlayer");
		chap5.setAccessible(true);
		chap5.set(t, 0);
		Field chap6 = c.getDeclaredField("numPlayers");
		chap6.setAccessible(true);
		chap6.set(t, 2);
		Field chap7 = c.getDeclaredField("playersWithHiddenCards");
		chap7.setAccessible(true);
		chap7.set(t, players);
		Object o = method.invoke(t, players);
		Field table = c.getDeclaredField("pokerTable");
		table.setAccessible(true);
		PokerTable pokerTable = (PokerTable) table.get(t);
		Field deckz = c.getDeclaredField("deck");
		deckz.setAccessible(true);
		Deck deck = (Deck) deckz.get(t);
		assertEquals(20, pokerTable.getSmallBlindAmount());
    	assertEquals(40, pokerTable.getBigBlindAmount());
    	assertEquals(48, deck.getCards().size());
    	assertTrue(players.get(0).isHasDealerButton());
    	assertTrue(players.get(0).isHasBigBlind());
    	assertTrue(players.get(1).isHasSmallBlind());
    	assertEquals(40, players.get(0).getBetAmount());
    	assertEquals(40, players.get(0).getBetAmountThisRound());
    	assertEquals(20, players.get(1).getBetAmount());
    	assertEquals(20, players.get(1).getBetAmountThisRound());
    	assertEquals(60, pokerTable.getPot());
    	assertEquals(1, chap5.get(t));
    	Field bid = c.getDeclaredField("bidingCount");
		bid.setAccessible(true);
		int bidingCount = Integer.parseInt(bid.get(t).toString());
    	assertEquals(1, bidingCount);
    	Field bet = c.getDeclaredField("maxBetOnTable");
		bet.setAccessible(true);
		int maxBet = Integer.parseInt(bet.get(t).toString());
    	assertEquals(40, maxBet);
	} 
	
	@Test                                       
	public final void testTimeToCheckMultipleWinners() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(500);
    	Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	players.add(player1);
    	players.add(player2);
    	List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		Field chap2 = c.getDeclaredField("playersWithHiddenCards");
		chap2.setAccessible(true);
		chap2.set(t, playersWithHiddenCards);
		Object o = method.invoke(t, players, table);
		List<Player> oList = (List<Player>) o;
    	assertEquals(0, player1.getFromWhichPot());
    	assertEquals(0, player2.getFromWhichPot());
    	assertEquals(500, player1.getChipsAmount());
    	assertEquals(500, player2.getChipsAmount());
	}  
	
	@Test                                       
	public final void testResetAfterRound() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("resetAfterRound")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	player1.setInGame(false);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	Object t = c.newInstance();
    	Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		Field chap2 = c.getDeclaredField("gameStarted");
		chap2.setAccessible(true);
		chap2.set(t, true);
		Object o = method.invoke(t, players);
		Field gs = c.getDeclaredField("gameStarted");
		gs.setAccessible(true);
		boolean gameStarted = gs.getBoolean(t);
    	assertFalse(gameStarted);
    	
    	player1.setInGame(true);
    	player2.setInGame(true);
    	player1.setChipsAmount(1000);
    	player2.setChipsAmount(1000);
    	Field chap3 = c.getDeclaredField("newHand");
		chap3.setAccessible(true);
		chap3.set(t, false);
		Field chap4 = c.getDeclaredField("maxBetOnTable");
		chap4.setAccessible(true);
		chap4.set(t, 0);
		o = method.invoke(t, players);
		Field nh = c.getDeclaredField("newHand");
		nh.setAccessible(true);
		boolean newHand = nh.getBoolean(t);
		Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		int maxBet = Integer.parseInt(mb.get(t).toString());
		assertTrue(newHand);
		assertEquals(0, maxBet);
	}
	
	@Test                                       
	public final void testNextAsBet() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("setNextAsBetPlayer")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	Player player3 = new Player(2);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	Object t = c.newInstance();
    	Field chap3 = c.getDeclaredField("betPlayer");
		chap3.setAccessible(true);
		chap3.set(t, 0);
		Field chap4 = c.getDeclaredField("numPlayers");
		chap4.setAccessible(true);
		chap4.set(t, 3);
		Object o = method.invoke(t, players);
		Field bet = c.getDeclaredField("betPlayer");
		bet.setAccessible(true);
		int betPlayer = Integer.parseInt(bet.get(t).toString());
    	assertEquals(2, betPlayer);
    	chap3.set(t, 2);
    	o = method.invoke(t, players);
    	betPlayer = Integer.parseInt(bet.get(t).toString());
    	assertEquals(0, betPlayer);
	}
	
	@Test                                       
	public final void testHandleReceivedBet() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Player player1 = new Player(0);
		Player player2 = new Player(1);
    	player1.setChipsAmount(500);
    	players.add(player1);
    	players.add(player2);
    	Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		PokerTable table = new PokerTable();
		chap2.set(t, table);
		Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 50);
		Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		List<String> options = new ArrayList<String>();
		options.add("BET");
		Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
		Field chap9 = c.getDeclaredField("numPlayers");
		chap9.setAccessible(true);
		chap9.set(t, 2);
    	SampleRequest response = new SampleRequest("BET", 500, 0);
    	Object o = method.invoke(t, response);
    	assertEquals(500, players.get(0).getBetAmountThisRound());
    	assertEquals(0, players.get(0).getChipsAmount());
    	Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		int maxBet = Integer.parseInt(mb.get(t).toString());
    	assertEquals(500, maxBet);
    	assertEquals(500, table.getPot());
    	assertTrue(players.get(0).isAllIn());
	}
	
	@Test                                       
	public final void testHandleReceivedCall() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Player player1 = new Player(0);
    	player1.setChipsAmount(500);
    	player1.setBetAmountThisRound(200);
    	player1.setFolded(false);
    	Player player2 = new Player(1);
    	player2.setFolded(false);
    	Player player3 = new Player(2);
    	player3.setFolded(false);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		PokerTable table = new PokerTable();
		chap2.set(t, table);
		Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 500);
		Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		List<String> options = new ArrayList<String>();
		options.add("CALL");
		Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
    	SampleRequest response = new SampleRequest("CALL", 0);
    	Object o = method.invoke(t, response);
    	Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		int maxBet = Integer.parseInt(mb.get(t).toString());
		assertEquals(500, players.get(0).getBetAmountThisRound());
    	assertEquals(200, players.get(0).getChipsAmount());
    	assertEquals(500, maxBet);
    	assertEquals(300, table.getPot());
	}
	
	@Test                                       
	public final void testHandleReceivedFold() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Player player1 = new Player(0);
    	players.add(player1);
		Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		List<String> options = new ArrayList<String>();
		options.add("FOLD");
		Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
    	SampleRequest response = new SampleRequest("FOLD", 0);
    	Object o = method.invoke(t, response);
       	assertTrue(players.get(0).isFolded());
	}
	
	@Test                                       
	public final void testHandleReceivedAllIn() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	Player player2 = new Player(1);
    	player1.setChipsAmount(1500);
    	players.add(player1);
    	players.add(player2);
		Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 500);
		Field chap1 = c.getDeclaredField("numPlayers");
		chap1.setAccessible(true);
		chap1.set(t, 2);
		Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		PokerTable table = new PokerTable();
		chap2.set(t, table);
		List<String> options = new ArrayList<String>();
		options.add("ALLIN");
		Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
    	SampleRequest response = new SampleRequest("ALLIN", 0);
    	Object o = method.invoke(t, response);
    	Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		int maxBet = Integer.parseInt(mb.get(t).toString());
		Field lb = c.getDeclaredField("lastToBet");
		lb.setAccessible(true);
		int lastBet = Integer.parseInt(lb.get(t).toString());
    	assertEquals(1500, table.getPot());
    	assertEquals(0, players.get(0).getChipsAmount());
    	assertEquals(1500, maxBet);
    	assertEquals(1, lastBet);
    	assertEquals(1500, players.get(0).getBetAmountThisRound());
    	assertTrue(players.get(0).isAllIn());
	}
	
	@Test                                       
	public final void testHandleReceivedRaise() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	Player player2 = new Player(1);
    	player2.setChipsAmount(1500);
    	players.add(player1);
    	players.add(player2);
		Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 500);
		Field chap1 = c.getDeclaredField("numPlayers");
		chap1.setAccessible(true);
		chap1.set(t, 2);
		Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		PokerTable table = new PokerTable();
		chap2.set(t, table);
		List<String> options = new ArrayList<String>();
		options.add("RAISE");
		Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
    	SampleRequest response = new SampleRequest("RAISE", 200, 0);
    	Object o = method.invoke(t, response);
    	Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		int maxBet = Integer.parseInt(mb.get(t).toString());
		Field lb = c.getDeclaredField("lastToBet");
		lb.setAccessible(true);
		int lastBet = Integer.parseInt(lb.get(t).toString());
		assertEquals(700, table.getPot());
    	assertEquals(800, players.get(0).getChipsAmount());
    	assertEquals(700, maxBet);
    	assertEquals(1, lastBet);
    	assertEquals(700, players.get(0).getBetAmountThisRound());
	}
	
	@Test                                       
	public final void testCheckOptions() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		List<Player> players = new ArrayList<Player>();
		Method method = null;
		Server server = Mockito.mock(Server.class);
		for(Method methodz : allMethods){
			if(methodz.getName().equals("checkPossibleOptions")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		PokerTable table = new PokerTable();
		table.setLimitType("fixed-limit");
		table.setRaiseCount(3);
		table.setFixedRaiseCount(5);
		Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		chap2.set(t, table);
		Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	player1.setBetAmountThisRound(500);
    	players.add(player1);
    	Field chap = c.getDeclaredField("maxBetOnTable");
		chap.setAccessible(true);
		chap.set(t, 500);
		Object o = method.invoke(t, 0, players);
		Field po = c.getDeclaredField("possibleOptions");
		po.setAccessible(true);
		List<String> options = (List<String>) po.get(t);
		assertEquals("RAISE", options.get(0));
		assertEquals("CHECK", options.get(1));
		assertEquals("FOLD", options.get(2));
		
		player1.setBetAmountThisRound(200);
		o = method.invoke(t, 0, players);
		options = (List<String>) po.get(t);
		assertEquals("CALL", options.get(0));
		assertEquals("RAISE", options.get(1));
		assertEquals("FOLD", options.get(2));
		
		player1.setChipsAmount(100);
		player1.setBetAmountThisRound(200);
		o = method.invoke(t, 0, players);
		options = (List<String>) po.get(t);
		assertEquals("ALLIN", options.get(0));
		assertEquals("FOLD", options.get(1));
		
		player1.setChipsAmount(100);
		player1.setBetAmountThisRound(0);
		chap.set(t, 0);
		o = method.invoke(t, 0, players);
		options = (List<String>) po.get(t);
		assertEquals("BET", options.get(0));
		assertEquals("CHECK", options.get(1));
		assertEquals("FOLD", options.get(2));
	}
	
	@Test                                       
	public final void testHandleConnected() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException{
		Server server = Mockito.mock(Server.class);
		Connection conn = Mockito.mock(Connection.class);
		Method method = null;
		for(Method methodz : allMethods){
			if(methodz.getName().equals("handleConnected")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		Field chap1 = c.getDeclaredField("numPlayers");
		chap1.setAccessible(true);
		chap1.set(t, 0);
		Field chap2 = c.getDeclaredField("playersCount");
		chap2.setAccessible(true);
		chap2.set(t, 1);
		Field chap3 = c.getDeclaredField("botsCount");
		chap3.setAccessible(true);
		chap3.set(t, 1);
		Object o = method.invoke(t, conn);
		Field po = c.getDeclaredField("players");
		po.setAccessible(true);
		List<Player> players = (List<Player>) po.get(t);
		assertEquals(2, players.size());
		o = method.invoke(t, conn);
		chap2.set(t, 5);
		players = (List<Player>) po.get(t);
		assertEquals(3, players.size());
	}
	
	@Test                                       
	public final void testHandleDisconnected() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException{
		Server server = Mockito.mock(Server.class);
		List<Player> players = new ArrayList<Player>();
		Player player1 = new Player(0);
		Connection conn = Mockito.mock(Connection.class);
		player1.setConnectionId(conn.getID());
		players.add(player1);
		Method method = null;
		for(Method methodz : allMethods){
			if(methodz.getName().equals("handleDisconnected")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		Object o = method.invoke(t, conn);
		Field po = c.getDeclaredField("gameStarted");
		po.setAccessible(true);
		assertFalse((Boolean)po.get(t));
	}
	
	@Test                                       
	public final void testSendBetResponse() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException{
		Server server = Mockito.mock(Server.class);
		List<Player> players = new ArrayList<Player>();
		Player player1 = new Player(0);
		player1.setBetAmountThisRound(50);
		players.add(player1);
		Method method = null;
		for(Method methodz : allMethods){
			if(methodz.getName().equals("sendBetResponse")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		Object t = c.newInstance();
		Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		PokerTable table = new PokerTable();
		table.setLimitType("no-limit");
		Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		chap2.set(t, table);
		List<String> options = new ArrayList<String>();
		options.add("CALL");
		options.add("FOLD");
		Field chap3 = c.getDeclaredField("possibleOptions");
		chap3.setAccessible(true);
		chap3.set(t, options);
		Field chap4 = c.getDeclaredField("betPlayer");
		chap4.setAccessible(true);
		chap4.set(t, 0);
		Field chap5 = c.getDeclaredField("maxBetOnTable");
		chap5.setAccessible(true);
		chap5.set(t, 100);
		Object o = method.invoke(t, 0, players);
	}
}