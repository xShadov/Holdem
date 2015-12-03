package com.tp.holdem;

import java.util.List;

public interface Strategy 
{
	public Strategy getStrategy();
	public String getName();
	public void whatDoIDo(KryoServer server, List<Card> hand);
}
