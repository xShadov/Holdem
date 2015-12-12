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
	
	public String getTag()
	{
		return request.getTAG();
	}


	@Override
	public void whatDoIDo(KryoServer server,List<Card> hand,int betAmount,int chips) 
	{
		rank = HandOperations.findHandRank(0, hand, server.getTable().getCardsOnTable());
		
		if(server.getTable().getCardsOnTable().size()>0)
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()<=betAmount)
				{
					if(server.getBigBlind()>=chips)
					{
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else
					{
						if(server.getMaxBetOnTable()==0)
							request = new SampleRequest("BET",server.getBigBlind(),server.getBetPlayer());
						else
							request = new SampleRequest("RAISE",server.getBigBlind(), server.getBetPlayer());
					}
				}
				else
				{
					if(server.getMaxBetOnTable()>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			}
			
			else if(rank.getHand().getValue()==3)
			{
				if(server.getMaxBetOnTable()<=betAmount)
				{
					if(server.getBigBlind()>=chips)
					{
						request = new SampleRequest("CHECK", server.getBetPlayer());
					}
					else
					{
						if(server.getMaxBetOnTable()==0)
							request = new SampleRequest("BET",server.getBigBlind(),server.getBetPlayer());
						else
							request = new SampleRequest("CHECK",server.getBigBlind(), server.getBetPlayer());
					}
				}
				else
				{
					if(server.getMaxBetOnTable()>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			}
			
			else if(rank.getHand().getValue()<3)
			{
				if(server.getMaxBetOnTable()<=betAmount)
				{
					if(server.getBigBlind()>=chips)
					{
						request = new SampleRequest("CHECK", server.getBetPlayer());
					}
					else
					{
						if(server.getMaxBetOnTable()==0)
							request = new SampleRequest("CHECK",server.getBigBlind(),server.getBetPlayer());
						else
							request = new SampleRequest("CHECK",server.getBigBlind(), server.getBetPlayer());
					}
				}
				else
				{
					if(server.getMaxBetOnTable()>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			}
		}
		else
		{
			if(server.getMaxBetOnTable()==betAmount){
				request = new SampleRequest("CHECK", server.getBetPlayer());
			}
			else{
				if(server.getMaxBetOnTable()>=chips){
					request = new SampleRequest("ALLIN", server.getBetPlayer());
				}
				else{	
					request = new SampleRequest("CALL", server.getBetPlayer());
				}
			}
		}
		System.out.println("player"+String.valueOf(server.getBetPlayer()) +" "+ rank.getHand().getValue()+" " +request.getTAG()+" "+request.getBetAmount());
		server.handleReceived((Object)request);
	}
}
