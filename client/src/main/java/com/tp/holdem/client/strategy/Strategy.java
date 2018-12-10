package com.tp.holdem.client.strategy;

import com.tp.holdem.client.model.Card;

import java.util.List;

public interface Strategy {
	public Strategy getStrategy();

	public String getName();

	//public void whatDoIDo(KryoServer server, List<Card> hand, int betAmount, int chips);
}
