package com.tp.holdem.logic.player

import com.tp.holdem.common.model.Moves
import com.tp.holdem.logic.*
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.*
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class PlayerBettingExtensionsSpec : Spek({
    Feature("available chips count") {
        lateinit var player: Player
        Scenario("counting players chips") {
            Given("playing player with chips and bet this phase") {
                player = Player.playing(0).chips(1500).betThisPhase(700)
            }

            Then("player has 800 available chips") {
                assertThat(player.availableChips()).isEqualTo(800)
            }
        }
    }

    Feature("betting big blind") {
        lateinit var table: PokerTable
        lateinit var player: Player

        Scenario("player correctly bets big blind") {
            Given("playing player and a table") {
                player = Player.playing(0)
                table = PokerTable.sample().players(player)
            }

            When("player bets big blind") {
                player = player.betBigBlind(table)
            }

            Then("player bet amount this phase is equal to big blind") {
                assertThat(player.betAmountThisPhase).isEqualTo(table.bigBlindAmount)
            }
        }

        Scenario("player has not enough chips for big blind so he goes all in") {
            Given("playing player and a table") {
                player = Player.playing(0).chips(10)
                table = PokerTable.withBlinds(40, 20).players(player)
            }

            When("player bets big blind") {
                player = player.betBigBlind(table)
            }

            Then("player is all in") {
                assertThat(player.allIn).isTrue()
            }
        }
    }

    Feature("betting small blind") {
        lateinit var table: PokerTable
        lateinit var player: Player

        Scenario("player correctly bets small blind") {
            Given("playing player and a table") {
                player = Player.playing(0)
                table = PokerTable.sample().players(player)
            }

            When("player bets small blind") {
                player = player.betSmallBlind(table)
            }

            Then("player bet amount this phase is equal to small blind") {
                assertThat(player.betAmountThisPhase).isEqualTo(table.smallBlindAmount)
            }
        }

        Scenario("player has not enough chips for small blind so he goes all in") {
            Given("playing player and a table") {
                player = Player.playing(0).chips(10)
                table = PokerTable.withBlinds(40, 20).players(player)
            }

            When("player bets small blind") {
                player = player.betSmallBlind(table)
            }

            Then("player is all in") {
                assertThat(player.allIn).isTrue()
            }
        }
    }

    Feature("preparation for first bet in round") {
        lateinit var table: PokerTable
        lateinit var player: Player

        Scenario("player is correctly prepared for first bet in round") {
            Given("playing player, player with big blind bet and a table") {
                table = PokerTable.withBlinds(40, 20)
                        .players(
                                Player.playing(0).chips(1500),
                                Player.playing(1).betThisPhase(40)
                        )
            }

            When("player is prepared for first bet") {
                player = table.findPlayer(0).prepareForFirstBetInRound(table)
            }

            Then("player has correct moves") {
                assertThat(player.possibleMoves).containsExactlyInAnyOrder(Moves.FOLD, Moves.CALL, Moves.RAISE)
            }

            Then("player has correct minimum/maximum bets calculated") {
                assertThat(player.minimumBet).isEqualTo(table.bigBlindAmount)
                assertThat(player.maximumBet).isEqualTo(1500 - table.bigBlindAmount)
            }
        }

        Scenario("player who does not have enough chips to call big blind, prepared for first bet in round") {
            Given("playing player, player with big blind bet and a table") {
                table = PokerTable.withBlinds(40, 20)
                        .players(
                                Player.playing(0).chips(30),
                                Player.playing(1).betThisPhase(40)
                        )
            }

            When("player is prepared for first bet") {
                player = table.findPlayer(0).prepareForFirstBetInRound(table)
            }

            Then("player has correct moves") {
                assertThat(player.possibleMoves).containsExactlyInAnyOrder(Moves.FOLD, Moves.ALLIN)
            }

            Then("player has correct minimum/maximum bets calculated") {
                assertThat(player.minimumBet).isEqualTo(0)
                assertThat(player.maximumBet).isEqualTo(0)
            }
        }
    }

    Feature("preparation for bet in phase") {
        lateinit var table: PokerTable
        lateinit var player: Player

        Scenario("player with not enough chips to call highest bet in phase is prepared for bet in phase") {
            Given("playing player, player with a bet, and a table") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(100),
                                Player.playing(1).betThisPhase(200)
                        )
            }

            When("player is prepared for bet in phase") {
                player = table.findPlayer(0).prepareForBetInPhase(table)
            }

            Then("player has correct moves") {
                assertThat(player.possibleMoves).containsExactlyInAnyOrder(Moves.FOLD, Moves.ALLIN)
            }

            Then("player has correct minimum/maximum bets calculated") {
                assertThat(player.minimumBet).isEqualTo(0)
                assertThat(player.maximumBet).isEqualTo(0)
            }
        }

        Scenario("player is correctly prepared for bet in phase in which someone already bet") {
            Given("playing player, player with a bet, and a table") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1500),
                                Player.playing(1).betThisPhase(200)
                        )
            }

            When("player is prepared for bet in phase") {
                player = table.findPlayer(0).prepareForBetInPhase(table)
            }

            Then("player has correct moves") {
                assertThat(player.possibleMoves).containsExactlyInAnyOrder(Moves.CALL, Moves.FOLD, Moves.RAISE)
            }

            Then("player has correct minimum/maximum bets calculated") {
                assertThat(player.minimumBet).isEqualTo(table.bigBlindAmount)
                assertThat(player.maximumBet).isEqualTo(1500 - 200)
            }
        }

        Scenario("player is correctly prepared for his second bet in phase") {
            Given("players with bets and a table") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1500).betThisPhase(300),
                                Player.playing(1).betThisPhase(500)
                        )
            }

            When("player is prepared for bet in phase") {
                player = table.findPlayer(0).prepareForBetInPhase(table)
            }

            Then("player has correct moves") {
                assertThat(player.possibleMoves).containsExactlyInAnyOrder(Moves.CALL, Moves.FOLD, Moves.RAISE)
            }

            Then("player has correct minimum/maximum bets calculated") {
                assertThat(player.minimumBet).isEqualTo(table.bigBlindAmount)
                assertThat(player.maximumBet).isEqualTo(1500 - 300 - (500 - 300))
            }
        }

        Scenario("player is correctly prepared for bet in phase which is a first bet in this phase") {
            Given("players without any bets and a table") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1500),
                                Player.playing(1)
                        )
            }

            When("player is prepared for bet in phase") {
                player = table.findPlayer(0).prepareForBetInPhase(table)
            }

            Then("player has correct moves") {
                assertThat(player.possibleMoves).containsExactlyInAnyOrder(Moves.BET, Moves.FOLD, Moves.CHECK)
            }

            Then("player has correct minimum/maximum bets calculated") {
                assertThat(player.minimumBet).isEqualTo(table.bigBlindAmount)
                assertThat(player.maximumBet).isEqualTo(1500)
            }
        }

        Scenario("player is correctly prepared for bet in phase in which all players have equal bets") {
            Given("players with equal bets and a table") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1500).betThisPhase(600),
                                Player.playing(1).betThisPhase(600)
                        )
            }

            When("player is prepared for bet in phase") {
                player = table.findPlayer(0).prepareForBetInPhase(table)
            }

            Then("player has correct moves") {
                assertThat(player.possibleMoves).containsExactlyInAnyOrder(Moves.CHECK, Moves.FOLD, Moves.RAISE)
            }

            Then("player has correct minimum/maximum bets calculated") {
                assertThat(player.minimumBet).isEqualTo(table.bigBlindAmount)
                assertThat(player.maximumBet).isEqualTo(1500 - 600 - (600 - 600))
            }
        }
    }
})