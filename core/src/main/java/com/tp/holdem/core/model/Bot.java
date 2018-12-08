package com.tp.holdem.core.model;

import com.tp.holdem.core.model.Player;
import com.tp.holdem.core.strategy.Strategy;

public class Bot extends Player {
	private Strategy strategy;

	public Bot(final int number, final String name, final Strategy strategy) {
		super(number, name);
		this.strategy = strategy;
	}

	@Override
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

}
