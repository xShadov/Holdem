package com.tp.holdem;

import java.util.List;

public class CheckHandRankStrategy implements Strategy
{
	private String name = "Check Hand Rank";
	private SampleRequest request;
	private HandRank rank;
	@Override
	public Strategy getStrategy() {
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void whatDoIDo(KryoServer server,List<Card> hand) 
	{
		rank = HandOperations.findHandRank(0, hand, server.getTable().getCardsOnTable());
		if(server.getFlopTime())
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("RAISE",100, server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()>1)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else if(server.getMaxBetOnTable()<300)
					request = new SampleRequest("CALL", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
		}
		else if(server.getTrunTime())
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("RAISE",100, server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()>1)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else if(server.getMaxBetOnTable()<100)
					request = new SampleRequest("CALL", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
		}
		else if(server.getRiverTime())
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("RAISE",200, server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()>1)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
		}
		else
		{
			if(server.getMaxBetOnTable()==0)
				request = new SampleRequest("CHECK", server.getBetPlayer());
			else
				request = new SampleRequest("CALL", server.getBetPlayer());
		}
		server.handleReceived((Object)request);
	}
	
}
