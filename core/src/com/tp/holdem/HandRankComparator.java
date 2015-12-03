package com.tp.holdem;

import java.util.Comparator;

public class HandRankComparator implements Comparator<HandRank> {

	@Override
	public int compare(HandRank hand1, HandRank hand2) {
		if(hand1.getHand()==hand2.getHand()){
			if(hand1.getHand()==HandRankingEnum.ROYAL_FLUSH) return 0;
			else if(hand1.getHand()==HandRankingEnum.STRAIGHT_FLUSH){
				if(hand1.getCardsThatMakeDeck().get(4).getValue()>hand2.getCardsThatMakeDeck().get(4).getValue()){
					return 1;
				}
				else if(hand1.getCardsThatMakeDeck().get(4).getValue()<hand2.getCardsThatMakeDeck().get(4).getValue()){
					return -1;
				}
				else{
					return 0;
				}
			}
			else if(hand1.getHand()==HandRankingEnum.FOUR_OF_A_KIND){
				int index1;
				int index2;
				if(hand1.getCardsThatMakeDeck().get(0).getHonour()==hand1.getCardsThatMakeDeck().get(1).getHonour()){
					index1 = 0;
				}
				else index1 = 1;
				if(hand2.getCardsThatMakeDeck().get(0).getHonour()==hand2.getCardsThatMakeDeck().get(1).getHonour()){
					index2 = 0;
				}
				else index2 = 1;
				if(hand1.getCardsThatMakeDeck().get(index1).getValue()>hand2.getCardsThatMakeDeck().get(index2).getValue()) return 1;
				else if(hand1.getCardsThatMakeDeck().get(index1).getValue()<hand2.getCardsThatMakeDeck().get(index2).getValue()) return -1;
				else{
					if(index1==0) index1=4;
					else index1=0;
					if(index2==0) index2=4;
					else index2=0;
					if(hand1.getCardsThatMakeDeck().get(index1).getValue()>hand2.getCardsThatMakeDeck().get(index2).getValue()) return 1;
					else if(hand1.getCardsThatMakeDeck().get(index1).getValue()<hand2.getCardsThatMakeDeck().get(index2).getValue()) return -1;
					else return 0;
				}
			}
			else if(hand1.getHand()==HandRankingEnum.FULL_HOUSE){
				int indexTriples1;
				int indexDoubles1;
				int indexTriples2;
				int indexDoubles2;
				if(hand1.getCardsThatMakeDeck().get(0).getValue()==hand1.getCardsThatMakeDeck().get(1).getValue()
						&& hand1.getCardsThatMakeDeck().get(0).getValue()==hand1.getCardsThatMakeDeck().get(2).getValue()){
					indexTriples1 = 0;
					indexDoubles1 = 3;
				}
				else{
					indexTriples1 = 3;
					indexDoubles1 = 0;
				}
				if(hand2.getCardsThatMakeDeck().get(0).getValue()==hand2.getCardsThatMakeDeck().get(1).getValue()
						&& hand2.getCardsThatMakeDeck().get(0).getValue()==hand2.getCardsThatMakeDeck().get(2).getValue()){
					indexTriples2 = 0;
					indexDoubles2 = 3;
				}
				else{
					indexTriples2 = 3;
					indexDoubles2 = 0;
				}
				if(hand1.getCardsThatMakeDeck().get(indexTriples1).getValue()>hand2.getCardsThatMakeDeck().get(indexTriples2).getValue()){
					return 1;
				}
				else if(hand1.getCardsThatMakeDeck().get(indexTriples1).getValue()<hand2.getCardsThatMakeDeck().get(indexTriples2).getValue()){
					return -1;
				}
				else{
					if(hand1.getCardsThatMakeDeck().get(indexDoubles1).getValue()<hand2.getCardsThatMakeDeck().get(indexDoubles2).getValue()){
						return 1;
					}
					else if(hand1.getCardsThatMakeDeck().get(indexDoubles1).getValue()<hand2.getCardsThatMakeDeck().get(indexDoubles2).getValue()){
						return -1;
					} else {
						return 0;
					}
				}
			}
			else if(hand1.getHand()==HandRankingEnum.FLUSH){
				for(int i=4; i>=0; i--){
					if(hand1.getCardsThatMakeDeck().get(i).getValue()>hand2.getCardsThatMakeDeck().get(i).getValue()){
						return 1;
					}
					else if(hand1.getCardsThatMakeDeck().get(i).getValue()<hand2.getCardsThatMakeDeck().get(i).getValue()){
						return -1;
					}
					else{
						if(i==0) return 0;
					}
				}
			}
			else if(hand1.getHand()==HandRankingEnum.STRAIGHT){
				if(hand1.getCardsThatMakeDeck().get(0).getValue()>hand2.getCardsThatMakeDeck().get(0).getValue()){
					return 1;
				}
				else if(hand1.getCardsThatMakeDeck().get(0).getValue()<hand2.getCardsThatMakeDeck().get(0).getValue()){
					return -1;
				}
				else{
					return 0;
				}
			}
			else if(hand1.getHand()==HandRankingEnum.THREE_OF_A_KIND){
				int index1;
				int index2;
				if(hand1.getCardsThatMakeDeck().get(0).getValue()==hand1.getCardsThatMakeDeck().get(1).getValue()
						&& hand1.getCardsThatMakeDeck().get(0).getValue()==hand1.getCardsThatMakeDeck().get(2).getValue()){
					index1 = 0;
				}
				else if(hand1.getCardsThatMakeDeck().get(1).getValue()==hand1.getCardsThatMakeDeck().get(2).getValue()
						&& hand1.getCardsThatMakeDeck().get(1).getValue()==hand1.getCardsThatMakeDeck().get(3).getValue()){
					index1 = 1;
				}
				else index1 = 2;
				if(hand2.getCardsThatMakeDeck().get(0).getValue()==hand2.getCardsThatMakeDeck().get(1).getValue()
						&& hand2.getCardsThatMakeDeck().get(0).getValue()==hand2.getCardsThatMakeDeck().get(2).getValue()){
					index2 = 0;
				}
				else if(hand2.getCardsThatMakeDeck().get(1).getValue()==hand2.getCardsThatMakeDeck().get(2).getValue()
						&& hand2.getCardsThatMakeDeck().get(1).getValue()==hand2.getCardsThatMakeDeck().get(3).getValue()){
					index2 = 1;
				}
				else index2 = 2;
				if(hand1.getCardsThatMakeDeck().get(index1).getValue()>hand1.getCardsThatMakeDeck().get(index2).getValue()){
					return 1;
				}
				else if(hand1.getCardsThatMakeDeck().get(index1).getValue()<hand1.getCardsThatMakeDeck().get(index2).getValue()){
					return -1;
				}
				else{
					int index1kicker;
					int index2kicker;
					if(index1==0 || index1==1) index1kicker=4;
					else index1kicker=1;
					if(index2==0 || index2==1) index2kicker=4;
					else index2kicker=1;
					if(hand1.getCardsThatMakeDeck().get(index1kicker).getValue()>hand1.getCardsThatMakeDeck().get(index1kicker).getValue()){
						return 1;
					}
					else if(hand1.getCardsThatMakeDeck().get(index1kicker).getValue()<hand1.getCardsThatMakeDeck().get(index1kicker).getValue()){
						return -1;
					}
					else{
						if(index1==0 && index1kicker==4) index1=3;
						else index1=0;
						if(index2==0 && index2kicker==4) index2=3;
						else index2=0;
						if(hand1.getCardsThatMakeDeck().get(index1).getValue()>hand1.getCardsThatMakeDeck().get(index2).getValue()){
							return 1;
						}
						else if(hand1.getCardsThatMakeDeck().get(index1).getValue()<hand1.getCardsThatMakeDeck().get(index2).getValue()){
							return -1;
						}
						else return 0;
					}
				}
			}
			else if(hand1.getHand()==HandRankingEnum.TWO_PAIR){
				int index1first;
				int index2first;
				if(hand1.getCardsThatMakeDeck().get(4).getValue()==hand1.getCardsThatMakeDeck().get(3).getValue()){
					index1first=4;
				}
				else index1first=3;
				if(hand2.getCardsThatMakeDeck().get(4).getValue()==hand2.getCardsThatMakeDeck().get(3).getValue()){
					index2first=4;
				}
				else index2first=3;
				if(hand1.getCardsThatMakeDeck().get(index1first).getValue()>hand2.getCardsThatMakeDeck().get(index2first).getValue()){
					return 1;
				}
				else if(hand1.getCardsThatMakeDeck().get(index1first).getValue()<hand2.getCardsThatMakeDeck().get(index2first).getValue()){
					return -1;
				}
				else{
					int index1second;
					int index2second;
					if(hand1.getCardsThatMakeDeck().get(0).getValue()==hand1.getCardsThatMakeDeck().get(2).getValue()){
						index1second=0;
					}
					else index1second=1;
					if(hand2.getCardsThatMakeDeck().get(0).getValue()==hand2.getCardsThatMakeDeck().get(1).getValue()){
						index2second=0;
					}
					else index2second=1;
					if(hand1.getCardsThatMakeDeck().get(index1second).getValue()>hand2.getCardsThatMakeDeck().get(index2second).getValue()){
						return 1;
					}
					else if(hand1.getCardsThatMakeDeck().get(index1second).getValue()<hand2.getCardsThatMakeDeck().get(index2second).getValue()){
						return -1;
					}
					else{
						int indexKicker1;
						int indexKicker2;
						if(index1first==4 && index1second==0) indexKicker1=2;
						else if(index1first==4 && index1second==1) indexKicker1=0;
						else indexKicker1=4;
						if(index2first==4 && index2second==0) indexKicker2=2;
						else if(index2first==4 && index2second==1) indexKicker2=0;
						else indexKicker2=4;
						if(hand1.getCardsThatMakeDeck().get(indexKicker1).getValue()>hand2.getCardsThatMakeDeck().get(indexKicker2).getValue()){
							return 1;
						}
						else if(hand1.getCardsThatMakeDeck().get(indexKicker1).getValue()<hand2.getCardsThatMakeDeck().get(indexKicker2).getValue()){
							return -1;
						}
						else return 0;
					}
				}
			}
			else if(hand1.getHand()==HandRankingEnum.PAIR){
				int index1;
				int index2;
				if(hand1.getCardsThatMakeDeck().get(4).getValue()==hand1.getCardsThatMakeDeck().get(3).getValue()){
					index1=4;
				}
				else if(hand1.getCardsThatMakeDeck().get(3).getValue()==hand1.getCardsThatMakeDeck().get(2).getValue()){
					index1=3;
				}
				else if(hand1.getCardsThatMakeDeck().get(2).getValue()==hand1.getCardsThatMakeDeck().get(1).getValue()){
					index1=2;
				}
				else index1=1;
				if(hand2.getCardsThatMakeDeck().get(4).getValue()==hand2.getCardsThatMakeDeck().get(3).getValue()){
					index2=4;
				}
				else if(hand2.getCardsThatMakeDeck().get(3).getValue()==hand2.getCardsThatMakeDeck().get(2).getValue()){
					index2=3;
				}
				else if(hand2.getCardsThatMakeDeck().get(2).getValue()==hand2.getCardsThatMakeDeck().get(1).getValue()){
					index2=2;
				}
				else index2=1;
				if(hand1.getCardsThatMakeDeck().get(index1).getValue()>hand2.getCardsThatMakeDeck().get(index2).getValue()){
					return 1;
				}
				else if(hand1.getCardsThatMakeDeck().get(index1).getValue()<hand2.getCardsThatMakeDeck().get(index2).getValue()){
					return -1;
				}
				else{
					int index1Kicker;
					int index2Kicker;
					if(index1==4) index1Kicker=2;
					else index1Kicker=4;
					if(index2==4) index2Kicker=2;
					else index2Kicker=4;
					if(hand1.getCardsThatMakeDeck().get(index1Kicker).getValue()>hand2.getCardsThatMakeDeck().get(index2Kicker).getValue()){
						return 1;
					}
					else if(hand1.getCardsThatMakeDeck().get(index1Kicker).getValue()<hand2.getCardsThatMakeDeck().get(index2Kicker).getValue()){
						return -1;
					}
					else{
						if(index1==4) index1Kicker=1;
						else if(index1==3) index1Kicker=1;
						else index1Kicker=3;
						if(index2==4) index2Kicker=1;
						else if(index2==3) index2Kicker=1;
						else index2Kicker=3;
						if(hand1.getCardsThatMakeDeck().get(index1Kicker).getValue()>hand2.getCardsThatMakeDeck().get(index2Kicker).getValue()){
							return 1;
						}
						else if(hand1.getCardsThatMakeDeck().get(index1Kicker).getValue()<hand2.getCardsThatMakeDeck().get(index2Kicker).getValue()){
							return -1;
						}
						else{
							if(index1==1) index1Kicker=2;
							else index1Kicker=0;
							if(index2==1) index2Kicker=2;
							else index2Kicker=0;
							if(hand1.getCardsThatMakeDeck().get(index1Kicker).getValue()>hand2.getCardsThatMakeDeck().get(index2Kicker).getValue()){
								return 1;
							}
							else if(hand1.getCardsThatMakeDeck().get(index1Kicker).getValue()<hand2.getCardsThatMakeDeck().get(index2Kicker).getValue()){
								return -1;
							}
							else return 0;
						}
					}
				}
			}
			else{
				for(int i=4; i>=0; i--){
					if(hand1.getCardsThatMakeDeck().get(i).getValue()>hand2.getCardsThatMakeDeck().get(i).getValue()){
						return 1;
					}
					else if(hand1.getCardsThatMakeDeck().get(i).getValue()<hand2.getCardsThatMakeDeck().get(i).getValue()){
						return -1;
					}
					else{
						if(i==0) return 0;
					}
				}
			}
		}
		else{
			if(hand1.getHand().getValue()>hand2.getHand().getValue()){
				return 1;
			}
			else return -1;
		}
		return 0;
	}
}
