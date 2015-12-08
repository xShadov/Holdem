package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hard implements Strategy
{
	private String name = "Hard";
	private SampleRequest request;
	private HandRank rank;
	private Random random = new Random();
	private int a; //wylosowana liczba
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
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()<server.getBigBlind())
					request = new SampleRequest("RAISE",server.getBigBlind(), server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else if(server.getMaxBetOnTable()<server.getBigBlind()*3)
					request = new SampleRequest("CALL", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()<3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
		}
		else if(server.getTable().getCardsOnTable().size()==4)
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("RAISE",server.getBigBlind(), server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else if(server.getMaxBetOnTable()>0)
					request = new SampleRequest("CALL", server.getBetPlayer());
				else if(server.getMaxBetOnTable()>server.getBigBlind())
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==1)
			{
				a = random.nextInt(10);
				System.out.println(String.valueOf(a));
				if(a<4)
				{
					request = new SampleRequest("RAISE",server.getBigBlind()*3, server.getBetPlayer());
				}
				else
				{
					if(server.getMaxBetOnTable()==0)
						request = new SampleRequest("CHECK", server.getBetPlayer());
					else
						request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
			else if(rank.getHand().getValue()<3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());

			}
		}
		else if(server.getTable().getCardsOnTable().size()==5)
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("RAISE",server.getBigBlind()*2, server.getBetPlayer());
				else
					request = new SampleRequest("CALL", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==3)
			{
				if(server.getMaxBetOnTable()==0)
					request = new SampleRequest("CHECK", server.getBetPlayer());
				else
					request = new SampleRequest("FOLD", server.getBetPlayer());
			}
			else if(rank.getHand().getValue()==1)
			{
				a = random.nextInt(10);
				System.out.println(String.valueOf(a));
				if(a<2)
				{
					request = new SampleRequest("RAISE",server.getBigBlind()*3, server.getBetPlayer());
				}
				else
				{
					if(server.getMaxBetOnTable()==0)
						request = new SampleRequest("CHECK", server.getBetPlayer());
					else
						request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
			else if(rank.getHand().getValue()<3)
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
