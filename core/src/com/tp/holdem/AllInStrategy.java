package com.tp.holdem;

public class AllInStrategy implements Strategy
{
	private String name = "Always All-in";
	
	@Override
	public Strategy getStrategy() 
	{
		return this;
	}

	@Override
	public void whatDoIDo() 
	{		
		
	}

	@Override
	public String getName() {
		return name;
	}

}
