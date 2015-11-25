package com.tp.holdem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Card {

  	private String suit;
  	private String honour;
  	private int xCordination;
  	private int yCordination;
  	
  	public int getxCordination() {
		return xCordination;
	}
	public void setxCordination(int xCordination) {
		this.xCordination = xCordination;
	}
	public int getyCordination() {
		return yCordination;
	}
	public void setyCordination(int yCordination) {
		this.yCordination = yCordination;
	}
	public Card(){
  		
  	}
  	public String getColor() {
		return color;
	}

	private String color;
  	
  	public Card(final String honour, final String suit){
    		this.suit = suit;
    		this.honour = honour;
    		if(suit=="Spade" || suit=="Club") this.color="black";
    		else this.color="red";
    		findCordination();
  	}
  
  	private void findCordination() {
		if(this.getHonour()=="Ace"){
			this.xCordination=2;
		}
		else if(this.getHonour()=="2"){
			this.xCordination=2+(1*73);
		}
		else if(this.getHonour()=="3"){
			this.xCordination=2+(2*73);
		}
		else if(this.getHonour()=="4"){
			this.xCordination=2+(3*73);
		}
		else if(this.getHonour()=="5"){
			this.xCordination=2+(4*73);
		}
		else if(this.getHonour()=="6"){
			this.xCordination=2+(5*73);
		}
		else if(this.getHonour()=="7"){
			this.xCordination=2+(6*73);
		}
		else if(this.getHonour()=="8"){
			this.xCordination=2+(7*73);
		}
		else if(this.getHonour()=="9"){
			this.xCordination=2+(8*73);
		}
		else if(this.getHonour()=="10"){
			this.xCordination=2+(9*73);
		}
		else if(this.getHonour()=="Jack"){
			this.xCordination=2+(10*73);
		}
		else if(this.getHonour()=="Queen"){
			this.xCordination=2+(11*73);
		}
		else if(this.getHonour()=="King"){
			this.xCordination=2+(12*73);
		}
		
		if(this.getColor()=="Club"){
			this.yCordination=3;
		}
		else if(this.getColor()=="Heart"){
			this.yCordination=101;
		}
		else if(this.getColor()=="Spade"){
			this.yCordination=199;
		}
		else if(this.getColor()=="Diamond"){
			this.yCordination=297;
		}
	}
	public String getSuit() {
  		  return suit;
  	}
  
  	public void setSuit(final String suit) {
  		  this.suit = suit;
  	}
  
  	public String getHonour() {
  		  return honour;
  	}
  
  	public void setHonour(final String honour) {
  		  this.honour = honour;
  	}
	
}