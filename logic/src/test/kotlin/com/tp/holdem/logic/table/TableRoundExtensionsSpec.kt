package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.*
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.availableChips
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TableRoundExtensionsSpec : Spek({
    Feature("starting new round") {
        lateinit var table: PokerTable

        Scenario("table has only 1 player, so exception is thrown") {
            Given("table with  1playing player") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0)
                        )
            }

            When("start first round there is no enough players") {
                assertThatThrownBy { table.newRound(0) }
            }
        }

        Scenario("table has 2 players, but only 1 is playing, so exception is thrown") {
            Given("table with  1playing player") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.sitting(1)
                        )
            }

            When("start first round there is no enough players") {
                assertThatThrownBy { table.newRound(0) }
            }
        }

        Scenario("table has 2 players, but only 1 has no chips, so exception is thrown") {
            Given("table with  1playing player") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1).chips(0)
                        )
            }

            When("start first round there is no enough players") {
                assertThatThrownBy { table.newRound(0) }
            }
        }

        Scenario("table has only 2 players, so same player is dealer and small blind") {
            Given("table with 2 playing players") {
                table = PokerTable.withBlinds(40, 20)
                        .players(
                                Player.playing(0).chips(1500),
                                Player.playing(1).chips(1500)
                        )
            }

            When("start first round") {
                table = table.newRound(0)
            }

            Then("player with number 1 is small blind and dealer") {
                assertThat(table.smallBlindPlayerNumber).isEqualTo(PlayerNumber(1))
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(1500 - 20)
                assertThat(table.findPlayer(1).betAmountThisPhase).isEqualTo(20)

                assertThat(table.dealerPlayerNumber).isEqualTo(PlayerNumber(1))
            }

            Then("players with number 0 is big blind") {
                assertThat(table.bigBlindPlayerNumber).isEqualTo(PlayerNumber(0))
                assertThat(table.findPlayer(0).availableChips()).isEqualTo(1500 - 40)
                assertThat(table.findPlayer(0).betAmountThisPhase).isEqualTo(40)
            }

            Then("table is prepared for new round") {
                assertThat(table.isPreparedForNewRound()).isTrue()
            }

            Then("each player has 2 cards") {
                assertThat(table.allPlayers).allMatch { it.hand.size() == 2 }
                assertThat(table.cardsInDeck()).isEqualTo(52 - (2 * 2))
            }
        }

        Scenario("table has 3 players, so every player has different button") {
            Given("table with 2 playing players") {
                table = PokerTable.withBlinds(40, 20)
                        .players(
                                Player.playing(0).chips(1500),
                                Player.playing(1).chips(1500),
                                Player.playing(2).chips(1500)
                        )
            }

            When("start first round") {
                table = table.newRound(0)
            }

            Then("player with number 1 is small blind") {
                assertThat(table.smallBlindPlayerNumber).isEqualTo(PlayerNumber(1))
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(1500 - 20)
                assertThat(table.findPlayer(1).betAmountThisPhase).isEqualTo(20)
            }

            Then("players with number 2 is big blind") {
                assertThat(table.bigBlindPlayerNumber).isEqualTo(PlayerNumber(2))
                assertThat(table.findPlayer(2).availableChips()).isEqualTo(1500 - 40)
                assertThat(table.findPlayer(2).betAmountThisPhase).isEqualTo(40)
            }

            Then("players with number 0 is dealer") {
                assertThat(table.dealerPlayerNumber).isEqualTo(PlayerNumber(0))
            }

            Then("table is prepared for new round") {
                assertThat(table.isPreparedForNewRound()).isTrue()
            }

            Then("each player has 2 cards") {
                assertThat(table.allPlayers).allMatch { it.hand.size() == 2 }
                assertThat(table.cardsInDeck()).isEqualTo(52 - (3 * 2))
            }
        }
    }

    Feature("ending a round") {
        lateinit var table: PokerTable

        Scenario("table has 1 clear winner") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0)
                                        .chips(1300)
                                        .generalBet(200)
                                        .betThisPhase(100)
                                        .hand("AC", "AS"),
                                Player.playing(1)
                                        .chips(1300)
                                        .generalBet(200)
                                        .betThisPhase(100)
                                        .hand("5C", "8S")
                        )
                        .cardsOnTable("2C", "2D", "2H", "10C", "QC")
            }

            When("round is over") {
                table = table.roundOver()
            }

            Then("table is in OVER phase") {
                assertThat(table.phase).isEqualTo(Phase.OVER)
            }

            Then("allPlayers betThisPhase goes to general bet") {
                assertThat(table.allPlayers).allMatch { it.betAmountThisPhase == 0 }
                assertThat(table.allPlayers).allMatch { it.betAmount == 200 + 100 }
            }

            Then("winner player number is 0") {
                assertThat(table.winnerPlayerNumber).isEqualTo(PlayerNumber(0))
            }

            Then("players have correct amount of chips") {
                assertThat(table.findPlayer(0).chipsAmount).isEqualTo(1500 + 300)
                assertThat(table.findPlayer(1).chipsAmount).isEqualTo(1500 - 300)
            }
        }

        Scenario("only 1 player is not-folded so he automatically wins") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0)
                                        .chips(1300)
                                        .generalBet(200)
                                        .betThisPhase(100),
                                Player.folded(1)
                                        .chips(1300)
                                        .generalBet(200)
                                        .betThisPhase(100),
                                Player.folded(2)
                                        .chips(1300)
                                        .generalBet(200)
                                        .betThisPhase(100)
                        )
            }

            When("round is over") {
                table = table.roundOver()
            }

            Then("table is in OVER phase") {
                assertThat(table.phase).isEqualTo(Phase.OVER)
            }

            Then("allPlayers betThisPhase goes to general bet") {
                assertThat(table.allPlayers).allMatch { it.betAmountThisPhase == 0 }
                assertThat(table.allPlayers).allMatch { it.betAmount == 200 + 100 }
            }

            Then("winner player number is 0") {
                assertThat(table.winnerPlayerNumber).isEqualTo(PlayerNumber(0))
            }

            Then("players have correct amount of chips") {
                assertThat(table.findPlayer(0).chipsAmount).isEqualTo(1500 + 300 + 300)
                assertThat(table.findPlayer(1).chipsAmount).isEqualTo(1500 - 300)
                assertThat(table.findPlayer(2).chipsAmount).isEqualTo(1500 - 300)
            }
        }
    }
})