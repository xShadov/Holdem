package com.tp.holdem.core.strategy;

import com.tp.holdem.core.*;
import com.tp.holdem.core.model.Card;
import com.tp.holdem.core.model.HandRank;
import com.tp.holdem.core.model.SampleRequest;

import java.util.List;
import java.util.Random;

public class HardStrategy implements Strategy {
	private final String name = "HardStrategy";
	private SampleRequest request;
	private HandRank rank;
	private final Random random = new Random();
	private int a; // wylosowana liczba
	private int howMuchToBet;

	@Override
	public Strategy getStrategy() {
		return this;
	}

	public String getTag() {
		return request.getTag();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void whatDoIDo(final KryoServer server, final List<Card> hand, final int betAmount, final int chips) {
		if (server.getLimitType() == "fixed-limit")
			howMuchToBet = server.getFixedChips();
		else
			howMuchToBet = server.getBigBlind();

		rank = HandOperations.findHandRank(0, hand, server.getTable().getCardsOnTable());

		// FLOP
		if (server.getTable().getCardsOnTable().size() == 3) {
			if (rank.getHand().value() > 4) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						else if (server.getLimitType() != "fixed-limit")
							request = new SampleRequest("RAISE", howMuchToBet, server.getBetPlayer());
						else
							request = new SampleRequest("CALL", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			} else if (rank.getHand().value() == 4) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new SampleRequest("CHECK", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						else
							request = new SampleRequest("CHECK", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			}

			else if (rank.getHand().value() < 4) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new SampleRequest("CHECK", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new SampleRequest("CHECK", server.getBetPlayer());
						else
							request = new SampleRequest("CHECK", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			}
		}

		// TURN
		else if (server.getTable().getCardsOnTable().size() == 4) {
			if (rank.getHand().value() > 4) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						else if (server.getLimitType() != "fixed-limit")
							request = new SampleRequest("RAISE", howMuchToBet, server.getBetPlayer());
						else
							request = new SampleRequest("CALL", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			} else if (rank.getHand().value() == 4) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new SampleRequest("CHECK", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						else
							request = new SampleRequest("CHECK", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			} else if (rank.getHand().value() == 1) {
				a = random.nextInt(10);
				if (a < 4) {
					if (server.getMaxBetOnTable() == 0) {
						if (server.getBigBlind() >= chips) {
							request = new SampleRequest("ALLIN", server.getBetPlayer());
						} else {
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						}
					} else {
						request = new SampleRequest("FOLD", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() == betAmount) {
						request = new SampleRequest("CHECK", server.getBetPlayer());
					} else {
						request = new SampleRequest("FOLD", server.getBetPlayer());
					}
				}
			} else if (rank.getHand().value() == 3) {
				if (server.getMaxBetOnTable() == betAmount) {
					request = new SampleRequest("CHECK", server.getBetPlayer());
				} else {
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			} else if (rank.getHand().value() == 2) {
				if (server.getMaxBetOnTable() == betAmount) {
					request = new SampleRequest("CHECK", server.getBetPlayer());
				} else {
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
		}

		// RIVER
		else if (server.getTable().getCardsOnTable().size() == 5) {
			if (rank.getHand().value() > 4) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						else if (server.getLimitType() != "fixed-limit")
							request = new SampleRequest("RAISE", howMuchToBet, server.getBetPlayer());
						else
							request = new SampleRequest("CALL", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			} else if (rank.getHand().value() == 4) {
				if (server.getMaxBetOnTable() <= betAmount) {
					if (server.getBigBlind() >= chips) {
						request = new SampleRequest("CHECK", server.getBetPlayer());
					} else {
						if (server.getMaxBetOnTable() == 0)
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						else
							request = new SampleRequest("CHECK", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() >= chips) {
						request = new SampleRequest("ALLIN", server.getBetPlayer());
					} else {
						request = new SampleRequest("CALL", server.getBetPlayer());
					}
				}
			} else if (rank.getHand().value() == 1) {
				a = random.nextInt(10);
				if (a < 2) {
					if (server.getMaxBetOnTable() == 0) {
						if (server.getBigBlind() >= chips) {
							request = new SampleRequest("ALLIN", server.getBetPlayer());
						} else {
							request = new SampleRequest("BET", howMuchToBet, server.getBetPlayer());
						}
					} else {
						request = new SampleRequest("FOLD", server.getBetPlayer());
					}
				} else {
					if (server.getMaxBetOnTable() == betAmount) {
						request = new SampleRequest("CHECK", server.getBetPlayer());
					} else {
						request = new SampleRequest("FOLD", server.getBetPlayer());
					}
				}
			} else if (rank.getHand().value() == 3) {
				if (server.getMaxBetOnTable() == betAmount) {
					request = new SampleRequest("CHECK", server.getBetPlayer());
				} else {
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			} else if (rank.getHand().value() == 2) {
				if (server.getMaxBetOnTable() == betAmount) {
					request = new SampleRequest("CHECK", server.getBetPlayer());
				} else {
					request = new SampleRequest("FOLD", server.getBetPlayer());
				}
			}
		} else {
			if (server.getMaxBetOnTable() == betAmount) {
				request = new SampleRequest("CHECK", server.getBetPlayer());
			} else {
				if (server.getMaxBetOnTable() >= chips) {
					request = new SampleRequest("ALLIN", server.getBetPlayer());
				} else {
					request = new SampleRequest("CALL", server.getBetPlayer());
				}
			}
		}
		/*
		 * System.out.print("Bot "
		 * +String.valueOf(server.getBetPlayer()-server.getPlayersCount()) +
		 * ", hand value: "+ rank.getHand().getValue()+", action: "
		 * +request.getTAG()); if(request.getTAG() =="BET" ||
		 * request.getTAG()=="RAISE") System.out.print(" with "
		 * +request.getBetAmount()+" chips"); System.out.println(
		 * ". He had those options:"); for(int i =
		 * 0;server.getPossibleOpitions().size()>i;i++) System.out.print(" "
		 * +server.getPossibleOpitions().get(i)); System.out.println("\r");
		 */
		server.handleReceived((Object) request);
	}
}
