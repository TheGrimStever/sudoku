package com.ai.baca.game

import com.ai.baca.domain.Board
import com.ai.baca.domain.BoardValidator
import com.ai.baca.domain.CellPosition
import com.ai.baca.domain.Puzzle

class SudokuGame(
    private val puzzle: Puzzle,
    private val boardValidator: BoardValidator = BoardValidator(),
) {

    private var entries = Board.empty()
    private var pencilMarks = PencilMarks.empty()
    private var selectedCell: CellPosition? = null
    private var isNoteMode = false
    private var incorrectCells: Set<CellPosition> = emptySet()
    private val history = mutableListOf<PlayerState>()

    fun snapshot(): GameSnapshot {
        val board = currentBoard()
        val conflictingCells = boardValidator.findConflictingCells(board)
        return GameSnapshot(
            givens = puzzle.givens,
            entries = entries,
            pencilMarks = pencilMarks,
            selectedCell = selectedCell,
            conflictingCells = conflictingCells,
            incorrectCells = incorrectCells,
            completedDigits = completedDigits(board, conflictingCells),
            isNoteMode = isNoteMode,
            canUndo = history.isNotEmpty(),
        )
    }

    /**
     * Compares every player-entered digit against the puzzle's solution and
     * records which ones are wrong. This is purely opt-in: nothing is
     * checked against the solution unless the player explicitly calls this,
     * so a player who wants a traditional, no-hints experience never
     * triggers it.
     */
    fun checkForErrors(): Set<CellPosition> {
        incorrectCells = (0..8).flatMap { row ->
            (0..8).mapNotNull { column ->
                val entry = entries[row, column]
                val position = CellPosition(row, column)
                if (entry != 0 && entry != puzzle.solution[row, column]) position else null
            }
        }.toSet()
        return incorrectCells
    }

    private fun completedDigits(board: Board, conflictingCells: Set<CellPosition>): Set<Int> {
        return (1..9).filter { digit ->
            val positions = (0..8).flatMap { row ->
                (0..8).mapNotNull { col ->
                    if (board[row, col] == digit) CellPosition(row, col) else null
                }
            }
            positions.size == 9 && positions.none { it in conflictingCells }
        }.toSet()
    }

    fun selectCell(position: CellPosition) {
        selectedCell = position
    }

    fun enterDigit(digit: Int) {
        require(digit in 1..9)

        val position = selectedCell ?: return
        if (puzzle.givens[position.row, position.column] != 0) return

        if (isNoteMode) {
            if (entries[position.row, position.column] != 0) return

            saveCurrentState()
            pencilMarks = pencilMarks.toggle(position, digit)
        } else {
            if (
                entries[position.row, position.column] == digit &&
                pencilMarks.digitsAt(position).isEmpty()
            ) return

            saveCurrentState()
            entries = entries.withValue(position.row, position.column, digit)
            pencilMarks = pencilMarks.clear(position)
            incorrectCells = emptySet()
        }
    }

    fun clearSelectedCell() {
        val position = selectedCell ?: return
        if (puzzle.givens[position.row, position.column] != 0) return

        if (
            entries[position.row, position.column] == 0 &&
            pencilMarks.digitsAt(position).isEmpty()
        ) return

        saveCurrentState()
        entries = entries.withValue(position.row, position.column, 0)
        pencilMarks = pencilMarks.clear(position)
        incorrectCells = emptySet()
    }

    fun toggleNoteMode() {
        isNoteMode = !isNoteMode
    }

    fun undo() {
        val previousState = history.removeLastOrNull() ?: return
        entries = previousState.entries
        pencilMarks = previousState.pencilMarks
        incorrectCells = emptySet()
    }

    private fun currentBoard(): Board {
        var board = puzzle.givens
        for (row in 0..8) {
            for (column in 0..8) {
                if (board[row, column] == 0) {
                    board = board.withValue(row, column, entries[row, column])
                }
            }
        }
        return board
    }

    private fun saveCurrentState() {
        history.add(PlayerState(entries, pencilMarks))
    }

    private data class PlayerState(
        val entries: Board,
        val pencilMarks: PencilMarks,
    )

}
