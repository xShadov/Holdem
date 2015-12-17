package com.tp.holdem;

import java.util.List;

public class Easy implements Strategy{

	private final static String NAME = "Easy";
	private SampleRequest request;

	@Override
	public Strategy getStrategy() {

		return this;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public String getTag()
	{
		return request.getTag();
	}

	@Override
	public void whatDoIDo(final KryoServer server, final List<Card> hand, final int betAmount, final int chips) 
	{
		if(server.getMaxBetOnTable()==betAmount)
			request = new SampleRequest("CHECK", server.getBetPlayer());
		else if(server.getMaxBetOnTable()>betAmount)
		{
			if(server.getMaxBetOnTable()>=chips)
				request = new SampleRequest("ALLIN",server.getBetPlayer());
			else
				request = new SampleRequest("CALL", server.getBetPlayer());
		}
		server.handleReceived((Object)request);
	}

}


