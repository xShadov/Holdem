package com.tp.holdem;


public class SampleRequest {

	public String text="";
	

	public SampleRequest(){
		
	}
	
	public SampleRequest(String text){
		this.text=text;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}
}
