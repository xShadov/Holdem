package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Medium implements Strategy
{
	private String name = "Medium";
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
		if(server.getTable().getCardsOnTable().size()==3)
		{
			if(rank.getHand().getValue()>2)
			{
				if(server.getMaxBetOnTable()<100)
					request = new SampleRequest("RAISE",100, server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==2)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else if(server.getMaxBetOnTable()<300)
					request = new SampleRequest("CALL", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==1)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
		}
		else if(server.getTable().getCardsOnTable().size()==4)
		{
			if(rank.getHand().getValue()>2)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("RAISE",100, server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==2)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else if(server.getMaxBetOnTable()==100)
					request = new SampleRequest("CALL", server.getBetPlayer());
				else if(server.getMaxBetOnTable()>100)
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==1)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
		}
		else if(server.getTable().getCardsOnTable().size()==5)
		{
			if(rank.getHand().getValue()>2)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("RAISE",200, server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==2)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==1)
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
		System.out.println(String.valueOf("player"+server.getBetPlayer() +" "+ rank.getHand().getValue()));
		server.handleReceived((Object)request);
		
	}
}
