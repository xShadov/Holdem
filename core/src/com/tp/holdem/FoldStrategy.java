package com.tp.holdem;

import java.util.List;

public class FoldStrategy implements Strategy
{
	private String name = "Always Fold";
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
	public void whatDoIDo(KryoServer server,List<Card> hand) {
		request = new SampleRequest("FOLD", server.getBetPlayer());
		server.handleReceived((Object)request);
	}

}
