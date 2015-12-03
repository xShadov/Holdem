package com.tp.holdem;


public class SampleRequest {

	public String text="";
	public String TAG="";
	public int betAmount=0;
	public int number;

	public SampleRequest(){
		
	}
	
	public SampleRequest(String TAG, String text){
		this.text=text;
	}
	
	public SampleRequest(String TAG, int betAmount, int number){
		this.TAG = TAG;
		this.betAmount = betAmount;
		this.number = number;
	}
	
	public SampleRequest(String TAG, int number){
		this.TAG = TAG;
		this.number = number;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}

	public String getTAG() {
		return TAG;
	}

	public void setTAG(String tAG) {
		TAG = tAG;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
