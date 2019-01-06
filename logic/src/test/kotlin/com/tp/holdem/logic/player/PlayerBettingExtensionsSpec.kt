package com.tp.holdem.logic.player

import com.tp.holdem.logic.*
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.availableChips
import com.tp.holdem.logic.players.betBigBlind
import com.tp.holdem.logic.players.betSmallBlind
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
})