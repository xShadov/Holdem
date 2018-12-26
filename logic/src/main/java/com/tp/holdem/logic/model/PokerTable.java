package com.tp.holdem.logic.model;

import com.tp.holdem.model.message.dto.PokerTableDTO;
import io.vavr.collection.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class PokerTable {
	private int potAmount;
	private int smallBlindAmount;
	private int bigBlindAmount;
	@Builder.Default
	private List<Card> cardsOnTable = List.empty();

	public PokerTableDTO toDTO() {
		return PokerTableDTO.builder()
				.potAmount(potAmount)
				.smallBlindAmount(smallBlindAmount)
				.bigBlindAmount(bigBlindAmount)
				.cardsOnTable(cardsOnTable.map(Card::toDTO).toJavaList())
				.build();
	}
}