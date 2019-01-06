package com.tp.holdem.logic.player

import com.tp.holdem.logic.*
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.players.allIn
import com.tp.holdem.logic.players.availableChips
import com.tp.holdem.logic.players.bet
import com.tp.holdem.logic.players.fold
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class PlayerMovesExtensionsSpec : Spek({
    Feature("all in move") {
        lateinit var player: Player

        Scenario("player goes all in") {
            Given("playing player with chips and no bet") {
                player = Player.playing(0).chips(1500)
            }

            When("player performs all in move") {
                player = player.allIn()
            }

            Then("player is all in") {
                assertThat(player.allIn).isTrue()
            }

            Then("player has no available chips") {
                assertThat(player.availableChips()).isEqualTo(0)
            }

            Then("player has 1500 bet this phase") {
                assertThat(player.betAmountThisPhase).isEqualTo(1500)
            }
        }

        Scenario("player goes all in, but is already all in") {
            Given("player with chips who is already all in") {
                player = Player.allIn(0).chips(1500)
            }

            Then("player is already all in, so exception is thrown") {
                assertThatThrownBy { player.allIn() }
            }
        }

        Scenario("player goes all in, but is folded") {
            Given("folded player") {
                player = Player.folded(0)
            }

            Then("player is folded, so exception is thrown") {
                assertThatThrownBy { player.allIn() }
            }
        }

        Scenario("player goes all in, but is sitting") {
            Given("sitting player") {
                player = Player.sitting(0)
            }

            Then("player is sitting, so exception is thrown") {
                assertThatThrownBy { player.allIn() }
            }
        }

        Scenario("player goes all in, but has no chips") {
            Given("player with no chips") {
                player = Player.playing(0).chips(0)
            }

            Then("player has no chips, so exception is thrown") {
                assertThatThrownBy { player.allIn() }
            }
        }
    }

    Feature("fold move") {
        lateinit var player: Player

        Scenario("player folds") {
            Given("playing player") {
                player = Player.playing(0)
            }

            When("player performs fold move") {
                player = player.fold()
            }

            Then("player is folded") {
                assertThat(player.folded).isTrue()
            }
        }

        Scenario("player folds, but is already folded") {
            Given("folded player") {
                player = Player.folded(0)
            }

            Then("player is folded, so exception is thrown") {
                assertThatThrownBy { player.fold() }
            }
        }

        Scenario("player folds, but is sitting") {
            Given("sitting player") {
                player = Player.sitting(0)
            }

            Then("player is sitting, so exception is thrown") {
                assertThatThrownBy { player.fold() }
            }
        }
    }

    Feature("bet move") {
        lateinit var player: Player

        Scenario("player bets") {
            Given("playing player with chips and no bet") {
                player = Player.playing(0).chips(1500)
            }

            When("player performs bet move") {
                player = player.bet(500)
            }

            Then("player available chips amount is reduced") {
                assertThat(player.availableChips()).isEqualTo(1000)
            }

            Then("player has 500 bet this phase") {
                assertThat(player.betAmountThisPhase).isEqualTo(500)
            }

            Then("player is not all in") {
                assertThat(player.allIn).isFalse()
            }
        }

        Scenario("player bets everything he has and goes all in") {
            Given("playing player with chips and no bet") {
                player = Player.playing(0).chips(1500)
            }

            When("player performs bet move") {
                player = player.bet(1500)
            }

            Then("player available chips amount is reduced") {
                assertThat(player.availableChips()).isEqualTo(0)
            }

            Then("player has 500 bet this phase") {
                assertThat(player.betAmountThisPhase).isEqualTo(1500)
            }

            Then("player is all in") {
                assertThat(player.allIn).isTrue()
            }
        }

        Scenario("player bets, but is folded") {
            Given("folded player") {
                player = Player.folded(0)
            }

            Then("player is folded, so exception is thrown") {
                assertThatThrownBy { player.bet(100) }
            }
        }

        Scenario("player bets, but is sitting") {
            Given("sitting player") {
                player = Player.sitting(0)
            }

            Then("player is sitting, so exception is thrown") {
                assertThatThrownBy { player.bet(100) }
            }
        }

        Scenario("player bets, but he has not enough chips") {
            Given("player with chips") {
                player = Player.playing(0).chips(100)
            }

            Then("player has not enough chips, so exception is thrown") {
                assertThatThrownBy { player.bet(500) }
            }
        }
    }
})