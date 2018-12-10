package com.tp.holdem.client.strategy;

import com.tp.holdem.client.architecture.model.ClientMoveRequest;
import com.tp.holdem.client.model.Card;

import java.util.List;

public class EasyStrategy {/*implements Strategy {

	private final static String NAME = "EasyStrategy";
	private ClientMoveRequest request;

	@Override
	public Strategy getStrategy() {
		return this;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public String getTag() {
		return request.getTag();
	}

	@Override
	public void whatDoIDo(final KryoServer server, final List<Card> hand, final int betAmount, final int chips) {
		if (server.getMaxBetOnTable() == betAmount)
			request = new ClientMoveRequest("CHECK", server.getBetPlayer());
		else if (server.getMaxBetOnTable() > betAmount) {
			if (server.getMaxBetOnTable() >= chips)
				request = new ClientMoveRequest("ALLIN", server.getBetPlayer());
			else
				request = new ClientMoveRequest("CALL", server.getBetPlayer());
		}
		server.handleReceived((Object) request);
	}*/

}
