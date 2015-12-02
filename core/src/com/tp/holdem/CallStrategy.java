package com.tp.holdem;

public class CallStrategy implements Strategy{

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
		//if(highestBet==0)
		request = new SampleRequest("CHECK", betPlayer);
		//else
		//request = new SampleRequest("CALL", betPlayer);
		server.handleReceived((Object)request);
	}

}
