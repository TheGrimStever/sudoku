package com.ai.baca.sudoku

import com.ai.baca.domain.CellPosition
import com.ai.baca.game.SudokuGame
import com.ai.baca.sample.SamplePuzzles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SudokuGameTest {
    @Test
    fun enteredDigitIsStoredInSelectedEmptyCell() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.enterDigit(7)

        assertEquals(7, game.snapshot().entries[position.row, position.column])
    }

    @Test
    fun givenCellCannotBeChanged() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstGivenCell(game)

        game.selectCell(position)
        game.enterDigit(7)

        assertEquals(0, game.snapshot().entries[position.row, position.column])
    }

    @Test
    fun duplicateHistoryPersistsUntilConflictIsCleared() {
        val game = SudokuGame(SamplePuzzles.easy)

        game.selectCell(CellPosition(0, 2))
        game.enterDigit(5)
        game.selectCell(CellPosition(1, 1))

        assertEquals(
            setOf(CellPosition(0, 0), CellPosition(0, 2)),
            game.snapshot().conflictingCells,
        )

        game.clearSelectedCell(CellPosition(0, 2))

        assertTrue(game.snapshot().conflictingCells.isEmpty())
    }

    private fun firstEmptyCell(game: SudokuGame): CellPosition =
        firstCellMatching(game) { it == 0 }

    private fun firstGivenCell(game: SudokuGame): CellPosition =
        firstCellMatching(game) { it != 0 }

    private fun firstCellMatching(game: SudokuGame, predicate: (Int) -> Boolean): CellPosition {
        val givens = game.snapshot().givens
        for (row in 0..8) for (column in 0..8) {
            if (predicate(givens[row, column])) return CellPosition(row, column)
        }
        error("No matching cell")
    }
}
