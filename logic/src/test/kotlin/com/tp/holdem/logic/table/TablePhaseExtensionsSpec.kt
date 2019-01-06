package com.tp.holdem.logic.table

import com.tp.holdem.common.model.Moves
import com.tp.holdem.common.model.Phase
import com.tp.holdem.logic.*
import com.tp.holdem.logic.model.PhaseStatus
import com.tp.holdem.logic.model.Player
import com.tp.holdem.logic.model.PlayerNumber
import com.tp.holdem.logic.model.PokerTable
import com.tp.holdem.logic.players.availableChips
import io.vavr.Tuple
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TablePhaseExtensionsSpec : Spek({
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

            Then("players bet parameters are set") {
                assertThat(table.findPlayer(1).isDuringBettingTurn()).isTrue()
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

            Then("players bet parameters are set") {
                assertThat(table.findPlayer(0).isDuringBettingTurn()).isTrue()
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

            Then("players bet parameters are set") {
                assertThat(table.findPlayer(2).isDuringBettingTurn()).isTrue()
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

            Then("players bet parameters are set") {
                assertThat(table.findPlayer(3).isDuringBettingTurn()).isTrue()
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

            Then("players bet parameters are set") {
                assertThat(table.findPlayer(1).isDuringBettingTurn()).isTrue()
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

        Scenario("player is currently sitting so exception is thrown") {
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
                assertThat(table.findPlayer(1).isNotInBettingTurn()).isTrue()
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
                assertThat(table.findPlayer(1).isNotInBettingTurn()).isTrue()
            }

            Then("player is folded") {
                assertThat(table.findPlayer(1).folded).isTrue()
            }
        }

        Scenario("player performs CALL move") {
            val amountToCall = 1000
            val playerChips = 1500

            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(amountToCall),
                                Player.playing(1).chips(playerChips)
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
                assertThat(table.findPlayer(1).isNotInBettingTurn()).isTrue()
            }

            Then("player available chips amount is reduced") {
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(playerChips - amountToCall)
            }
        }

        Scenario("player performs CALL move, that he does not have enough chips for, so exception is thrown") {
            val amountToCall = 2000
            val playerChips = 1500

            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(amountToCall),
                                Player.playing(1).chips(playerChips)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.CALL)
                        )
            }

            When("CALL move is performed") {
                assertThatThrownBy { table.playerMove(1, Moves.CALL) }
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
                assertThat(table.findPlayer(1).isNotInBettingTurn()).isTrue()
            }

            Then("player available chips amount is reduced") {
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(0)
            }

            Then("player is all in") {
                assertThat(table.findPlayer(1).allIn).isTrue()
            }
        }

        Scenario("player performs BET move") {
            val amountToBet = 600
            val playerChips = 1500

            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1).chips(playerChips)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.BET)
                        )
            }

            When("BET move is performed") {
                table = table.playerMove(1, Moves.BET, amountToBet)
            }

            Then("player move is registered as BET") {
                assertThat(table.lastBetOf(1)).isEqualTo(Moves.BET)
            }

            Then("player betting turn parameters are reset") {
                assertThat(table.findPlayer(1).isNotInBettingTurn()).isTrue()
            }

            Then("player available chips amount is reduced") {
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(playerChips - amountToBet)
            }
        }

        Scenario("player performs BET move that he does not have chips for, so exception is thrown") {
            val playerChips = 1500
            val amountToBet = 2000

            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0),
                                Player.playing(1).chips(playerChips)
                        )
                        .bettingPlayer(
                                number = 1,
                                moves = *arrayOf(Moves.BET)
                        )
            }

            When("BET move is performed, but player does not have enough chips so exception is thrown") {
                assertThatThrownBy { table.playerMove(1, Moves.BET, amountToBet) }
            }
        }

        Scenario("player performs RAISE move") {
            val playerChips = 1500
            val amountToCall = 1000
            val amountToRaise = 300

            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(amountToCall),
                                Player.playing(1).chips(playerChips)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.RAISE)
                        )
            }

            When("RAISE move is performed") {
                table = table.playerMove(1, Moves.RAISE, amountToRaise)
            }

            Then("player move is registered as RAISE") {
                assertThat(table.lastBetOf(1)).isEqualTo(Moves.RAISE)
            }

            Then("player betting turn parameters are reset") {
                assertThat(table.findPlayer(1).isNotInBettingTurn()).isTrue()
            }

            Then("player available chips amount is reduced") {
                assertThat(table.findPlayer(1).availableChips()).isEqualTo(playerChips - amountToCall - amountToRaise)
            }
        }

        Scenario("player performs RAISE move that he does not have chips for, so exception is thrown") {
            val playerChips = 1500
            val amountToCall = 1000
            val amountToRaise = 800

            Given("table with 2 playing players") {
                table = PokerTable.sample()
                        .players(
                                Player.playing(0).betThisPhase(amountToCall),
                                Player.playing(1).chips(playerChips)
                        )
                        .bettingPlayer(
                                number = 1,
                                maximumBet = 100,
                                minimumBet = 50,
                                moves = *arrayOf(Moves.RAISE)
                        )
            }

            When("RAISE move is performed, but player does not have enough chips, so exception is thrown") {
                assertThatThrownBy { table.playerMove(1, Moves.RAISE, amountToRaise) }
            }
        }
    }

    Feature("going to next phase") {
        lateinit var table: PokerTable

        Scenario("table is in RIVER phase, so since it's the last playing phase - exception is thrown") {
            Given("table in RIVER phase") {
                table = PokerTable.inPhase(Phase.RIVER)
            }

            When("going to next phase throws exception since RIVER is the last playing phase") {
                assertThatThrownBy { table.goToNextPlayingPhase() }
            }
        }

        Scenario("table is in OVER phase, so exception is thrown") {
            Given("table in OVER phase") {
                table = PokerTable.inPhase(Phase.RIVER)
            }

            When("going to next phase throws exception since there is no next phase") {
                assertThatThrownBy { table.goToNextPlayingPhase() }
            }
        }

        Scenario("table goes from START to PRE_FLOP phase") {
            Given("table in START phase, with 2 playing players") {
                table = PokerTable.inPhase(Phase.START)
                        .players(
                                Player.playing(0),
                                Player.playing(1)
                        )
                        .dealerPlayer(0)
            }

            When("going to next phase - PRE_FLOP") {
                table = table.goToNextPlayingPhase()
            }

            Then("current phase is PRE_FLOP") {
                assertThat(table.currentPhase()).isEqualTo(Phase.PRE_FLOP)
            }

            Then("dealer player is betting") {
                assertThat(table.isBetting(0)).isTrue()
            }

            Then("betting player has betting parameters set") {
                assertThat(table.findPlayer(0).isDuringBettingTurn())
            }
        }

        Scenario("table goes from PRE_FLOP to FLOP phase") {
            val maxBetPreviousPhase = 500
            val chipsAmount = mapOf(0 to 500, 1 to 1500, 2 to 1500, 3 to 1500)

            Given("table in PRE_FLOP phase, with 4 players and leftover moves from previous phase") {
                table = PokerTable.inPhase(Phase.PRE_FLOP)
                        .players(
                                Player.allIn(0).chips(chipsAmount[0]!!).betThisPhase(maxBetPreviousPhase),
                                Player.playing(1).chips(chipsAmount[1]!!).betThisPhase(maxBetPreviousPhase),
                                Player.folded(2).chips(chipsAmount[2]!!).betThisPhase(maxBetPreviousPhase),
                                Player.playing(3).chips(chipsAmount[3]!!).betThisPhase(maxBetPreviousPhase)
                        )
                        .bigBlindPlayer(0)
                        .moves(mapOf(0 to Moves.ALLIN, 1 to Moves.CALL, 2 to Moves.FOLD, 3 to Moves.CALL))
            }

            When("going to next phase - FLOP") {
                table = table.goToNextPlayingPhase()
            }

            Then("current phase is FLOP") {
                assertThat(table.currentPhase()).isEqualTo(Phase.FLOP)
            }

            Then("player to the left of big blind player is betting") {
                assertThat(table.isBetting(1)).isTrue()
            }

            Then("betting player has betting parameters set") {
                assertThat(table.findPlayer(1).isDuringBettingTurn())
            }

            Then("deck is 52 - 3 size") {
                assertThat(table.cardsInDeck()).isEqualTo(52 - 3)
            }

            Then("there are 3 cards on table") {
                assertThat(table.cardsOnTable()).isEqualTo(3)
            }

            Then("players (except betting player) are prepared for new phase") {
                table.allPlayers
                        .filter { table.isBetting(it).not() }
                        .forEach { player ->
                            assertThat(player.isNotInBettingTurn()).isTrue()
                            assertThat(player.betAmountThisPhase).isEqualTo(0)
                            assertThat(player.betAmount).isEqualTo(maxBetPreviousPhase)
                            assertThat(player.chipsAmount).isEqualTo(chipsAmount[player.number.number]!! - maxBetPreviousPhase)
                        }
            }

            Then("FOLD and ALLIN moves from previous phase are passed to new phase") {
                assertThat(table.latestMoves).containsAll(
                        listOf(
                                Tuple.of(PlayerNumber.of(0), Moves.ALLIN),
                                Tuple.of(PlayerNumber.of(2), Moves.FOLD)
                        )
                )
            }
        }

        Scenario("table goes from FLOP to TURN phase") {
            val maxBetPreviousPhase = 500
            val chipsAmount = mapOf(0 to 500, 1 to 1500, 2 to 1500, 3 to 1500)

            Given("table in FLOP phase, with 4 players and leftover moves from previous phase") {
                table = PokerTable.inPhase(Phase.FLOP)
                        .players(
                                Player.allIn(0).chips(chipsAmount[0]!!).betThisPhase(maxBetPreviousPhase),
                                Player.playing(1).chips(chipsAmount[1]!!).betThisPhase(maxBetPreviousPhase),
                                Player.folded(2).chips(chipsAmount[2]!!).betThisPhase(maxBetPreviousPhase),
                                Player.playing(3).chips(chipsAmount[3]!!).betThisPhase(maxBetPreviousPhase)
                        )
                        .bigBlindPlayer(0)
                        .cardsOnTable(3)
                        .moves(mapOf(0 to Moves.ALLIN, 1 to Moves.CALL, 2 to Moves.FOLD, 3 to Moves.CALL))
            }

            When("going to next phase - TURN") {
                table = table.goToNextPlayingPhase()
            }

            Then("current phase is TURN") {
                assertThat(table.currentPhase()).isEqualTo(Phase.TURN)
            }

            Then("player to the left of big blind player is betting") {
                assertThat(table.isBetting(1)).isTrue()
            }

            Then("betting player has betting parameters set") {
                assertThat(table.findPlayer(1).isDuringBettingTurn())
            }

            Then("deck is 52 - 3 - 1 size") {
                assertThat(table.cardsInDeck()).isEqualTo(52 - 3 - 1)
            }

            Then("there are 3 cards on table") {
                assertThat(table.cardsOnTable()).isEqualTo(4)
            }

            Then("players (except betting player) are prepared for new phase") {
                table.allPlayers
                        .filter { table.isBetting(it).not() }
                        .forEach { player ->
                            assertThat(player.isNotInBettingTurn()).isTrue()
                            assertThat(player.betAmountThisPhase).isEqualTo(0)
                            assertThat(player.betAmount).isEqualTo(maxBetPreviousPhase)
                            assertThat(player.chipsAmount).isEqualTo(chipsAmount[player.number.number]!! - maxBetPreviousPhase)
                        }
            }

            Then("FOLD and ALLIN moves from previous phase are passed to new phase") {
                assertThat(table.latestMoves).containsAll(
                        listOf(
                                Tuple.of(PlayerNumber.of(0), Moves.ALLIN),
                                Tuple.of(PlayerNumber.of(2), Moves.FOLD)
                        )
                )
            }
        }

        Scenario("table goes from TURN to RIVER phase") {
            val maxBetPreviousPhase = 500
            val chipsAmount = mapOf(0 to 500, 1 to 1500, 2 to 1500, 3 to 1500)

            Given("table in TURN phase, with 4 players and leftover moves from previous phase") {
                table = PokerTable.inPhase(Phase.TURN)
                        .players(
                                Player.allIn(0).chips(chipsAmount[0]!!).betThisPhase(maxBetPreviousPhase),
                                Player.playing(1).chips(chipsAmount[1]!!).betThisPhase(maxBetPreviousPhase),
                                Player.folded(2).chips(chipsAmount[2]!!).betThisPhase(maxBetPreviousPhase),
                                Player.playing(3).chips(chipsAmount[3]!!).betThisPhase(maxBetPreviousPhase)
                        )
                        .bigBlindPlayer(0)
                        .cardsOnTable(4)
                        .moves(mapOf(0 to Moves.ALLIN, 1 to Moves.CALL, 2 to Moves.FOLD, 3 to Moves.CALL))
            }

            When("going to next phase - RIVER") {
                table = table.goToNextPlayingPhase()
            }

            Then("current phase is RIVER") {
                assertThat(table.currentPhase()).isEqualTo(Phase.RIVER)
            }

            Then("player to the left of big blind player is betting") {
                assertThat(table.isBetting(1)).isTrue()
            }

            Then("betting player has betting parameters set") {
                assertThat(table.findPlayer(1).isDuringBettingTurn())
            }

            Then("deck is 52 - 3 - 1 - 1 size") {
                assertThat(table.cardsInDeck()).isEqualTo(52 - 3 - 1 - 1)
            }

            Then("there are 3 cards on table") {
                assertThat(table.cardsOnTable()).isEqualTo(5)
            }

            Then("players (except betting player) are prepared for new phase") {
                table.allPlayers
                        .filter { table.isBetting(it).not() }
                        .forEach { player ->
                            assertThat(player.isNotInBettingTurn()).isTrue()
                            assertThat(player.betAmountThisPhase).isEqualTo(0)
                            assertThat(player.betAmount).isEqualTo(maxBetPreviousPhase)
                            assertThat(player.chipsAmount).isEqualTo(chipsAmount[player.number.number]!! - maxBetPreviousPhase)
                        }
            }

            Then("FOLD and ALLIN moves from previous phase are passed to new phase") {
                assertThat(table.latestMoves).containsAll(
                        listOf(
                                Tuple.of(PlayerNumber.of(0), Moves.ALLIN),
                                Tuple.of(PlayerNumber.of(2), Moves.FOLD)
                        )
                )
            }
        }
    }
})
