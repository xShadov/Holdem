package com.tp.holdem;

import java.util.List;

public class Easy implements Strategy{

	private String name = "Easy";
	private SampleRequest request;
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
		if(server.getMaxBetOnTable()==betAmount)
			request = new SampleRequest("CHECK", server.getBetPlayer());
		else if(server.getMaxBetOnTable()<chips)
			request = new SampleRequest("CALL", server.getBetPlayer());
		else
			request = new SampleRequest("ALLIN",server.getBetPlayer());
		server.handleReceived((Object)request);
	}

}


