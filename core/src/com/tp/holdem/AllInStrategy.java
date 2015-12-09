package com.tp.holdem;

import java.util.List;


public class AllInStrategy implements Strategy
{
	private String name = "Always All-in";
	private SampleRequest request;
	
	@Override
	public Strategy getStrategy() 
	{
		return this;
	}

	@Override
	public void whatDoIDo(KryoServer server,List<Card> hand, int betAmount,int chips) 
	{		
		request = new SampleRequest("ALLIN", server.getBetPlayer());
		server.handleReceived((Object)request);
	}

	@Override
	public String getName() {
		return name;
	}

}
