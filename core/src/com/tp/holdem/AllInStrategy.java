package com.tp.holdem;

import com.esotericsoftware.kryonet.Connection;

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
	public void whatDoIDo(KryoServer server, int betPlayer) 
	{		
		request = new SampleRequest("ALLIN", betPlayer);
		server.handleReceived((Object)request);
	}

	@Override
	public String getName() {
		return name;
	}

}
