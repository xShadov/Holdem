package com.tp.holdem.common.message.dto;

import com.tp.holdem.common.model.Moves;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentPlayerDTO {
	private int number;

	private int minimumBet;
	private int maximumBet;
	private int betAmount;
	private int betAmountThisPhase;
	private int chipsAmount;

	private boolean inGame;
	private boolean allIn;
	private boolean folded;

	private String name;

	@Builder.Default
	private List<Moves> possibleMoves = new ArrayList<>();
	@Builder.Default
	private List<CardDTO> hand = new ArrayList<>();

	public boolean playing() {
		return isInGame() && !isFolded();
	}
}
