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
import org.mockito.Mockito;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.tp.holdem.Card;
import com.tp.holdem.Deck;
import com.tp.holdem.Player;
import com.tp.holdem.PokerTable;
import com.tp.holdem.SampleRequest;

public class ServerTests {

	transient Class<?> c;
	transient Method[] allMethods;
	
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
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("everyoneFoldedExceptBetPlayer")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(0);
		player1.setFolded(true);
		final Player player2 = new Player(1);
		player2.setFolded(false);
		players.add(player1);
		players.add(player2);
		final Object t = c.newInstance();
		final Object o = method.invoke(t, players);
		assertTrue((Boolean) o);
	}  
	
	@Test                                       
	public final void testTimeToCheckOneWinner() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(500);
    	final Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	players.add(player1);
    	players.add(player2);
    	final List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	final PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Spade"));
    	table.addCard(new Card("9", "Spade"));
    	table.addCard(new Card("8", "Spade"));
    	final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final Field chap2 = c.getDeclaredField("playersWithHiddenCards");
		chap2.setAccessible(true);
		chap2.set(t, playersWithHiddenCards);
		final Object o = method.invoke(t, players, table);
		final List<Player> oList = (List<Player>) o;
    	assertEquals(1000, oList.get(0).getChipsAmount());
	}  
	
	@Test                                       
	public final void testfindAmountChipsForAllInPot() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("findAmountChipsForAllInPot")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		
		final Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(900);
    	final Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	players.add(player1);
    	players.add(player2);
    	final List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	final PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	for(final Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				methodz.setAccessible(true);
				final Object t = c.newInstance();
				final Field chap = c.getDeclaredField("server");
				chap.setAccessible(true);
				chap.set(t, server);
				final Field chap2 = c.getDeclaredField("playersWithHiddenCards");
				chap2.setAccessible(true);
				chap2.set(t, playersWithHiddenCards);
				final Object o = methodz.invoke(t, players, table);
			}
		}
    	player1.setBetAmount(900);
    	player2.setBetAmount(500);
    	final Object t = c.newInstance();
    	final Object o = method.invoke(t, 0, 900, players);
		assertEquals(1400, Integer.parseInt(o.toString()));
	}  
	
	
	@Test                                       
	public final void testhowManyPeopleInSamePot() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("howManyPeopleInSamePot")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(500);
    	final Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	player1.setInGame(true);
    	player2.setInGame(true);
    	players.add(player1);
    	players.add(player2);
    	final List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	final PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	for(final Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				methodz.setAccessible(true);
				final Object t = c.newInstance();
				final Field chap = c.getDeclaredField("server");
				chap.setAccessible(true);
				chap.set(t, server);
				final Field chap2 = c.getDeclaredField("playersWithHiddenCards");
				chap2.setAccessible(true);
				chap2.set(t, playersWithHiddenCards);
				final Object o = methodz.invoke(t, players, table);
			}
		}
    	final Object t = c.newInstance();
    	final Object o = method.invoke(t, 0, players);
		assertEquals(2, Integer.parseInt(o.toString()));
	} 
	
	@Test                                       
	public final void testFirstAndLastToBet() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("setFirstAndLastToBet")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(0);
		final Player player2 = new Player(1);
    	final Player player3 = new Player(2);
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
    	final Object t = c.newInstance();
    	final Field chap = c.getDeclaredField("bidingCount");
		chap.setAccessible(true);
		chap.set(t, 1);
		final Field chap2 = c.getDeclaredField("numPlayers");
		chap2.setAccessible(true);
		chap2.set(t, 3);
		final Field chap3 = c.getDeclaredField("turnPlayer");
		chap3.setAccessible(true);
		chap3.set(t, 0);
		Object o = method.invoke(t, players);
		final Field betPlayer = c.getDeclaredField("betPlayer");
		betPlayer.setAccessible(true);
		final Field lastToBet = c.getDeclaredField("lastToBet");
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
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("notEnoughPlayers")){
				method = methodz;
				method.setAccessible(true);
			}
		}
	
		final Player player1 = new Player(0);
		final Player player2 = new Player(1);
    	player1.setInGame(false);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	for(final Method methodz : allMethods){
			if(methodz.getName().equals("resetAfterRound")){
				methodz.setAccessible(true);
				final Object t = c.newInstance();
				final Field chap = c.getDeclaredField("server");
				final Field chap2 = c.getDeclaredField("handsCounter");
				chap.setAccessible(true);
				chap.set(t, server);
				chap2.setAccessible(true);
				chap2.set(t, 0);
				final Object o = methodz.invoke(t, players);
			}
		}
    	final Object t = c.newInstance();
		Object o = method.invoke(t, players);
    	assertTrue((Boolean) o);

    	player1.setInGame(true);
    	player2.setInGame(true);
    	o = method.invoke(t, players);
    	assertFalse((Boolean) o);
	} 
	
	@Test                                       
	public final void testNewHand() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("initiateNewHand")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(1);
    	player1.setChipsAmount(1500);
    	final Player player2 = new Player(2);
    	player2.setChipsAmount(2500);
    	players.add(player1);
    	players.add(player2);
    	final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("limitType");
		chap.setAccessible(true);
		chap.set(t, "no-limit");
		final Field chap2 = c.getDeclaredField("bigBlindAmount");
		chap2.setAccessible(true);
		chap2.set(t, 40);
		final Field chap3 = c.getDeclaredField("smallBlindAmount");
		chap3.setAccessible(true);
		chap3.set(t, 20);
		final Field chap4 = c.getDeclaredField("server");
		chap4.setAccessible(true);
		chap4.set(t, server);
		final Field chap5 = c.getDeclaredField("turnPlayer");
		chap5.setAccessible(true);
		chap5.set(t, 0);
		final Field chap6 = c.getDeclaredField("numPlayers");
		chap6.setAccessible(true);
		chap6.set(t, 2);
		final Field chap7 = c.getDeclaredField("playersWithHiddenCards");
		chap7.setAccessible(true);
		chap7.set(t, players);
		final Object o = method.invoke(t, players);
		final Field table = c.getDeclaredField("pokerTable");
		table.setAccessible(true);
		final PokerTable pokerTable = (PokerTable) table.get(t);
		final Field deckz = c.getDeclaredField("deck");
		deckz.setAccessible(true);
		final Deck deck = (Deck) deckz.get(t);
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
    	final Field bid = c.getDeclaredField("bidingCount");
		bid.setAccessible(true);
		final int bidingCount = Integer.parseInt(bid.get(t).toString());
    	assertEquals(1, bidingCount);
    	final Field bet = c.getDeclaredField("maxBetOnTable");
		bet.setAccessible(true);
		final int maxBet = Integer.parseInt(bet.get(t).toString());
    	assertEquals(40, maxBet);
	} 
	
	@Test                                       
	public final void testNewHandWithNoChips() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("initiateNewHand")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(1);
    	player1.setChipsAmount(15);
    	final Player player2 = new Player(2);
    	player2.setChipsAmount(10);
    	players.add(player1);
    	players.add(player2);
    	final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("limitType");
		chap.setAccessible(true);
		chap.set(t, "no-limit");
		final Field chap2 = c.getDeclaredField("bigBlindAmount");
		chap2.setAccessible(true);
		chap2.set(t, 40);
		final Field chap3 = c.getDeclaredField("smallBlindAmount");
		chap3.setAccessible(true);
		chap3.set(t, 20);
		final Field chap4 = c.getDeclaredField("server");
		chap4.setAccessible(true);
		chap4.set(t, server);
		final Field chap5 = c.getDeclaredField("turnPlayer");
		chap5.setAccessible(true);
		chap5.set(t, 0);
		final Field chap6 = c.getDeclaredField("numPlayers");
		chap6.setAccessible(true);
		chap6.set(t, 2);
		final Field chap7 = c.getDeclaredField("playersWithHiddenCards");
		chap7.setAccessible(true);
		chap7.set(t, players);
		final Object o = method.invoke(t, players);
		final Field table = c.getDeclaredField("pokerTable");
		table.setAccessible(true);
		final PokerTable pokerTable = (PokerTable) table.get(t);
		final Field deckz = c.getDeclaredField("deck");
		deckz.setAccessible(true);
		final Deck deck = (Deck) deckz.get(t);
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
    	final Field bid = c.getDeclaredField("bidingCount");
		bid.setAccessible(true);
		final int bidingCount = Integer.parseInt(bid.get(t).toString());
    	assertEquals(1, bidingCount);
    	final Field bet = c.getDeclaredField("maxBetOnTable");
		bet.setAccessible(true);
		final int maxBet = Integer.parseInt(bet.get(t).toString());
    	assertEquals(15, maxBet);
    	assertTrue(players.get(0).isAllIn());
    	assertTrue(players.get(1).isAllIn());
	} 
	
	@Test                                       
	public final void testNewHandWithExactAmountOfChips() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("initiateNewHand")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(1);
    	player1.setChipsAmount(40);
    	final Player player2 = new Player(2);
    	player2.setChipsAmount(20);
    	players.add(player1);
    	players.add(player2);
    	final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("limitType");
		chap.setAccessible(true);
		chap.set(t, "no-limit");
		final Field chap2 = c.getDeclaredField("bigBlindAmount");
		chap2.setAccessible(true);
		chap2.set(t, 40);
		final Field chap3 = c.getDeclaredField("smallBlindAmount");
		chap3.setAccessible(true);
		chap3.set(t, 20);
		final Field chap4 = c.getDeclaredField("server");
		chap4.setAccessible(true);
		chap4.set(t, server);
		final Field chap5 = c.getDeclaredField("turnPlayer");
		chap5.setAccessible(true);
		chap5.set(t, 0);
		final Field chap6 = c.getDeclaredField("numPlayers");
		chap6.setAccessible(true);
		chap6.set(t, 2);
		final Field chap7 = c.getDeclaredField("playersWithHiddenCards");
		chap7.setAccessible(true);
		chap7.set(t, players);
		final Object o = method.invoke(t, players);
		final Field table = c.getDeclaredField("pokerTable");
		table.setAccessible(true);
		final PokerTable pokerTable = (PokerTable) table.get(t);
		final Field deckz = c.getDeclaredField("deck");
		deckz.setAccessible(true);
		final Deck deck = (Deck) deckz.get(t);
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
    	final Field bid = c.getDeclaredField("bidingCount");
		bid.setAccessible(true);
		final int bidingCount = Integer.parseInt(bid.get(t).toString());
    	assertEquals(1, bidingCount);
    	final Field bet = c.getDeclaredField("maxBetOnTable");
		bet.setAccessible(true);
		final int maxBet = Integer.parseInt(bet.get(t).toString());
    	assertEquals(40, maxBet);
	} 
	
	@Test                                       
	public final void testTimeToCheckMultipleWinners() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("timeToCheckWinner")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(500);
    	final Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	players.add(player1);
    	players.add(player2);
    	final List<Player> playersWithHiddenCards = new ArrayList<Player>(players.size());
    	playersWithHiddenCards.add(new Player());
    	playersWithHiddenCards.add(new Player());
    	final PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final Field chap2 = c.getDeclaredField("playersWithHiddenCards");
		chap2.setAccessible(true);
		chap2.set(t, playersWithHiddenCards);
		final Object o = method.invoke(t, players, table);
    	assertEquals(0, player1.getFromWhichPot());
    	assertEquals(0, player2.getFromWhichPot());
    	assertEquals(500, player1.getChipsAmount());
    	assertEquals(500, player2.getChipsAmount());
	}  
	
	@Test                                       
	public final void testResetAfterRound() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("resetAfterRound")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(0);
		final Player player2 = new Player(1);
    	player1.setInGame(false);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	final Object t = c.newInstance();
    	final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final Field chap2 = c.getDeclaredField("gameStarted");
		chap2.setAccessible(true);
		chap2.set(t, true);
		Object o = method.invoke(t, players);
		final Field gs = c.getDeclaredField("gameStarted");
		gs.setAccessible(true);
		final boolean gameStarted = gs.getBoolean(t);
    	assertFalse(gameStarted);
    	
    	player1.setInGame(true);
    	player2.setInGame(true);
    	player1.setChipsAmount(1000);
    	player2.setChipsAmount(1000);
    	final Field chap3 = c.getDeclaredField("newHand");
		chap3.setAccessible(true);
		chap3.set(t, false);
		final Field chap4 = c.getDeclaredField("maxBetOnTable");
		chap4.setAccessible(true);
		chap4.set(t, 0);
		o = method.invoke(t, players);
		final Field nh = c.getDeclaredField("newHand");
		nh.setAccessible(true);
		final boolean newHand = nh.getBoolean(t);
		final Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		final int maxBet = Integer.parseInt(mb.get(t).toString());
		assertTrue(newHand);
		assertEquals(0, maxBet);
	}
	
	@Test                                       
	public final void testNextAsBet() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("setNextAsBetPlayer")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Player player1 = new Player(0);
		final Player player2 = new Player(1);
    	final Player player3 = new Player(2);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	final Object t = c.newInstance();
    	final Field chap3 = c.getDeclaredField("betPlayer");
		chap3.setAccessible(true);
		chap3.set(t, 0);
		final Field chap4 = c.getDeclaredField("numPlayers");
		chap4.setAccessible(true);
		chap4.set(t, 3);
		Object o = method.invoke(t, players);
		final Field bet = c.getDeclaredField("betPlayer");
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
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Player player1 = new Player(0);
		final Player player2 = new Player(1);
    	player1.setChipsAmount(500);
    	players.add(player1);
    	players.add(player2);
    	final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		final PokerTable table = new PokerTable();
		chap2.set(t, table);
		final Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 50);
		final Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		final List<String> options = new ArrayList<String>();
		options.add("BET");
		final Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		final Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		final Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		final Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
		final Field chap9 = c.getDeclaredField("numPlayers");
		chap9.setAccessible(true);
		chap9.set(t, 2);
		final SampleRequest response = new SampleRequest("BET", 500, 0);
		final Object o = method.invoke(t, response);
    	assertEquals(500, players.get(0).getBetAmountThisRound());
    	assertEquals(0, players.get(0).getChipsAmount());
    	final Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		final int maxBet = Integer.parseInt(mb.get(t).toString());
    	assertEquals(500, maxBet);
    	assertEquals(500, table.getPot());
    	assertTrue(players.get(0).isAllIn());
	}
	
	@Test                                       
	public final void testHandleReceivedCall() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Player player1 = new Player(0);
    	player1.setChipsAmount(500);
    	player1.setBetAmountThisRound(200);
    	player1.setFolded(false);
    	final Player player2 = new Player(1);
    	player2.setFolded(false);
    	final Player player3 = new Player(2);
    	player3.setFolded(false);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		final PokerTable table = new PokerTable();
		chap2.set(t, table);
		final Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 500);
		final Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		final List<String> options = new ArrayList<String>();
		options.add("CALL");
		final Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		final Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		final Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		final Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
		final SampleRequest response = new SampleRequest("CALL", 0);
		final Object o = method.invoke(t, response);
    	final Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		final int maxBet = Integer.parseInt(mb.get(t).toString());
		assertEquals(500, players.get(0).getBetAmountThisRound());
    	assertEquals(200, players.get(0).getChipsAmount());
    	assertEquals(500, maxBet);
    	assertEquals(300, table.getPot());
	}
	
	@Test                                       
	public final void testHandleReceivedFold() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Player player1 = new Player(0);
    	players.add(player1);
    	final Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		final Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		final List<String> options = new ArrayList<String>();
		options.add("FOLD");
		final Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		final Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		final Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
		final SampleRequest response = new SampleRequest("FOLD", 0);
		final Object o = method.invoke(t, response);
       	assertTrue(players.get(0).isFolded());
	}
	
	@Test                                       
	public final void testHandleReceivedAllIn() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	final Player player2 = new Player(1);
    	player1.setChipsAmount(1500);
    	players.add(player1);
    	players.add(player2);
    	final Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		final Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		final Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 500);
		final Field chap1 = c.getDeclaredField("numPlayers");
		chap1.setAccessible(true);
		chap1.set(t, 2);
		final Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		final PokerTable table = new PokerTable();
		chap2.set(t, table);
		final List<String> options = new ArrayList<String>();
		options.add("ALLIN");
		final Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		final Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		final Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
		final SampleRequest response = new SampleRequest("ALLIN", 0);
		final Object o = method.invoke(t, response);
    	final Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		final int maxBet = Integer.parseInt(mb.get(t).toString());
		final Field lb = c.getDeclaredField("lastToBet");
		lb.setAccessible(true);
		final int lastBet = Integer.parseInt(lb.get(t).toString());
    	assertEquals(1500, table.getPot());
    	assertEquals(0, players.get(0).getChipsAmount());
    	assertEquals(1500, maxBet);
    	assertEquals(1, lastBet);
    	assertEquals(1500, players.get(0).getBetAmountThisRound());
    	assertTrue(players.get(0).isAllIn());
	}
	
	@Test                                       
	public final void testHandleReceivedRaise() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("handleReceived")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	final Player player2 = new Player(1);
    	player2.setChipsAmount(1500);
    	players.add(player1);
    	players.add(player2);
    	final Field chap4 = c.getDeclaredField("players");
		chap4.setAccessible(true);
		chap4.set(t, players);
		final Field chap6 = c.getDeclaredField("bidingTime");
		chap6.setAccessible(true);
		chap6.set(t, true);
		final Field chap3 = c.getDeclaredField("maxBetOnTable");
		chap3.setAccessible(true);
		chap3.set(t, 500);
		final Field chap1 = c.getDeclaredField("numPlayers");
		chap1.setAccessible(true);
		chap1.set(t, 2);
		final Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		final PokerTable table = new PokerTable();
		chap2.set(t, table);
		final List<String> options = new ArrayList<String>();
		options.add("RAISE");
		final Field chap5 = c.getDeclaredField("possibleOptions");
		chap5.setAccessible(true);
		chap5.set(t, options);
		final Field chap7 = c.getDeclaredField("bidingOver");
		chap7.setAccessible(true);
		chap7.set(t, false);
		final Field chap8 = c.getDeclaredField("betPlayer");
		chap8.setAccessible(true);
		chap8.set(t, 0);
		final SampleRequest response = new SampleRequest("RAISE", 200, 0);
		final Object o = method.invoke(t, response);
    	final Field mb = c.getDeclaredField("maxBetOnTable");
		mb.setAccessible(true);
		final int maxBet = Integer.parseInt(mb.get(t).toString());
		final Field lb = c.getDeclaredField("lastToBet");
		lb.setAccessible(true);
		final int lastBet = Integer.parseInt(lb.get(t).toString());
		assertEquals(700, table.getPot());
    	assertEquals(800, players.get(0).getChipsAmount());
    	assertEquals(700, maxBet);
    	assertEquals(1, lastBet);
    	assertEquals(700, players.get(0).getBetAmountThisRound());
	}
	
	@Test                                       
	public final void testCheckOptions() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException{ 
		final List<Player> players = new ArrayList<Player>();
		Method method = null;
		final Server server = Mockito.mock(Server.class);
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("checkPossibleOptions")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final PokerTable table = new PokerTable();
		table.setLimitType("fixed-limit");
		table.setRaiseCount(3);
		table.setFixedRaiseCount(5);
		final Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		chap2.set(t, table);
		final Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	player1.setBetAmountThisRound(500);
    	players.add(player1);
    	final Field chap = c.getDeclaredField("maxBetOnTable");
		chap.setAccessible(true);
		chap.set(t, 500);
		Object o = method.invoke(t, 0, players);
		final Field po = c.getDeclaredField("possibleOptions");
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
		final Server server = Mockito.mock(Server.class);
		final Connection conn = Mockito.mock(Connection.class);
		Method method = null;
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("handleConnected")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final Field chap1 = c.getDeclaredField("numPlayers");
		chap1.setAccessible(true);
		chap1.set(t, 0);
		final Field chap2 = c.getDeclaredField("playersCount");
		chap2.setAccessible(true);
		chap2.set(t, 1);
		final Field chap3 = c.getDeclaredField("botsCount");
		chap3.setAccessible(true);
		chap3.set(t, 1);
		Object o = method.invoke(t, conn);
		final Field po = c.getDeclaredField("players");
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
		final Server server = Mockito.mock(Server.class);
		final List<Player> players = new ArrayList<Player>();
		final Player player1 = new Player(0);
		final Connection conn = Mockito.mock(Connection.class);
		player1.setConnectionId(conn.getID());
		players.add(player1);
		Method method = null;
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("handleDisconnected")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final Object o = method.invoke(t, conn);
		final Field po = c.getDeclaredField("gameStarted");
		po.setAccessible(true);
		assertFalse((Boolean)po.get(t));
	}
	
	@Test                                       
	public final void testSendBetResponse() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException, InstantiationException{
		final Server server = Mockito.mock(Server.class);
		final List<Player> players = new ArrayList<Player>();
		final Player player1 = new Player(0);
		player1.setBetAmountThisRound(50);
		players.add(player1);
		Method method = null;
		for(final Method methodz : allMethods){
			if(methodz.getName().equals("sendBetResponse")){
				method = methodz;
				method.setAccessible(true);
			}
		}
		final Object t = c.newInstance();
		final Field chap = c.getDeclaredField("server");
		chap.setAccessible(true);
		chap.set(t, server);
		final PokerTable table = new PokerTable();
		table.setLimitType("no-limit");
		final Field chap2 = c.getDeclaredField("pokerTable");
		chap2.setAccessible(true);
		chap2.set(t, table);
		final List<String> options = new ArrayList<String>();
		options.add("CALL");
		options.add("FOLD");
		final Field chap3 = c.getDeclaredField("possibleOptions");
		chap3.setAccessible(true);
		chap3.set(t, options);
		final Field chap4 = c.getDeclaredField("betPlayer");
		chap4.setAccessible(true);
		chap4.set(t, 0);
		final Field chap5 = c.getDeclaredField("maxBetOnTable");
		chap5.setAccessible(true);
		chap5.set(t, 100);
		final Object o = method.invoke(t, 0, players);
	}
}