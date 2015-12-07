package Tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tp.holdem.AllInStrategy;
import com.tp.holdem.BetComparator;
import com.tp.holdem.Bot;
import com.tp.holdem.Card;
import com.tp.holdem.Deck;
import com.tp.holdem.HandRank;
import com.tp.holdem.HandRankComparator;
import com.tp.holdem.HandRankingEnum;
import com.tp.holdem.Player;
import com.tp.holdem.Strategy;

public class PlayerTests {

  	private transient Player playerNoName, playerWithName;
  	
    @Before
    public void setUp(){
    		playerNoName = new Player(5);
    		playerWithName = new Player(6, "Roger");
    }
  
    @After
    public void tearDown(){
    		playerNoName = null;
    		playerWithName = null;
    }
    
    
    @Test                                       
    public final void testPlayerDefaultNameCheck() {
    	  assertEquals("Player has the wrong default name", "Player5", playerNoName.getName() );
    } 
    
    @Test                                        
    public final void testPlayerGetName() {
    	  assertEquals("Player has the wrong name", "Player5", playerNoName.getName() );
    } 
    
    @Test                                        
    public final void testPlayerGetNumber() {
    	  assertEquals("Player has the wrong number", 6, playerWithName.getNumber() );
    	  playerWithName.setNumber(15);
    	  assertEquals("Player has the wrong number", 15, playerWithName.getNumber() );
    } 
    
    @Test                                        
    public final void testPlayerSetName() {
    	  playerNoName.setName("Steven");
    	  assertEquals("Player has the wrong number", "Steven", playerNoName.getName() );
    } 
    
    @Test                                        
    public final void testPlayerAddCard() {
    	  playerNoName.addCard(new Card("5", "Heart"));
    	  assertEquals("Player still has no cards", 1, playerNoName.getHand().size() );
    } 
    
    @Test                                        
    public final void testPlayerCardsInPosesion() {
    	  playerWithName.addCard(new Card("5", "Heart"));
    	  playerWithName.addCard(new Card("6", "Heart"));
    	  assertEquals("Player has wrong number of cards", 2, playerWithName.getHand().size() );
    } 
    
    @Test                                        
    public final void testPlayerCompareBets() {
    	  Player player1 = new Player(10);
    	  Player player2 = new Player(20);
    	  player1.setBetAmount(20);
    	  player2.setBetAmount(30);
    	  BetComparator comp = new BetComparator();
    	  assertEquals(-1, comp.compare(player1, player2));
    	  assertEquals(1, comp.compare(player2, player1));
    	  player1.setBetAmount(0);
    	  player2.setBetAmount(0);
    	  assertEquals(0, comp.compare(player1, player2));
    } 
    
    @Test                                        
    public final void testPlayerClearHand() {
    	  Player player = new Player(15);
    	  player.addCard(new Card("Jack", "Spade"));
    	  player.addCard(new Card("Queen", "Diamond"));
    	  player.clearHand();
    	  assertEquals(0, player.getHand().size());
    } 
    
    @Test                                        
    public final void testPlayerStrategy() {
    	  Player player = new Player(15);
    	  Bot playerBot = new Bot(5, "bot", new AllInStrategy());
    	  List<Player> players = new ArrayList<Player>();
    	  players.add(player);
    	  players.add(playerBot);
    	  for(Player playerk : players){
	    	  if(playerk instanceof Bot){
	    		  assertEquals("Always All-in", playerk.getStrategy().getName());
	    	  } else {
	    		  assertEquals(null, playerk.getStrategy());
	    	  }
    	  }
    } 
    
    @Test                                        
    public final void testPlayerHandSetting() {
    	  List<Card> cards = new ArrayList<Card>();
    	  Player player = new Player(15);
    	  cards.add(new Card("Jack", "Spade"));
    	  cards.add(new Card("Queen", "Diamond"));
    	  player.setHand(cards);
    	  assertEquals(2, player.getHand().size());
    	  assertEquals("Jack", player.getHand().get(0).getHonour());
    	  assertEquals("Queen", player.getHand().get(1).getHonour());
    } 
    
    @Test                                        
    public final void testPlayerPotCheck() {
    	  Player player = new Player(15);
    	  player.setFromWhichPot(10);
    	  assertEquals(10, player.getFromWhichPot());
    } 
    
    @Test                                        
    public final void testPlayerConnectionId() {
    	  Player player = new Player(15);
    	  player.setConnectionId(52131);
    	  assertEquals(52131, player.getConnectionId());
    } 
    
    @Test                                        
    public final void testPlayerHandRankNoCards() {
    	  Player player = new Player(15);
    	  HandRank handRank = new HandRank();
    	  handRank.setPlayerNumber(player.getNumber());
    	  handRank.setHand(HandRankingEnum.ROYAL_FLUSH);
    	  handRank.setCardsThatMakeDeck(new ArrayList<Card>());
    	  player.setHandRank(handRank);
    	  assertEquals(HandRankingEnum.ROYAL_FLUSH, player.getHandRank().getHand());
    	  assertEquals(15, player.getHandRank().getPlayerNumber());
    	  assertEquals(0, player.getHandRank().getCardsThatMakeDeck().size());
    } 
    
    @Test                                        
    public final void testPlayerHandRankWithCards() {
    	  Player player = new Player(15);
    	  List<Card> cards = new ArrayList<Card>();
    	  cards.add(new Card("2", "Club"));
  		  cards.add(new Card("2", "Heart"));
  		  cards.add(new Card("5", "Club"));
  		  cards.add(new Card("5", "Heart"));
  		  cards.add(new Card("5", "Club"));
    	  player.setHandRank(new HandRank(player.getNumber(), HandRankingEnum.ROYAL_FLUSH, cards));
    	  assertEquals(HandRankingEnum.ROYAL_FLUSH, player.getHandRank().getHand());
    	  assertEquals(15, player.getHandRank().getPlayerNumber());
    	  assertEquals(5, player.getHandRank().getCardsThatMakeDeck().size());
    	  String string = player.getNumber()+" "+player.getHandRank().getHand();
    	  for(Card card : player.getHandRank().getCardsThatMakeDeck()){
  			string+=" "+card.getHonour()+" "+card.getSuit();
  		  }
    	  assertEquals(string, player.getHandRank()+"");
    } 
    
    @Test                                        
    public final void testPlayerCompareHands() {
    	  Player player = new Player(15);
    	  player.setHandRank(new HandRank(player.getNumber(), HandRankingEnum.STRAIGHT, new ArrayList<Card>()));
    	  Player player2 = new Player(20);
    	  player2.setHandRank(new HandRank(player2.getNumber(), HandRankingEnum.STRAIGHT_FLUSH, new ArrayList<Card>()));
    	  HandRankComparator comp = new HandRankComparator();
    	  assertEquals(-1, comp.compare(player.getHandRank(), player2.getHandRank()));
    	  assertEquals(1, comp.compare(player2.getHandRank(), player.getHandRank()));
    } 
    
    @Test                                        
    public final void testPlayerCloneMethod() {
    	  Player playerToClone = new Player(10, "Cloner");
    	  playerToClone.setAllIn(true);
    	  playerToClone.setBetAmount(152);
    	  playerToClone.setBetAmountThisRound(50);
    	  playerToClone.setHasBigBlind(true);
    	  playerToClone.setHasDealerButton(true);
    	  playerToClone.setHasSmallBlind(true);
    	  playerToClone.setChipsAmount(1512);
    	  playerToClone.setFolded(true);
    	  playerToClone.setInGame(false);
    	  Player clonedPlayer = new Player();
    	  clonedPlayer.setAllProperties(playerToClone);
    	  assertEquals(clonedPlayer.isAllIn(), playerToClone.isAllIn());
    	  assertEquals(clonedPlayer.getBetAmount(), playerToClone.getBetAmount());
    	  assertEquals(clonedPlayer.getBetAmountThisRound(), playerToClone.getBetAmountThisRound());
    	  assertEquals(clonedPlayer.isHasBigBlind(), playerToClone.isHasBigBlind());
    	  assertEquals(clonedPlayer.isHasDealerButton(), playerToClone.isHasDealerButton());
    	  assertEquals(clonedPlayer.isHasSmallBlind(), playerToClone.isHasSmallBlind());
    	  assertEquals(clonedPlayer.getChipsAmount(), playerToClone.getChipsAmount());
    	  assertEquals(clonedPlayer.isFolded(), playerToClone.isFolded());
    	  assertEquals(clonedPlayer.isInGame(), playerToClone.isInGame());
    } 
	
}
