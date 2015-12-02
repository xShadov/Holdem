package com.tp.holdem;

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
	public void whatDoIDo(KryoServer server, int betPlayer) {
		request = new SampleRequest("FOLD", betPlayer);
		server.handleReceived((Object)request);
	}

}
