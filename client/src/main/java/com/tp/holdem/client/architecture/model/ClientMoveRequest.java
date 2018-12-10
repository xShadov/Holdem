package com.tp.holdem.client.architecture.model;


import com.tp.holdem.client.architecture.model.action.ActionType;
import com.tp.holdem.client.model.Player;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientMoveRequest {
	public ActionType move;
	public Player player;
	public int betAmount;
}
