package com.tp.holdem;


public class SampleRequest {

	public String text="";
	public String tag="";
	public int betAmount=0;
	public int number;

	public SampleRequest(){
		
	}
	
	public SampleRequest(final String TAG, final String text){
		this.tag=TAG;
		this.text=text;
	}
	
	public SampleRequest(final String TAG, final int betAmount, final int number){
		this.tag = TAG;
		this.betAmount = betAmount;
		this.number = number;
	}
	
	public SampleRequest(final String TAG, final int number){
		this.tag = TAG;
		this.number = number;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(final String text){
		this.text = text;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(final String tAG) {
		tag = tAG;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(final int betAmount) {
		this.betAmount = betAmount;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}
}
