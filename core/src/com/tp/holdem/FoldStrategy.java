package com.tp.holdem;

public class FoldStrategy implements Strategy
{
	private String name = "Always Fold";
	@Override
	public Strategy getStrategy() {
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void whatDoIDo() {
		// TODO Auto-generated method stub
		
	}

}
