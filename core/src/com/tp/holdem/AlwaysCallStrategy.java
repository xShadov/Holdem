package com.tp.holdem;

public class AlwaysCallStrategy implements Strategy{

	private String name = "Always Call";
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
	public void whatDoIDo(KryoServer server, int betPlayer) 
	{
		request = new SampleRequest("CALL", betPlayer);
		server.handleReceived((Object)request);
	}

}
