package com.ai.baca.sudoku

import com.ai.baca.domain.CellPosition
import com.ai.baca.game.SudokuGame
import com.ai.baca.sample.SamplePuzzles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

        game.selectCell(CellPosition(0, 2))
        game.clearSelectedCell()

        assertTrue(game.snapshot().conflictingCells.isEmpty())
    }

    @Test
    fun noteModeTogglesPencilMarksInSelectedEmptyCell() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.toggleNoteMode()
        game.enterDigit(2)
        game.enterDigit(8)

        assertTrue(game.snapshot().isNoteMode)
        assertEquals(setOf(2, 8), game.snapshot().pencilMarks.digitsAt(position))
        assertEquals(0, game.snapshot().entries[position.row, position.column])

        game.enterDigit(2)

        assertEquals(setOf(8), game.snapshot().pencilMarks.digitsAt(position))
    }

    @Test
    fun noteModeCannotChangeGivenOrEnteredCells() {
        val game = SudokuGame(SamplePuzzles.easy)
        val givenPosition = firstGivenCell(game)
        val entryPosition = firstEmptyCell(game)

        game.toggleNoteMode()
        game.selectCell(givenPosition)
        game.enterDigit(3)

        assertTrue(game.snapshot().pencilMarks.digitsAt(givenPosition).isEmpty())
        assertFalse(game.snapshot().canUndo)

        game.toggleNoteMode()
        game.selectCell(entryPosition)
        game.enterDigit(5)
        game.toggleNoteMode()
        game.enterDigit(7)

        assertTrue(game.snapshot().pencilMarks.digitsAt(entryPosition).isEmpty())
        assertEquals(5, game.snapshot().entries[entryPosition.row, entryPosition.column])
    }

    @Test
    fun committedEntryClearsPencilMarksInThatCell() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.toggleNoteMode()
        game.enterDigit(1)
        game.enterDigit(9)
        game.toggleNoteMode()
        game.enterDigit(6)

        assertEquals(6, game.snapshot().entries[position.row, position.column])
        assertTrue(game.snapshot().pencilMarks.digitsAt(position).isEmpty())
    }

    @Test
    fun pencilMarksDoNotCreateConflicts() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = CellPosition(0, 2)

        game.selectCell(position)
        game.toggleNoteMode()
        game.enterDigit(5)

        assertTrue(game.snapshot().conflictingCells.isEmpty())
    }

    @Test
    fun clearRemovesEntryAndPencilMarksFromSelectedCell() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.toggleNoteMode()
        game.enterDigit(4)
        game.clearSelectedCell()

        assertEquals(0, game.snapshot().entries[position.row, position.column])
        assertTrue(game.snapshot().pencilMarks.digitsAt(position).isEmpty())
    }

    @Test
    fun clearDoesNothingWithoutSelectionOrOnGivenCell() {
        val game = SudokuGame(SamplePuzzles.easy)

        game.clearSelectedCell()
        assertFalse(game.snapshot().canUndo)

        game.selectCell(firstGivenCell(game))
        game.clearSelectedCell()
        assertFalse(game.snapshot().canUndo)
    }

    @Test
    fun undoRestoresEntriesAndPencilMarksInReverseOrder() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.toggleNoteMode()
        game.enterDigit(3)
        game.enterDigit(7)
        game.toggleNoteMode()
        game.enterDigit(9)

        game.undo()
        assertEquals(0, game.snapshot().entries[position.row, position.column])
        assertEquals(setOf(3, 7), game.snapshot().pencilMarks.digitsAt(position))

        game.undo()
        assertEquals(setOf(3), game.snapshot().pencilMarks.digitsAt(position))

        game.undo()
        assertTrue(game.snapshot().pencilMarks.digitsAt(position).isEmpty())
        assertFalse(game.snapshot().canUndo)
    }

    @Test
    fun undoRestoresClearedCell() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.enterDigit(8)
        game.clearSelectedCell()
        game.undo()

        assertEquals(8, game.snapshot().entries[position.row, position.column])
    }

    @Test
    fun undoRestoresReplacedDigit() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.enterDigit(2)
        game.enterDigit(6)
        game.undo()

        assertEquals(2, game.snapshot().entries[position.row, position.column])
    }

    @Test
    fun undoWithEmptyHistoryDoesNothing() {
        val game = SudokuGame(SamplePuzzles.easy)

        game.undo()

        assertFalse(game.snapshot().canUndo)
        assertTrue(game.snapshot().pencilMarks.digitsAt(firstEmptyCell(game)).isEmpty())
    }

    @Test
    fun selectionModeChangesAndNoOpEntriesAreNotRecordedInHistory() {
        val game = SudokuGame(SamplePuzzles.easy)
        val position = firstEmptyCell(game)

        game.selectCell(position)
        game.toggleNoteMode()
        game.toggleNoteMode()
        assertFalse(game.snapshot().canUndo)

        game.enterDigit(4)
        game.enterDigit(4)
        game.undo()

        assertEquals(0, game.snapshot().entries[position.row, position.column])
        assertFalse(game.snapshot().canUndo)
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
