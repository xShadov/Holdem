package com.tp.holdem.core.model;

public class Card {
	private String suit;
	private String honour;
	private int xCordination;
	private int yCordination;
	private int value;
	private String color;

	public Card() {

	}

	public Card(final String honour, final String suit) {
		this.suit = suit;
		this.honour = honour;

		if (suit.equals("Spade") || suit.equals("Club"))
			this.color = "black";
		else
			this.color = "red";

		findCordination();
		findValue();
	}

	private void findValue() {
		if (this.honour.equals("2")) {
			this.value = 2;
		} else if (this.honour.equals("3")) {
			this.value = 3;
		} else if (this.honour.equals("4")) {
			this.value = 4;
		} else if (this.honour.equals("5")) {
			this.value = 5;
		} else if (this.honour.equals("6")) {
			this.value = 6;
		} else if (this.honour.equals("7")) {
			this.value = 7;
		} else if (this.honour.equals("8")) {
			this.value = 8;
		} else if (this.honour.equals("9")) {
			this.value = 9;
		} else if (this.honour.equals("10")) {
			this.value = 10;
		} else if (this.honour.equals("Jack")) {
			this.value = 11;
		} else if (this.honour.equals("Queen")) {
			this.value = 12;
		} else if (this.honour.equals("King")) {
			this.value = 13;
		} else if (this.honour.equals("Ace")) {
			this.value = 14;
		}
	}

	private void findCordination() {
		if (this.getHonour().equals("Ace")) {
			this.xCordination = 2;
		} else if (this.getHonour().equals("2")) {
			this.xCordination = 2 + (1 * 73);
		} else if (this.getHonour().equals("3")) {
			this.xCordination = 2 + (2 * 73);
		} else if (this.getHonour().equals("4")) {
			this.xCordination = 2 + (3 * 73);
		} else if (this.getHonour().equals("5")) {
			this.xCordination = 2 + (4 * 73);
		} else if (this.getHonour().equals("6")) {
			this.xCordination = 2 + (5 * 73);
		} else if (this.getHonour().equals("7")) {
			this.xCordination = 2 + (6 * 73);
		} else if (this.getHonour().equals("8")) {
			this.xCordination = 2 + (7 * 73);
		} else if (this.getHonour().equals("9")) {
			this.xCordination = 2 + (8 * 73);
		} else if (this.getHonour().equals("10")) {
			this.xCordination = 2 + (9 * 73);
		} else if (this.getHonour().equals("Jack")) {
			this.xCordination = 2 + (10 * 73);
		} else if (this.getHonour().equals("Queen")) {
			this.xCordination = 2 + (11 * 73);
		} else if (this.getHonour().equals("King")) {
			this.xCordination = 2 + (12 * 73);
		}

		if (this.getSuit().equals("Club")) {
			this.yCordination = 3;
		} else if (this.getSuit().equals("Heart")) {
			this.yCordination = 101;
		} else if (this.getSuit().equals("Spade")) {
			this.yCordination = 199;
		} else if (this.getSuit().equals("Diamond")) {
			this.yCordination = 297;
		}
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(final String suit) {
		this.suit = suit;
		if (suit.equals("Spade") || suit.equals("Club")) {
			this.color = "black";
		} else
			this.color = "red";
	}

	public String getHonour() {
		return honour;
	}

	public void setHonour(final String honour) {
		this.honour = honour;
		findCordination();
		findValue();
	}

	public String getColor() {
		return color;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public int getxCordination() {
		return xCordination;
	}

	public void setxCordination(final int xCordination) {
		this.xCordination = xCordination;
	}

	public int getyCordination() {
		return yCordination;
	}

	public void setyCordination(final int yCordination) {
		this.yCordination = yCordination;
	}

}