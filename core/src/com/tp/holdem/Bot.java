package com.tp.holdem;

public class Bot extends Player
{
	private final transient Strategy strategy;
	public Bot(final int number, final String name, final Strategy strategy)
	{
		super(number,name);
		this.strategy = strategy;
	}
	@Override
	public Strategy getStrategy()
	{
		return strategy;
	}
	
}

