package com.tp.holdem.common.message.dto;

import com.tp.holdem.common.model.Phase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PokerTableDTO {
	private int potAmount;
	private int potAmountThisPhase;
	private int smallBlindAmount;
	private int bigBlindAmount;
	private PlayerDTO dealer;
	private PlayerDTO bigBlind;
	private PlayerDTO smallBlind;
	private PlayerDTO bettingPlayer;
	private List<PlayerDTO> winnerPlayers;
	private Phase phase;
	private boolean gameOver;
	@Builder.Default
	private List<PlayerDTO> allPlayers = new ArrayList<>();
	@Builder.Default
	private List<CardDTO> cardsOnTable = new ArrayList<>();
}