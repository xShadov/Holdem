package com.tp.holdem.core.model;


import lombok.Data;

@Data
public class SampleRequest {
	public String text = "";
	public String tag = "";
	public int betAmount;
	public int number;

	public SampleRequest() {

	}

	public SampleRequest(final String TAG, final String text) {
		this.tag = TAG;
		this.text = text;
	}

	public SampleRequest(final String TAG, final int betAmount, final int number) {
		this.tag = TAG;
		this.betAmount = betAmount;
		this.number = number;
	}

	public SampleRequest(final String TAG, final int number) {
		this.tag = TAG;
		this.number = number;
	}
}
