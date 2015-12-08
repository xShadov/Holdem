package ServerClientTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tp.holdem.Card;
import com.tp.holdem.Player;
import com.tp.holdem.PokerTable;
import com.tp.holdem.SampleRequest;
import com.tp.holdem.SampleResponse;

public class FakeServerTests {
	private List<Player> players;
  	
    @Before
    public void setUp(){
    	players = new ArrayList<Player>();
    }
  
    @After
    public void tearDown(){
    	players = null;
    }
    
    
    @Test                                       
    public final void testServerNewHand() throws Exception {
    	Player player1 = new Player(1);
    	player1.setChipsAmount(1500);
    	Player player2 = new Player(2);
    	player2.setChipsAmount(2500);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.initiateNewHand();
    	assertEquals(20, server.getPokerTable().getSmallBlindAmount());
    	assertEquals(40, server.getPokerTable().getBigBlindAmount());
    	assertEquals(48, server.getDeck().getCards().size());
    	assertTrue(server.getPlayers().get(0).isHasDealerButton());
    	assertTrue(server.getPlayers().get(0).isHasBigBlind());
    	assertTrue(server.getPlayers().get(1).isHasSmallBlind());
    	assertEquals(40, server.getPlayers().get(0).getBetAmount());
    	assertEquals(40, server.getPlayers().get(0).getBetAmountThisRound());
    	assertEquals(20, server.getPlayers().get(1).getBetAmount());
    	assertEquals(20, server.getPlayers().get(1).getBetAmountThisRound());
    	assertEquals(60, server.getPokerTable().getPot());
    	assertEquals(1, server.getBidingCount());
    	assertEquals(1, server.getTurnPlayer());
    	assertTrue(server.isBidingTime());
    	assertFalse(server.isBidingOver());
    	assertEquals(1, server.getBetPlayer());
    	assertEquals(0, server.getLastToBet());
    	assertEquals(40, server.getMaxBetOnTable());
    } 
    
    @Test                                       
    public final void testServerNewHandWhenTheyGotNoChips() throws Exception {
    	Player player1 = new Player(1);
    	player1.setChipsAmount(10);
    	Player player2 = new Player(2);
    	player2.setChipsAmount(15);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.initiateNewHand();
    	assertEquals(20, server.getPokerTable().getSmallBlindAmount());
    	assertEquals(40, server.getPokerTable().getBigBlindAmount());
    	assertEquals(48, server.getDeck().getCards().size());
    	assertTrue(server.getPlayers().get(0).isHasDealerButton());
    	assertTrue(server.getPlayers().get(0).isHasBigBlind());
    	assertTrue(server.getPlayers().get(1).isHasSmallBlind());
    	assertEquals(10, server.getPlayers().get(0).getBetAmount());
    	assertEquals(10, server.getPlayers().get(0).getBetAmountThisRound());
    	assertEquals(15, server.getPlayers().get(1).getBetAmount());
    	assertEquals(15, server.getPlayers().get(1).getBetAmountThisRound());
    	assertEquals(25, server.getPokerTable().getPot());
    	assertEquals(1, server.getBidingCount());
    	assertEquals(1, server.getTurnPlayer());
    	assertTrue(server.isBidingTime());
    	assertFalse(server.isBidingOver());
    	assertEquals(1, server.getBetPlayer());
    	assertEquals(0, server.getLastToBet());
    	assertEquals(15, server.getMaxBetOnTable());
    	assertTrue(server.getPlayers().get(0).isAllIn());
    	assertTrue(server.getPlayers().get(1).isAllIn());
    } 
    
    @Test                                       
    public final void testServerNewHandWhenTheyGotExactAmountOfChips() throws Exception {
    	Player player1 = new Player(1);
    	player1.setChipsAmount(40);
    	Player player2 = new Player(2);
    	player2.setChipsAmount(20);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.initiateNewHand();
    	assertEquals(20, server.getPokerTable().getSmallBlindAmount());
    	assertEquals(40, server.getPokerTable().getBigBlindAmount());
    	assertEquals(48, server.getDeck().getCards().size());
    	assertTrue(server.getPlayers().get(0).isHasDealerButton());
    	assertTrue(server.getPlayers().get(0).isHasBigBlind());
    	assertTrue(server.getPlayers().get(1).isHasSmallBlind());
    	assertEquals(40, server.getPlayers().get(0).getBetAmount());
    	assertEquals(40, server.getPlayers().get(0).getBetAmountThisRound());
    	assertEquals(20, server.getPlayers().get(1).getBetAmount());
    	assertEquals(20, server.getPlayers().get(1).getBetAmountThisRound());
    	assertEquals(60, server.getPokerTable().getPot());
    	assertEquals(1, server.getBidingCount());
    	assertEquals(1, server.getTurnPlayer());
    	assertTrue(server.isBidingTime());
    	assertFalse(server.isBidingOver());
    	assertEquals(1, server.getBetPlayer());
    	assertEquals(0, server.getLastToBet());
    	assertEquals(40, server.getMaxBetOnTable());
    	assertTrue(server.getPlayers().get(0).isAllIn());
    	assertTrue(server.getPlayers().get(1).isAllIn());
    } 
    
    @Test                                       
    public final void testServerEveryoneFolded() throws Exception {
    	Player player1 = new Player(1);
    	player1.setChipsAmount(1500);
    	player1.setFolded(false);
    	Player player2 = new Player(2);
    	player2.setChipsAmount(2500);
    	player2.setFolded(true);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.initiateNewHand();
    	assertTrue(server.everyoneFoldedExceptBetPlayer());
    } 
    
    @Test                                       
    public final void testServerCheckOneWinner() throws Exception {
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
    	FakeServer server = new FakeServer(players);
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Spade"));
    	table.addCard(new Card("9", "Spade"));
    	table.addCard(new Card("8", "Spade"));
    	server.setPokerTable(table);
    	server.timeToCheckWinner();
    	assertEquals(1000, player1.getChipsAmount());
    } 
    
    @Test                                       
    public final void testServerCheckMultipleWinners() throws Exception {
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
    	FakeServer server = new FakeServer(players);
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	server.setPokerTable(table);
    	server.timeToCheckWinner();
    	assertEquals(0, player1.getFromWhichPot());
    	assertEquals(0, player2.getFromWhichPot());
    	assertEquals(500, player1.getChipsAmount());
    	assertEquals(500, player2.getChipsAmount());
    } 
    
    @Test                                       
    public final void testServerChipsInPot() throws Exception {
    	Player player1 = new Player(0);
    	player1.addCard(new Card("Jack", "Spade"));
    	player1.addCard(new Card("Queen", "Spade"));
    	player1.setBetAmount(300);
    	Player player2 = new Player(1);
    	player2.addCard(new Card("Jack", "Heart"));
    	player2.addCard(new Card("Queen", "Heart"));
    	player2.setBetAmount(500);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	server.setPokerTable(table);
    	server.timeToCheckWinner();
    	player1.setBetAmount(300);
    	player2.setBetAmount(500);
    	assertEquals(800, server.findAmountChipsForAllInPot(0, 500));
    } 
    
    @Test                                       
    public final void testServerPeopleInSamePot() throws Exception {
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
    	FakeServer server = new FakeServer(players);
    	PokerTable table = new PokerTable();
    	table.setPot(1000);
    	table.addCard(new Card("10", "Club"));
    	table.addCard(new Card("9", "Club"));
    	table.addCard(new Card("8", "Club"));
    	server.setPokerTable(table);
    	server.timeToCheckWinner();
    	assertEquals(2, server.howManyPeopleInSamePot(0));
    } 
    
    @Test                                       
    public final void testServerReset() throws Exception {
    	Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	player1.setInGame(false);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.setGameStarted(true);
    	server.resetAfterRound();
    	assertFalse(server.isGameStarted());
    	
    	player1.setInGame(true);
    	player2.setInGame(true);
    	server = new FakeServer(players);
    	server.setNewHand(false);
    	server.setMaxBetOnTable(0);
    	server.resetAfterRound();
    	assertTrue(server.isNewHand());
    	assertEquals(40, server.getMaxBetOnTable());
    } 
    
    @Test                                       
    public final void testServerNotEnoughPlayers() throws Exception {
    	Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	player1.setInGame(false);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.setGameStarted(true);
    	server.resetAfterRound();
    	assertTrue(server.notEnoughPlayers());
    	
    	player1.setInGame(true);
    	player2.setInGame(true);
    	server = new FakeServer(players);
    	server.setNewHand(false);
    	server.setMaxBetOnTable(0);
    	server.resetAfterRound();
    	assertFalse(server.notEnoughPlayers());
    } 
    
    @Test                                       
    public final void testServerFirstAndLastToBet() throws Exception {
    	Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	player1.setInGame(false);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.setBidingCount(1);
    	server.setFirstAndLastToBet();
    	assertEquals(0, server.getBetPlayer());
    	assertEquals(1, server.getLastToBet());
    	
    	server = new FakeServer(players);
    	server.setBidingCount(2);
    	server.setTurnPlayer(1);
    	server.setFirstAndLastToBet();
    	assertEquals(1, server.getBetPlayer());
    	assertEquals(0, server.getLastToBet());
    }
    
    @Test                                       
    public final void testServerPreviousAsLastToBet() throws Exception {
    	Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	Player player3 = new Player(2);
    	Player player4 = new Player(3);
    	Player player5 = new Player(4);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	players.add(player4);
    	players.add(player5);
    	FakeServer server = new FakeServer(players);
    	server.setBetPlayer(0);
    	server.setPreviousAsLastToBet();
    	assertEquals(4, server.getLastToBet());
    	server.setBetPlayer(4);
    	server.setPreviousAsLastToBet();
    	assertEquals(3, server.getLastToBet());
    	server.setBetPlayer(3);
    	server.setPreviousAsLastToBet();
    	assertEquals(2, server.getLastToBet());
    	server.setBetPlayer(2);
    	server.setPreviousAsLastToBet();
    	assertEquals(1, server.getLastToBet());
    	server.setBetPlayer(1);
    	server.setPreviousAsLastToBet();
    	assertEquals(0, server.getLastToBet());
    }
    
    @Test                                       
    public final void testServerEverybodyAllIn() throws Exception {
    	Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	Player player3 = new Player(2);
    	player1.setAllIn(true);
    	player2.setAllIn(false);
    	player3.setAllIn(true);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	FakeServer server = new FakeServer(players);
    	assertFalse(server.everybodyAllIn());
    	player2.setAllIn(true);
    	assertTrue(server.everybodyAllIn());
    }
    
    @Test                                       
    public final void testServerSetNextAsBet() throws Exception {
    	Player player1 = new Player(0);
    	Player player2 = new Player(1);
    	Player player3 = new Player(2);
    	player2.setInGame(false);
    	players.add(player1);
    	players.add(player2);
    	players.add(player3);
    	FakeServer server = new FakeServer(players);
    	server.setBetPlayer(0);
    	server.setNextAsBetPlayer();
    	assertEquals(2, server.getBetPlayer());
    	server.setBetPlayer(2);
    	server.setNextAsBetPlayer();
    	assertEquals(0, server.getBetPlayer());
    }
    
    @Test                                       
    public final void testServerReceivedBet() throws Exception {
    	Player player1 = new Player(0);
    	player1.setChipsAmount(500);
    	players.add(player1);
    	FakeServer server = new FakeServer(players);
    	server.setPokerTable(new PokerTable());
    	server.setMaxBetOnTable(50);
    	SampleRequest response = new SampleRequest("BET", 500, 0);
    	server.handleReceived(response);
    	assertEquals(500, server.getPlayers().get(0).getBetAmountThisRound());
    	assertEquals(0, server.getPlayers().get(0).getChipsAmount());
    	assertEquals(500, server.getMaxBetOnTable());
    	assertEquals(500, server.getPokerTable().getPot());
    	assertTrue(server.getPlayers().get(0).isAllIn());
    }
    
    @Test                                       
    public final void testServerReceivedCall() throws Exception {
    	Player player1 = new Player(0);
    	player1.setChipsAmount(500);
    	player1.setBetAmountThisRound(200);
    	players.add(player1);
    	FakeServer server = new FakeServer(players);
    	server.setPokerTable(new PokerTable());
    	server.setMaxBetOnTable(500);
    	SampleRequest response = new SampleRequest("CALL", 0);
    	server.handleReceived(response);
    	assertEquals(500, server.getPlayers().get(0).getBetAmountThisRound());
    	assertEquals(200, server.getPlayers().get(0).getChipsAmount());
    	assertEquals(500, server.getMaxBetOnTable());
    	assertEquals(300, server.getPokerTable().getPot());
    }
    
    @Test                                       
    public final void testServerReceivedFold() throws Exception {
    	Player player1 = new Player(0);
    	players.add(player1);
    	FakeServer server = new FakeServer(players);
    	SampleRequest response = new SampleRequest("FOLD", 0);
    	server.handleReceived(response);
    	assertTrue(server.getPlayers().get(0).isFolded());
    }
    
    @Test                                       
    public final void testServerReceivedAllin() throws Exception {
    	Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	Player player2 = new Player(1);
    	player1.setChipsAmount(1500);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.setMaxBetOnTable(500);
    	server.setPokerTable(new PokerTable());
    	SampleRequest response = new SampleRequest("ALLIN", 0);
    	server.handleReceived(response);
    	assertEquals(1500, server.getPokerTable().getPot());
    	assertEquals(0, server.getPlayers().get(0).getChipsAmount());
    	assertEquals(1500, server.getMaxBetOnTable());
    	assertEquals(1, server.getLastToBet());
    	assertEquals(1500, server.getPlayers().get(0).getBetAmountThisRound());
    	assertTrue(server.getPlayers().get(0).isAllIn());
    }
    
    @Test                                       
    public final void testServerReceivedRaise() throws Exception {
    	Player player1 = new Player(0);
    	player1.setChipsAmount(1500);
    	Player player2 = new Player(1);
    	player1.setChipsAmount(1500);
    	players.add(player1);
    	players.add(player2);
    	FakeServer server = new FakeServer(players);
    	server.setMaxBetOnTable(500);
    	server.setPokerTable(new PokerTable());
    	SampleRequest response = new SampleRequest("RAISE", 200, 0);
    	server.handleReceived(response);
    	assertEquals(700, server.getPokerTable().getPot());
    	assertEquals(800, server.getPlayers().get(0).getChipsAmount());
    	assertEquals(700, server.getMaxBetOnTable());
    	assertEquals(1, server.getLastToBet());
    	assertEquals(700, server.getPlayers().get(0).getBetAmountThisRound());
    }
}
