package com.tp.holdem.core.strategy;

import com.tp.holdem.core.model.Card;
import com.tp.holdem.core.KryoServer;

import java.util.List;

public interface Strategy {
	public Strategy getStrategy();

	public String getName();

	public void whatDoIDo(KryoServer server, List<Card> hand, int betAmount, int chips);
}
