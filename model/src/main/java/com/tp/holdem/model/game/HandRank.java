package com.tp.holdem.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HandRank {
	private Player player;
	private Hands hand;
	private List<Card> cardsThatMakeHand;
}
