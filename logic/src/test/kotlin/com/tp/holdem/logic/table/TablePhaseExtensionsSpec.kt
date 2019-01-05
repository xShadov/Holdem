package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.*
import com.tp.holdem.logic.players.availableChips
import com.tp.holdem.logic.model.PhaseStatus
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PokerTable
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TablePhaseExtensionsSpec : Spek({
    //TODO check is betInPhase was called
    Feature("picking next player to bet") {
        lateinit var table: PokerTable

        Scenario("player to the left is picked") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .bettingPlayer(0)
            }

            When("find next player to bet") {
                table = table.nextPlayerToBet()
            }

            Then("player with number 1 has betting turn") {
                assertThat(table.isBetting(1)).isTrue()
            }
        }

        Scenario("player to the right is picked") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .bettingPlayer(1)
            }

            When("find next player to bet") {
                table = table.nextPlayerToBet()
            }

            Then("player with number 0 has betting turn") {
                assertThat(table.isBetting(0)).isTrue()
            }
        }

        Scenario("player on the left is skipped (he's folded) and next player is picked") {
            Given("table with 2 playing and 1 folded players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.folded(1),
                                Player.playing(2)
                        )
                        .bettingPlayer(0)
            }

            When("find next player to bet") {
                table = table.nextPlayerToBet()
            }

            Then("player with number 2 has betting turn") {
                assertThat(table.isBetting(2)).isTrue()
            }
        }

        Scenario("2 players on the left are skipped (folded and not in game) and next player is picked") {
            Given("table with 2 playing, 1 folded and 1 sitting player") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.folded(1),
                                Player.sitting(2),
                                Player.playing(3)
                        )
                        .bettingPlayer(0)
            }

            When("find next player to bet") {
                table = table.nextPlayerToBet()
            }

            Then("player with number 3 has betting turn") {
                assertThat(table.isBetting(3)).isTrue()
            }
        }

        Scenario("2 players on the left and 1 player in new cycle are skipped (folded)") {
            Given("table with 2 playing and 3 folded players") {
                table = PokerTable.sample()
                        .players(
                                Player.folded(0),
                                Player.playing(1),
                                Player.playing(2),
                                Player.folded(3),
                                Player.folded(3)
                        )
                        .bettingPlayer(2)
            }

            When("find next player to bet") {
                table = table.nextPlayerToBet()
            }

            Then("player with number 1 has betting turn") {
                assertThat(table.isBetting(1)).isTrue()
            }
        }

        Scenario("table has no current bettingPlayer player, meaning it's in illegal state") {
            Given("table without betting player") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
            }

            Then("table is in illegal state so exception is thrown") {
                assertThatThrownBy { table.nextPlayerToBet() }
            }
        }

        Scenario("table has only one player fit to bet, meaning it's in illegal state") {
            Given("table without betting player") {
                table = PokerTable.sample()
                        .players(
                                Player.folded(0),
                                Player.folded(1),
                                Player.playing(2),
                                Player.sitting(3)
                        )
                        .bettingPlayer(2)
            }

            Then("table is in illegal state so exception is thrown") {
                assertThatThrownBy { table.nextPlayerToBet() }
            }
        }
    }

    Feature("start showdown mode") {
        lateinit var table: PokerTable

        Scenario("table goes from not-showdown mode to showdown mode") {
            Given("table in not-showdown mode") {
                table = PokerTable.sample()
            }

            When("start showdown") {
                table = table.startShowdown()
            }

            Then("table is in showdown mode") {
                assertThat(table.showdown).isTrue()
            }
        }
    }

    Feature("find phase status") {
        lateinit var table: PokerTable

        Scenario("ROUND_OVER status is found since only a single player is not folded") {
            Given("table with one not-folded player") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.folded(0),
                                Player.playing(1),
                                Player.folded(2)
                        )
            }

            Then("ROUND_OVER status is returned") {
                assertThat(table.phaseStatus()).isEqualTo(PhaseStatus.ROUND_OVER)
            }
        }

        Scenario("table has less than 2 players in game, meaning it's in illegal state") {
            Given("table with only one playing player") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.sitting(0),
                                Player.playing(1),
                                Player.sitting(2)
                        )
            }

            Then("table is in illegal state so exception is thrown") {
                assertThatThrownBy { table.phaseStatus() }
            }
        }

        Scenario("table has only folded, meaning it's in illegal state") {
            Given("table with only folded players") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.folded(0),
                                Player.folded(1),
                                Player.folded(2)
                        )
            }

            Then("table is in illegal state so exception is thrown") {
                assertThatThrownBy { table.phaseStatus() }
            }
        }

        Scenario("everyone has moved in this phase and everyone is all in, so table is ready for showdown") {
            Given("table with only all-in players that moved this phase") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.allIn(0),
                                Player.allIn(1),
                                Player.allIn(2)
                        )
                        .everyoneMovedThisPhase()
            }

            Then("READY_FOR_SHOWDOWN status is returned") {
                assertThat(table.phaseStatus()).isEqualTo(PhaseStatus.READY_FOR_SHOWDOWN)
            }
        }

        Scenario("everyone has moved in this phase and everyone (except a single player) is all in, so table is ready for showdown") {
            Given("table with only all-in players that moved this phase") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.allIn(0),
                                Player.playing(1),
                                Player.allIn(2)
                        )
                        .everyoneMovedThisPhase()
            }

            Then("READY_FOR_SHOWDOWN status is returned") {
                assertThat(table.phaseStatus()).isEqualTo(PhaseStatus.READY_FOR_SHOWDOWN)
            }
        }

        Scenario("everyone has moved in this phase and bets are equal, so table is ready for next phase") {
            Given("table with players that moved this phase and equal bets") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.playing(2)
                        )
                        .everyoneMovedThisPhase()
                        .everyoneHasTheSameBet()
            }

            Then("READY_FOR_NEXT status is returned") {
                assertThat(table.phaseStatus()).isEqualTo(PhaseStatus.READY_FOR_NEXT)
            }
        }

        Scenario("table is in non-playing phase, meaning it's in illegal state") {
            Given("table in START phase") {
                table = PokerTable.inPhase(Phase.START)
            }

            Then("table is in illegal state so exception is thrown") {
                assertThatThrownBy { table.phaseStatus() }
            }
        }

        Scenario("everyone has moved in this phase and bets are equal, but table is in last playing phase so round is over") {
            Given("table in last playing phase, with equal bets and players that already moved this phase") {
                table = PokerTable
                        .inPhase(Phase.lastPlayingPhase())
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.playing(2)
                        )
                        .everyoneMovedThisPhase()
                        .everyoneHasTheSameBet()
            }

            Then("ROUND_OVER status is returned") {
                assertThat(table.phaseStatus()).isEqualTo(PhaseStatus.ROUND_OVER)
            }
        }

        Scenario("not everyone has moved yet, so phase is keep going") {
            Given("table with some players that have not moved yet this phase") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.playing(2)
                        )
                        .playersMovedThisPhase(0, 1)
            }

            Then("KEEP_GOING status is returned") {
                assertThat(table.phaseStatus()).isEqualTo(PhaseStatus.KEEP_GOING)
            }
        }

        Scenario("everyone has moved, but bets are not equal, so phase is keep going") {
            Given("table with players that have all moved this phase, but their bets are not equal") {
                table = PokerTable
                        .inPhase(Phase.FLOP)
                        .players(
                                Player.playing(0),
                                Player.playing(1),
                                Player.playing(2)
                        )
                        .everyoneMovedThisPhase()
                        .notEveryoneHasTheSameBet()
            }

            Then("KEEP_GOING status is returned") {
                assertThat(table.phaseStatus()).isEqualTo(PhaseStatus.KEEP_GOING)
            }
        }
    }

    Feature("handling player move") {
        lateinit var table: PokerTable

        Scenario("player with such number is not found") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
            }

            Then("player is not found so exception is thrown") {
                assertThatThrownBy { table.playerMove(2, Moves.CALL) }
            }
        }

        Scenario("player is currently folded so exception is thrown") {
            Given("table with 1 folded and 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.folded(1),
                                Player.playing(2)
                        )
                        .bettingPlayer(1)
            }

            Then("player that moved is folded so exception is thrown") {
                assertThatThrownBy { table.playerMove(1, Moves.CALL) }
            }
        }

        Scenario("player is currently folded so exception is thrown") {
            Given("table with 1 sitting and 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.sitting(1),
                                Player.playing(2)
                        )
                        .bettingPlayer(1)
            }

            Then("player that moved is sitting so exception is thrown") {
                assertThatThrownBy { table.playerMove(1, Moves.CALL) }
            }
        }

        Scenario("different player has betting turn so exception is thrown") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .bettingPlayer(0)
            }

            Then("betting turn has player number 0 so exception is thrown") {
                assertThatThrownBy { table.playerMove(1, Moves.CALL) }
            }
        }

        Scenario("player tried to perform invalid move so exception is thrown") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .bettingPlayer(
                                number = 1,
                                moves = *arrayOf(Moves.CHECK, Moves.BET)
                        )
            }

            Then("CALL move is performed and it's not in players possible moves so exception is thrown") {
                assertThatThrownBy { table.playerMove(1, Moves.CALL) }
            }
        }

        Scenario("player performs CHECK move") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.CHECK)
                        )
            }

            When("CHECK move is performed") {
                table = table.playerMove(1, Moves.CHECK)
            }

            Then("player move is registered as CHECK") {
                assertThat(table.lastBetOf(1)).isEqualTo(Moves.CHECK)
            }

            Then("player betting turn parameters are reset") {
                assertThat(table.findPlayer(1).isAfterBettingTurn()).isTrue()
            }
        }

        Scenario("player performs FOLD move") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.FOLD)
                        )
            }

            When("FOLD move is performed") {
                table = table.playerMove(1, Moves.FOLD)
            }

            Then("player move is registered as FOLD") {
                assertThat(table.lastBetOf(1)).isEqualTo(Moves.FOLD)
            }

            Then("player betting turn parameters are reset") {
                assertThat(table.findPlayer(1).isAfterBettingTurn()).isTrue()
            }

            Then("player is folded") {
                assertThat(table.findPlayer(1).folded).isTrue()
            }
        }

        Scenario("player performs CALL move") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(1000),
                                Player.playing(1).chips(1500)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.CALL)
                        )
            }

            When("CALL move is performed") {
                table = table.playerMove(1, Moves.CALL)
            }

            Then("player move is registered as CALL") {
                assertThat(table.lastBetOf(1)).isEqualTo(Moves.CALL)
            }

            Then("player betting turn parameters are reset") {
                assertThat(table.findPlayer(1).isAfterBettingTurn()).isTrue()
            }

            Then("player available chips amount is reduced") {
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(500)
            }
        }

        Scenario("player performs ALLIN move") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1).chips(1500)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.ALLIN)
                        )
            }

            When("ALLIN move is performed") {
                table = table.playerMove(1, Moves.ALLIN)
            }

            Then("player move is registered as ALLIN") {
                assertThat(table.lastBetOf(1)).isEqualTo(Moves.ALLIN)
            }

            Then("player betting turn parameters are reset") {
                assertThat(table.findPlayer(1).isAfterBettingTurn()).isTrue()
            }

            Then("player available chips amount is reduced") {
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(0)
            }

            Then("player is all in") {
                assertThat(table.findPlayer(1).allIn).isTrue()
            }
        }

        Scenario("player performs BET move") {
            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1).chips(1500)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.BET)
                        )
            }

            When("BET move is performed") {
                table = table.playerMove(1, Moves.BET)
            }

            Then("player move is registered as BET") {
                assertThat(table.lastBetOf(1)).isEqualTo(Moves.BET)
            }

            Then("player betting turn parameters are reset") {
                assertThat(table.findPlayer(1).isAfterBettingTurn()).isTrue()
            }

            Then("player available chips amount is reduced") {
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(0)
            }

            Then("player is all in") {
                assertThat(table.findPlayer(1).allIn).isTrue()
            }
        }
    }
})
