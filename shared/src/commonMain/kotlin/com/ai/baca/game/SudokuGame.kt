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

    fun selectCell(position: CellPosition) {}

    fun enterDigit(digit: Int) { /* reject edits to 'givens', update entries */}

    fun clearSelectedCell(position: CellPosition) {
        // clear only a player-entered value
    }

    fun newGame(difficulty: Difficulty = Difficulty.EASY) {}

}