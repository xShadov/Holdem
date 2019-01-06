package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Moves
import com.tp.holdem.logic.*
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TableCalculatingExtensionsSpec : Spek({
    Feature("calculating highest bet") {
        lateinit var table: PokerTable

        Scenario("highest bet is a sum of players bets") {
            Given("table with 2 players with bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(100),
                                Player.playing(1).betThisPhase(200)
                        )
            }

            Then("highest bet is calculated") {
                assertThat(table.highestBetThisPhase()).isEqualTo(200)
            }
        }

        Scenario("players have no bets so highest bet is 0") {
            Given("table with 2 players with no bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
            }

            Then("highest bet is calculated") {
                assertThat(table.highestBetThisPhase()).isEqualTo(0)
            }
        }

        Scenario("there are no players so highest bet is 0") {
            Given("table with no players") {
                table = PokerTable.sample()
            }

            Then("highest bet is calculated") {
                assertThat(table.highestBetThisPhase()).isEqualTo(0)
            }
        }
    }

    Feature("calculating whole pot amount") {
        lateinit var table: PokerTable

        Scenario("whole pot is sum of bet amounts") {
            Given("table with 2 players with bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).generalBet(100),
                                Player.playing(1).generalBet(200)
                        )
            }

            Then("pot amount is calculated") {
                assertThat(table.potAmount()).isEqualTo(300)
            }
        }

        Scenario("players have no bets so pot amount is 0") {
            Given("table with 2 players with no bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
            }

            Then("highest best is calculated") {
                assertThat(table.emptyPot()).isTrue()
            }
        }

        Scenario("there are no players so pot amount is 0") {
            Given("table with no players") {
                table = PokerTable.sample()
            }

            Then("highest best is calculated") {
                assertThat(table.emptyPot()).isTrue()
            }
        }
    }

    Feature("calculating pot amount this phase") {
        lateinit var table: PokerTable

        Scenario("pot this phase is sum of bet amounts from this phase") {
            Given("table with 2 players with bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).generalBet(100).betThisPhase(500),
                                Player.playing(1).generalBet(200).betThisPhase(800)
                        )
            }

            Then("pot amount this phase is calculated") {
                assertThat(table.potAmountThisPhase()).isEqualTo(1300)
            }
        }

        Scenario("players have no bets so pot amount this phase is 0") {
            Given("table with 2 players with no bets this phase") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).generalBet(100),
                                Player.playing(1).generalBet(200)
                        )
            }

            Then("pot amount this phase is calculated") {
                assertThat(table.emptyPotThisPhase()).isTrue()
            }
        }

        Scenario("there are no players so pot amount is 0") {
            Given("table with no players") {
                table = PokerTable.sample()
            }

            Then("pot amount this phase is calculated") {
                assertThat(table.emptyPotThisPhase()).isTrue()
            }
        }
    }

    Feature("check if there are enough players with chips to continue a game") {
        lateinit var table: PokerTable

        Scenario("there are 3 players with chips") {
            Given("table with 3 playing players with chips") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1000),
                                Player.playing(1).chips(500),
                                Player.playing(2).chips(500)
                        )
            }

            Then("there are enough players with chips") {
                assertThat(table.notEnoughPlayersWithChips()).isFalse()
            }
        }

        Scenario("there are 3 players with chips, but one is not in game") {
            Given("table with 2 playing players with chips") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1000),
                                Player.playing(1).chips(500),
                                Player.sitting(2).chips(500)
                        )
            }

            Then("there are enough players with chips") {
                assertThat(table.notEnoughPlayersWithChips()).isFalse()
            }
        }

        Scenario("there are 3 players with chips, but only one is in game") {
            Given("table with 1 playing player with chips") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1000),
                                Player.sitting(1).chips(500),
                                Player.sitting(2).chips(500)
                        )
            }

            Then("there are NOT enough players with chips") {
                assertThat(table.notEnoughPlayersWithChips()).isTrue()
            }
        }

        Scenario("there are 3 playing players, but only one has chips") {
            Given("table with 3 playing players, 2 without chips") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).chips(1000),
                                Player.playing(1).chips(0),
                                Player.playing(2).chips(0)
                        )
            }

            Then("there are NOT enough players with chips") {
                assertThat(table.notEnoughPlayersWithChips()).isTrue()
            }
        }
    }

    Feature("check if all players moved") {
        lateinit var table: PokerTable

        Scenario("there are 3 players that moved already") {
            Given("table with 3 playing players and their moves") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.playing(2)
                        )
                        .moves(mapOf(0 to Moves.CALL, 1 to Moves.CHECK, 2 to Moves.CALL))
            }

            Then("all players have already moved") {
                assertThat(table.allPlayersMoved()).isTrue()
            }
        }

        Scenario("there are 3 players and 2 of them have moved, but one of those is not in game") {
            Given("table with 2 playing players and 1 sitting player with their moves") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.sitting(2)
                        )
                        .moves(mapOf(0 to Moves.CALL, 2 to Moves.CHECK))
            }

            Then("not all players have already moved") {
                assertThat(table.allPlayersMoved()).isFalse()
            }
        }

        Scenario("there are 2 playing players, but only 1 has moved") {
            Given("table with 2 playing players and their moves") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .moves(mapOf(1 to Moves.CHECK))
            }

            Then("not all players have already moved") {
                assertThat(table.allPlayersMoved()).isFalse()
            }
        }
    }

    Feature("check if bets are equal to highest bet this phase") {
        lateinit var table: PokerTable

        Scenario("there are 3 players with equal bets") {
            Given("table with 3 playing players and their bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.playing(2)
                        )
                        .everyoneHasTheSameBet()
            }

            Then("all players have equal bets") {
                assertThat(table.allBetsEqualToHighestBet()).isTrue()
            }
        }

        Scenario("there are 3 players with different bets") {
            Given("table with 3 playing players and their bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.playing(2)
                        )
                        .notEveryoneHasTheSameBet()
            }

            Then("all players dont have equal bets") {
                assertThat(table.allBetsEqualToHighestBet()).isFalse()
            }
        }

        Scenario("there are 2 equal bets, and 3 different, but they are folded/all in/sitting") {
            Given("table with 2 playing, 1 folded 1 sitting 1 allin players and their bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(300),
                                Player.playing(1).betThisPhase(300),
                                Player.sitting(2).betThisPhase(200),
                                Player.folded(3).betThisPhase(100),
                                Player.allIn(4).betThisPhase(50)
                        )
            }

            Then("all players have equal bets") {
                assertThat(table.allBetsEqualToHighestBet()).isTrue()
            }
        }

        Scenario("bets of playing players are equal, but not to the highest bet, that one belongs to the allin player") {
            Given("table with 2 playing, 1 folded 1 sitting 1 allin players and their bets") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(300),
                                Player.playing(1).betThisPhase(300),
                                Player.sitting(2).betThisPhase(200),
                                Player.folded(3).betThisPhase(400),
                                Player.allIn(4).betThisPhase(500)
                        )
            }

            Then("all players dont have equal bets") {
                assertThat(table.allBetsEqualToHighestBet()).isFalse()
            }
        }

    }
})
