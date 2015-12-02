package com.tp.holdem;

import java.util.ArrayList;
import java.util.List;

public class Bot extends Player
{
	private Strategy strategy;
	public Bot(int number,String name,Strategy strategy)
	{
		super(number,name);
		this.strategy = strategy;
	}

	
}

