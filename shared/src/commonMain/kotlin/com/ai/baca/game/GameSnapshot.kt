package com.ai.baca.game

import com.ai.baca.domain.Board
import com.ai.baca.domain.CellPosition

data class GameSnapshot(
    val givens: Board,
    val entries: Board,
    val pencilMarks: PencilMarks,
    val selectedCell: CellPosition?,
    val conflictingCells: Set<CellPosition>,
    val incorrectCells: Set<CellPosition>,
    val completedDigits: Set<Int>,
    val isNoteMode: Boolean,
    val canUndo: Boolean,
) {
    val isComplete: Boolean get() = completedDigits.size == 9
}
