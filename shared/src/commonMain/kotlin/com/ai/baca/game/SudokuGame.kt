package com.ai.baca.game

import com.ai.baca.domain.Board
import com.ai.baca.domain.BoardValidator
import com.ai.baca.domain.CellPosition
import com.ai.baca.domain.Difficulty
import com.ai.baca.domain.Puzzle

class SudokuGame(
    private val puzzle: Puzzle,
    private val boardValidator: BoardValidator = BoardValidator(),
) {

    private var entries = Board.empty()
    private var pencilMarks = PencilMarks.empty()
    private var selectedCell: CellPosition? = null
    private var isNoteMode = false
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
            completedDigits = completedDigits(board, conflictingCells),
            isNoteMode = isNoteMode,
            canUndo = history.isNotEmpty(),
        )
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
    }

    fun toggleNoteMode() {
        isNoteMode = !isNoteMode
    }

    fun undo() {
        val previousState = history.removeLastOrNull() ?: return
        entries = previousState.entries
        pencilMarks = previousState.pencilMarks
    }

    fun newGame(difficulty: Difficulty = Difficulty.EASY) {}

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
