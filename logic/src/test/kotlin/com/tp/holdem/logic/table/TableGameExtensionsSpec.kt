package com.tp.holdem.logic.table

import com.tp.holdem.logic.*
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TableGameExtensionsSpec : Spek({
    Feature("starting new game") {
        lateinit var table: PokerTable

        Scenario("starting a new game") {
            val chipsAmount = 2500
            Given("table in initial state with sitting players") {
                table = PokerTable.sample()
                        .players(
                                Player.sitting(0),
                                Player.sitting(1)
                        )
            }

            When("new game is started") {
                table = table.newGame(chipsAmount)
            }

            Then("all players are in game") {
                assertThat(table.allPlayers).allMatch(Player::inGame)
            }

            Then("all players have chips") {
                assertThat(table.allPlayers).allMatch { it.chipsAmount == chipsAmount }
            }
        }

        Scenario("staring a new game with chips amount smaller than big blind, so exception is thrown") {
            Given("table in initial state with sitting players") {
                table = PokerTable.sample()
                        .players(
                                Player.sitting(0),
                                Player.sitting(1)
                        )
            }

            Then("starting chips amount is lower than big blind so exception is thrown") {
                assertThatThrownBy { table.newGame(table.bigBlindAmount - 1) }
            }
        }
    }

    Feature("player connecting to table") {
        lateinit var table: PokerTable

        Scenario("adding player to table") {
            val newPlayer = Player.playing(0)

            Given("table in initial state") {
                table = PokerTable.sample()
            }

            When("player is added to the table") {
                table = table.addPlayer(newPlayer)
            }

            Then("player is at the table") {
                assertThat(table.findPlayer(0)).isNotNull
            }
        }
    }

    Feature("player leaving the table") {
        lateinit var table: PokerTable

        Scenario("player leaving from the table") {
            Given("table in initial state with a single player") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0)
                        )
            }

            When("player has left the table") {
                table = table.playerLeft(PlayerNumber.of(0))
            }

            Then("player is still there, but he's sitting") {
                assertThat(table.findPlayer(0).inGame).isFalse()
            }
        }

        Scenario("disconnecting player who is not connected") {
            Given("table in initial state") {
                table = PokerTable.sample()
            }

            Then("disconnecting not existing player, so exception is thrown") {
                assertThatThrownBy { table.playerLeft(PlayerNumber.of(0)) }
            }
        }
    }

    Feature("ending a game") {
        lateinit var table: PokerTable

        Scenario("ending current game") {
            Given("table in initial state with a single player") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
            }

            When("game is ended") {
                table = table.gameOver()
            }

            Then("table is in game over state") {
                assertThat(table.gameOver).isTrue()
            }

            Then("players are not in game anymore") {
                assertThat(table.allPlayers).allMatch { it.inGame.not() }
                assertThat(table.allPlayers).allMatch { it.isNotInBettingTurn() }
            }
        }
    }
})
