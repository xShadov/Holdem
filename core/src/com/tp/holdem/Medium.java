package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Medium implements Strategy
{
	private String name = "Medium";
	private SampleRequest request;
	private HandRank rank;
	private int howMuchToBet;
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
		if(server.getLimitType()=="fixed-limit")
			howMuchToBet=server.getFixedChips();
		else
			howMuchToBet=server.getBigBlind();
		
		
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
							request = new SampleRequest("BET",howMuchToBet,server.getBetPlayer());
						else
							request = new SampleRequest("RAISE",howMuchToBet, server.getBetPlayer());
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
							request = new SampleRequest("BET",howMuchToBet,server.getBetPlayer());
						else
							request = new SampleRequest("CHECK", server.getBetPlayer());
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
							request = new SampleRequest("CHECK",server.getBetPlayer());
						else
							request = new SampleRequest("CHECK", server.getBetPlayer());
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
		/*System.out.print("Bot "+String.valueOf(server.getBetPlayer()-server.getPlayersCount()) +", hand value: "+ rank.getHand().getValue()+", action: " +request.getTAG());
		if(request.getTAG() =="BET" || request.getTAG()=="RAISE")
			System.out.print(" with "+request.getBetAmount()+" chips");
		System.out.println(". He had those options:");
		for(int i = 0;server.getPossibleOpitions().size()>i;i++)
			System.out.print(" "+server.getPossibleOpitions().get(i));
		System.out.println("\r");*/
		server.handleReceived((Object)request);
	}
}
