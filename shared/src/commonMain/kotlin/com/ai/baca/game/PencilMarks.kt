package com.ai.baca.game

import com.ai.baca.domain.Board
import com.ai.baca.domain.CellPosition

/**
 * Immutable collection of pencil marks for every cell on a Sudoku board.
 *
 * Each cell uses the lowest nine bits of an [Int], one bit for each digit.
 */
class PencilMarks private constructor(
    private val cells: IntArray,
) {
    fun contains(position: CellPosition, digit: Int): Boolean {
        requireValidDigit(digit)
        return cells[indexOf(position)] and maskFor(digit) != 0
    }

    fun toggle(position: CellPosition, digit: Int): PencilMarks {
        requireValidDigit(digit)

        val updated = cells.copyOf()
        val index = indexOf(position)
        updated[index] = updated[index] xor maskFor(digit)
        return PencilMarks(updated)
    }

    fun clear(position: CellPosition): PencilMarks {
        val index = indexOf(position)
        if (cells[index] == 0) return this

        val updated = cells.copyOf()
        updated[index] = 0
        return PencilMarks(updated)
    }

    fun digitsAt(position: CellPosition): Set<Int> =
        (1..9).filterTo(linkedSetOf()) { digit -> contains(position, digit) }

    override fun equals(other: Any?): Boolean =
        this === other || other is PencilMarks && cells.contentEquals(other.cells)

    override fun hashCode(): Int = cells.contentHashCode()

    override fun toString(): String = "PencilMarks(cells=${cells.contentToString()})"

    private fun indexOf(position: CellPosition): Int =
        Board.indexOf(position.row, position.column)

    private fun maskFor(digit: Int): Int = 1 shl (digit - 1)

    private fun requireValidDigit(digit: Int) {
        require(digit in 1..9) { "Pencil mark digit must be between 1 and 9" }
    }

    companion object {
        fun empty(): PencilMarks = PencilMarks(IntArray(81))
    }
}
