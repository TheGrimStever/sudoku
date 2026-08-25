package com.ai.baca.game

import com.ai.baca.domain.Board
import com.ai.baca.domain.CellPosition
import com.ai.baca.domain.Difficulty
import com.ai.baca.domain.Puzzle

class SudokuGame(
    private val puzzle: Puzzle
) {

    private var entries = Board.empty()
    private var selectedCell: CellPosition? = null

    fun snapshot() = GameSnapshot(
        givens = puzzle.givens,
        entries = entries,
        selectedCell = selectedCell,
    )

    fun selectCell(position: CellPosition) {
        selectedCell = position
    }

    fun enterDigit(digit: Int) {
        require(digit in 1..9)

        val position = selectedCell ?: return
        if (puzzle.givens[position.row, position.column] != 0) return

        entries = entries.withValue(position.row, position.column, digit)
    }

    fun clearSelectedCell(position: CellPosition) {
        if (puzzle.givens[position.row, position.column] != 0) return
        entries = entries.withValue(position.row, position.column, 0)
    }

    fun newGame(difficulty: Difficulty = Difficulty.EASY) {}

}
