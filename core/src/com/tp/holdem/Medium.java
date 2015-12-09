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
	public void whatDoIDo(KryoServer server,List<Card> hand,int betAmount,int chips) 
	{
		rank = HandOperations.findHandRank(0, hand, server.getTable().getCardsOnTable());
		
		//FLOP
		if(server.getTable().getCardsOnTable().size()==3)
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()<=server.getBigBlind()){
					if(server.getBigBlind()>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("RAISE",server.getBigBlind(), server.getBetPlayer());
					}
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
			else if(rank.getHand().getValue()==3)
			{
				if(server.getMaxBetOnTable()<=server.getBigBlind()*2){
					if(server.getMaxBetOnTable()*2>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
			else if(rank.getHand().getValue()<3)
			{
				if(server.getMaxBetOnTable()<=server.getBigBlind()*2){
					if(server.getMaxBetOnTable()*2>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
		}
		
		//TURN
		else if(server.getTable().getCardsOnTable().size()==4)
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()==betAmount){
					if(server.getBigBlind()>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("RAISE",server.getBigBlind(), server.getBetPlayer());
					}
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
			else if(rank.getHand().getValue()==3)
			{
				if(server.getMaxBetOnTable()<=server.getBigBlind()*2){
					if(server.getMaxBetOnTable()*2>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
			else if(rank.getHand().getValue()==1)
			{
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
			else if(rank.getHand().getValue()==2)
			{
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
		}
		
		// RIVER
		else if(server.getTable().getCardsOnTable().size()==5)
		{
			if(rank.getHand().getValue()>3)
			{
				if(server.getMaxBetOnTable()==betAmount){
					if(server.getBigBlind()>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("RAISE",server.getBigBlind(), server.getBetPlayer());
					}
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
			else if(rank.getHand().getValue()==3)
			{
				if(server.getMaxBetOnTable()<=server.getBigBlind()*2){
					if(server.getMaxBetOnTable()*2>=chips){
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					}
					else{	
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
			else if(rank.getHand().getValue()==1)
			{
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
			else if(rank.getHand().getValue()==2)
			{
				if(server.getMaxBetOnTable()==betAmount){
					request = new SampleRequest("CHECK", server.getBetPlayer());
				}
				else{
					request = new SampleRequest("FOLD", server.getBetPlayer());
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
