package com.tp.holdem;

import java.util.List;

import ServerClientTests.FakeServer;

public interface Strategy 
{
	public Strategy getStrategy();
	public String getName();
	public void whatDoIDo(KryoServer server, List<Card> hand, int betAmount,int chips);
}
