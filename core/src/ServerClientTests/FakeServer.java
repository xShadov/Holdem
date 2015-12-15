package ServerClientTests;

import java.util.ArrayList;
import java.util.List;

import com.tp.holdem.Card;
import com.tp.holdem.KryoServer;
import com.tp.holdem.PokerTable;
import com.tp.holdem.SampleRequest;

public class FakeServer extends KryoServer
{
	private int betPlayer = 0;
	private int maxBetOnTable;
	private int bigBlindAmount;
	private int fixedChips = 40;
	private String limit = "no-limit";
	private PokerTable pokerTable = new PokerTable();
	private SampleRequest request;
	
	public FakeServer(int maxBetOnTable, int bigBlindAmount)
	{

		this.maxBetOnTable=maxBetOnTable;
		this.bigBlindAmount=bigBlindAmount;
	}
	
	public void setCardsOnTable(List<Card> tableCards)
	{
		pokerTable.setCardsOnTable(tableCards);
	}
	
	public int getFixedChips()
	{
		return fixedChips;
	}
	
	public String getLimit()
	{
		return limit;
	}
	
	public void handleReceived(SampleRequest request)
	{
		this.request=request;
	}
	
	public void setMaxBetOnTable(int maxBetOnTable)
	{
		this.maxBetOnTable=maxBetOnTable;
	}
	
	public void setbigBlindAmount(int bigBlindAmount)
	{
		this.bigBlindAmount=bigBlindAmount;
	}
	
	public String getReceived()
	{
		return request.getTAG();
	}
	
	public int getMaxBetOnTable()
	{
		return maxBetOnTable;
	}
	
	public PokerTable getTable()
	{
		return pokerTable;
	}
	
	public int getBetPlayer()
	{
		return betPlayer;
	}
	
	public int getBigBlind()
	{
		return bigBlindAmount;
	}
}
