package ServerClientTests;

import java.util.List;

import com.tp.holdem.Card;
import com.tp.holdem.KryoServer;
import com.tp.holdem.PokerTable;
import com.tp.holdem.SampleRequest;

public class FakeServer extends KryoServer
{
	private final int betPlayer = 0;
	private int maxBetOnTable;
	private int bigBlindAmount;
	private final int fixedChips = 40;
	private final String limit = "no-limit";
	private final PokerTable pokerTable = new PokerTable();
	private SampleRequest request;
	
	public FakeServer(final int maxBetOnTable, final int bigBlindAmount)
	{

		this.maxBetOnTable=maxBetOnTable;
		this.bigBlindAmount=bigBlindAmount;
	}
	
	public void setCardsOnTable(final List<Card> tableCards)
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
	
	public void handleReceived(final SampleRequest request)
	{
		this.request=request;
	}
	
	public void setMaxBetOnTable(final int maxBetOnTable)
	{
		this.maxBetOnTable=maxBetOnTable;
	}
	
	public void setbigBlindAmount(final int bigBlindAmount)
	{
		this.bigBlindAmount=bigBlindAmount;
	}
	
	public String getReceived()
	{
		return request.getTag();
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
