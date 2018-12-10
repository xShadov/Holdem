package com.tp.holdem.client.strategy;

import com.tp.holdem.client.architecture.model.ClientMoveRequest;
import com.tp.holdem.client.model.Card;
import com.tp.holdem.client.model.HandOperations;
import com.tp.holdem.client.model.HandRank;

import java.util.List;

public class MediumStrategy{/* implements Strategy {
	private final String name = "MediumStrategy";
	private ClientMoveRequest request;
	private HandRank rank;
	private int howMuchToBet;

	@Override
	public Strategy getStrategy() {
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getTag() {
		return request.getTag();
	}

	@Override
	public void whatDoIDo(final KryoServer server, final List<Card> hand, final int betAmount, final int chips) {
		if (server.getLimitType() == "fixed-limit")
			howMuchToBet = server.getFixedChips();
		else
			howMuchToBet = server.getBigBlind();

		rank = HandOperations.findHandRank(0, hand, server.getTable().getCardsOnTable());

		if (server.getTable().getCardsOnTable().size() > 0) {
			if (rank.getHand().value() > 3) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new ClientMoveRequest("ALLIN", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new ClientMoveRequest("BET", howMuchToBet, server.getBetPlayer());
						else if (server.getLimitType() != "fixed-limit")
							request = new ClientMoveRequest("RAISE", howMuchToBet, server.getBetPlayer());
						else
							request = new ClientMoveRequest("CALL", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new ClientMoveRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new ClientMoveRequest("CALL", server.getBetPlayer());
					}
				}
			}

			else if (rank.getHand().value() == 3) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new ClientMoveRequest("CHECK", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new ClientMoveRequest("BET", howMuchToBet, server.getBetPlayer());
						else
							request = new ClientMoveRequest("CHECK", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new ClientMoveRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new ClientMoveRequest("CALL", server.getBetPlayer());
					}
				}
			}

			else if (rank.getHand().value() < 3) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new ClientMoveRequest("CHECK", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new ClientMoveRequest("CHECK", server.getBetPlayer());
						else
							request = new ClientMoveRequest("CHECK", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new ClientMoveRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new ClientMoveRequest("CALL", server.getBetPlayer());
					}
				}
			}
		} else {
			if (server.getMaxBetOnTable() == betAmount) {
				request = new ClientMoveRequest("CHECK", server.getBetPlayer());
			} else {
				if (server.getMaxBetOnTable() >= chips) {
					request = new ClientMoveRequest("ALLIN", server.getBetPlayer());
				} else {
					request = new ClientMoveRequest("CALL", server.getBetPlayer());
				}
			}
		}
		*//*
		 * System.out.print("Bot "
		 * +String.valueOf(server.getBetPlayer()-server.getPlayersCount()) +
		 * ", hand value: "+ rank.getHand().getValue()+", action: "
		 * +request.getTAG()); if(request.getTAG() =="BET" ||
		 * request.getTAG()=="RAISE") System.out.print(" with "
		 * +request.getBetAmount()+" chips"); System.out.println(
		 * ". He had those options:"); for(int i =
		 * 0;server.getPossibleOpitions().size()>i;i++) System.out.print(" "
		 * +server.getPossibleOpitions().get(i)); System.out.println("\r");
		 *//*
		server.handleReceived((Object) request);
	}*/
}
