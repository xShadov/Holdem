package ServerClientTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tp.holdem.AllInStrategy;
import com.tp.holdem.Card;
import com.tp.holdem.Easy;
import com.tp.holdem.FoldStrategy;
import com.tp.holdem.Hard;
import com.tp.holdem.Medium;

public class BotsTests 
{
	private static final String FOLD = "FOLD";
	private static final String BET = "BET";
	private static final String RAISE = "RAISE";
	private static final String ALLIN = "ALLIN";
	private static final String CHECK = "CHECK";
	private static final String CALL = "CALL";
	private transient final List<Card> hand = new ArrayList<Card>();
	private final List<Card> tableCards = new ArrayList<Card>();
	final private String[] suits = {"Spade", "Heart", "Diamond", "Club"};
  	final private String[] honours = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"}; 
  	
	@Before
	public void setUp()
	{
		hand.clear();
		tableCards.clear();
	}
	
	@Test
	public void testMedium()
	{
		final FakeServer server = new FakeServer(20, 10); // maxBet , bigBlind
		final Medium bot = new Medium();
		
		hand.add(new Card(honours[1],suits[1]));
		hand.add(new Card(honours[1],suits[2]));
		server.setCardsOnTable(tableCards);
		
		bot.whatDoIDo(server, hand, 20, 20);  //ilePostawilem, ileMam
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		
		//Flop
		tableCards.add(new Card(honours[1],suits[3]));
		tableCards.add(new Card(honours[3],suits[1]));
		tableCards.add(new Card(honours[4],suits[1]));
		server.setCardsOnTable(tableCards);

		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[2],suits[1]));
		tableCards.add(new Card(honours[4],suits[1]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(CHECK));
	}
	
	@Test
	public void testEasy()
	{
		FakeServer server = new FakeServer(20, 0);
		Easy bot = new Easy();
		hand.add(new Card(honours[1],suits[1]));
		hand.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		server.setCardsOnTable(tableCards);
		bot.whatDoIDo(server, hand, 20, 20);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
	}
	
	@Test
	public void testHard()
	{
		FakeServer server = new FakeServer(20, 10); // maxBet , bigBlind
		final Hard bot = new Hard();
		
		hand.add(new Card(honours[1],suits[1]));
		hand.add(new Card(honours[2],suits[2]));
		server.setCardsOnTable(tableCards);
		
		bot.whatDoIDo(server, hand, 20, 20);  //ilePostawilem, ileMam
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		
		//Flop
		tableCards.add(new Card(honours[3],suits[3]));
		tableCards.add(new Card(honours[4],suits[1]));
		tableCards.add(new Card(honours[5],suits[1]));
		server.setCardsOnTable(tableCards);

		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[2],suits[1]));
		tableCards.add(new Card(honours[4],suits[1]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(CHECK));
		
		//Turn
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[2],suits[1]));
		tableCards.add(new Card(honours[4],suits[1]));
		tableCards.add(new Card(honours[4],suits[1]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[2],suits[1]));
		tableCards.add(new Card(honours[5],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[3],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[3],suits[1]));
		tableCards.add(new Card(honours[6],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));
		
		tableCards.clear();
		tableCards.add(new Card(honours[4],suits[3]));
		tableCards.add(new Card(honours[6],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		tableCards.add(new Card(honours[10],suits[1]));
		server.setCardsOnTable(tableCards);
		server.setMaxBetOnTable(0);
		while(!bot.getTag().equals(ALLIN))
			bot.whatDoIDo(server, hand, 0, 9);
		while(!bot.getTag().equals(BET))
			bot.whatDoIDo(server, hand, 0, 11);
		server.setMaxBetOnTable(10);
		while(!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 200);
		while(!bot.getTag().equals(CHECK))
			bot.whatDoIDo(server, hand, 10, 200);
		while(!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 9);

		
		//River
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[2],suits[1]));
		tableCards.add(new Card(honours[4],suits[1]));
		tableCards.add(new Card(honours[4],suits[1]));
		tableCards.add(new Card(honours[4],suits[2]));
		server.setCardsOnTable(tableCards);
		
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(RAISE));
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[2],suits[1]));
		tableCards.add(new Card(honours[5],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		tableCards.add(new Card(honours[4],suits[2]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 20, 11);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(ALLIN));
		bot.whatDoIDo(server, hand, 0, 21);
		assertTrue(bot.getTag().equals(CALL));
		server.setMaxBetOnTable(0);
		bot.whatDoIDo(server, hand, 0, 19);
		assertTrue(bot.getTag().equals(BET));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[3],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		tableCards.add(new Card(honours[4],suits[2]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));
		
		tableCards.clear();
		tableCards.add(new Card(honours[2],suits[3]));
		tableCards.add(new Card(honours[3],suits[1]));
		tableCards.add(new Card(honours[6],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		tableCards.add(new Card(honours[4],suits[2]));
		server.setCardsOnTable(tableCards);
		
		server.setMaxBetOnTable(20);
		bot.whatDoIDo(server, hand, 20, 9);
		assertTrue(bot.getTag().equals(CHECK));
		bot.whatDoIDo(server, hand, 0, 9);
		assertTrue(bot.getTag().equals(FOLD));
		
		tableCards.clear();
		tableCards.add(new Card(honours[4],suits[3]));
		tableCards.add(new Card(honours[6],suits[1]));
		tableCards.add(new Card(honours[8],suits[1]));
		tableCards.add(new Card(honours[10],suits[1]));
		tableCards.add(new Card(honours[9],suits[2]));
		server.setCardsOnTable(tableCards);
		server.setMaxBetOnTable(0);
		while(!bot.getTag().equals(ALLIN))
			bot.whatDoIDo(server, hand, 0, 9);
		while(!bot.getTag().equals(BET))
			bot.whatDoIDo(server, hand, 0, 11);
		server.setMaxBetOnTable(10);
		while(!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 200);
		while(!bot.getTag().equals(CHECK))
			bot.whatDoIDo(server, hand, 10, 200);
		while(!bot.getTag().equals(FOLD))
			bot.whatDoIDo(server, hand, 0, 9);
	}
	
	@Test
	public void testAllInStrategy()
	{
		FakeServer server = new FakeServer(20, 0);
		AllInStrategy bot = new AllInStrategy();
		hand.add(new Card(honours[1],suits[1]));
		hand.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		server.setCardsOnTable(tableCards);
		bot.whatDoIDo(server, hand, 20, 20);
		assertTrue(bot.getTag().equals(ALLIN));
	}
	
	@Test
	public void testAlwaysFold()
	{
		FakeServer server = new FakeServer(20, 0);
		FoldStrategy bot = new FoldStrategy();
		hand.add(new Card(honours[1],suits[1]));
		hand.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		tableCards.add(new Card(honours[1],suits[1]));
		server.setCardsOnTable(tableCards);
		bot.whatDoIDo(server, hand, 20, 20);
		assertTrue(bot.getTag().equals(FOLD));
	}
}
