package com.tp.holdem;

import java.util.List;

public class AllInStrategy implements Strategy {
	private final static String NAME = "Always All-in";
	private SampleRequest request;

	@Override
	public Strategy getStrategy() {
		return this;
	}

	public String getTag() {
		return request.getTag();
	}

	@Override
	public void whatDoIDo(final KryoServer server, final List<Card> hand, final int betAmount, final int chips) {
		request = new SampleRequest("ALLIN", server.getBetPlayer());
		server.handleReceived((Object) request);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
