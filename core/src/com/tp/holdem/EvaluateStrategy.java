package com.tp.holdem;

public class EvaluateStrategy implements Strategy
{
	private String name = "Evaluate";
	private SampleRequest request;
	@Override
	public Strategy getStrategy() {
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void whatDoIDo(KryoServer server, int betPlayer) 
	{
		
	}

}
