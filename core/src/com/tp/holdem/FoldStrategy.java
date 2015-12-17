package com.tp.holdem;

import java.util.List;

public class FoldStrategy implements Strategy
{
	private final static String NAME = "Always Fold";
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
	public void whatDoIDo(final KryoServer server, final List<Card> hand, final int betAmount, final int chips) {
		request = new SampleRequest("FOLD", server.getBetPlayer());
		server.handleReceived((Object)request);
	}

}
