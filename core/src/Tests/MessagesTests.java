package Tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tp.holdem.Card;
import com.tp.holdem.Deck;
import com.tp.holdem.Player;
import com.tp.holdem.PokerTable;
import com.tp.holdem.SampleRequest;
import com.tp.holdem.SampleResponse;

public class MessagesTests {

	private transient SampleRequest request;
	private transient SampleResponse response;
  	
    @Before
    public void setUp(){
		request = new SampleRequest();
		response = new SampleResponse();
    }
  
    @After
    public void tearDown(){
		request = null;
		response = null;
    }
    
    @Test                                        
    public final void testResponseOnlyTAG() {
    	  response = new SampleResponse("TAG");
    	  assertEquals("TAG", response.getTAG());
    	  response.setTAG("TAG2");
    	  assertEquals("TAG2", response.getTAG());
    } 
    
    @Test                                        
    public final void testResponseTAGWithMessage() {
    	  response = new SampleResponse("TAG", "message");
    	  assertEquals("TAG", response.getTAG());
    	  assertEquals("message", response.getText());
    	  response.setTAG("TAG2");
		  response.setText("message2");
    	  assertEquals("TAG2", response.getTAG());
    	  assertEquals("message2", response.getText());
    } 
    
    @Test                                        
    public final void testResponseTAGWithTable() {
    	  PokerTable table = new PokerTable();
    	  table.setPot(5000);
    	  response = new SampleResponse("TAG", table);
    	  assertEquals("TAG", response.getTAG());
    	  assertEquals(5000, response.getTable().getPot());
    	  response.setTAG("TAG2");
    	  table.setPot(6000);
    	  response.setTable(table);
    	  assertEquals("TAG2", response.getTAG());
    	  assertEquals(6000, response.getTable().getPot());
    } 
    
    @Test                                        
    public final void testResponseTAGWithPlayerList() {
    	  List<Player> players = new ArrayList<Player>();
    	  players.add(new Player(0));
    	  players.add(new Player(1));
    	  response = new SampleResponse("TAG", players);
    	  assertEquals("TAG", response.getTAG());
    	  assertEquals("Player0", response.getPlayers().get(0).getName());
    	  assertEquals("Player1", response.getPlayers().get(1).getName());
    	  response.setTAG("TAG2");
    	  players.add(new Player(2));
    	  response.setPlayers(players);
    	  assertEquals("TAG2", response.getTAG());
    	  assertEquals("Player2", response.getPlayers().get(2).getName());
    } 
    
    @Test                                        
    public final void testResponseTAGWithCardList() {
    	  List<Card> cards = new ArrayList<Card>();
    	  cards.add(new Card("Jack", "Spade"));
    	  cards.add(new Card("Queen", "Diamond"));
    	  response = new SampleResponse("TAG", cards, false);
    	  assertEquals("TAG", response.getTAG());
    	  assertEquals("Jack", response.getCards().get(0).getHonour());
    	  assertEquals("Queen", response.getCards().get(1).getHonour());
    } 
    
    @Test                                        
    public final void testResponseTAGWithNumber() {
          response = new SampleResponse("TAG", 155);
          assertEquals("TAG", response.getTAG());
    	  assertEquals(155, response.getNumber());
    	  response.setTAG("TAG2");
    	  response.setNumber(222);
    	  assertEquals("TAG2", response.getTAG());
    	  assertEquals(222, response.getNumber());
    }
    
    @Test                                        
    public final void testResponseTAGWithNumberAndMaxBetOnTable() {
    	  response = new SampleResponse("TAG", 155, 500);
    	  assertEquals("TAG", response.getTAG());
    	  assertEquals(155, response.getNumber());
    	  assertEquals(500, response.getMaxBetOnTable());
    	  response.setTAG("TAG2");
   	      response.setNumber(333);
   	      response.setMaxBetOnTable(2221);
    	  assertEquals("TAG2", response.getTAG());
    	  assertEquals(333, response.getNumber());
    	  assertEquals(2221, response.getMaxBetOnTable());
    }
    
    @Test                                        
    public final void testRequestTAGWithMessage() {
    	  request = new SampleRequest("TAG", "message");
    	  assertEquals("TAG", request.getTAG());
    	  assertEquals("message", request.getText());
    	  request.setTAG("TAG2");
    	  request.setText("message2");
    	  assertEquals("TAG2", request.getTAG());
    	  assertEquals("message2", request.getText());
    } 
    
    @Test                                        
    public final void testRequestTAGWithNumber() {
    	request = new SampleRequest("TAG", 155);
          assertEquals("TAG", request.getTAG());
    	  assertEquals(155, request.getNumber());
    	  request.setTAG("TAG2");
    	  request.setNumber(222);
    	  assertEquals("TAG2", request.getTAG());
    	  assertEquals(222, request.getNumber());
    }
    
    @Test                                        
    public final void testResponseTAGWithNumberAndBetAmount() {
    	  request = new SampleRequest("TAG", 155, 500);
    	  assertEquals("TAG", request.getTAG());
    	  assertEquals(500, request.getNumber());
    	  assertEquals(155, request.getBetAmount());
    	  request.setTAG("TAG2");
    	  request.setNumber(333);
    	  request.setBetAmount(999);
    	  assertEquals("TAG2", request.getTAG());
    	  assertEquals(333, request.getNumber());
    	  assertEquals(999, request.getBetAmount());
    }
}
