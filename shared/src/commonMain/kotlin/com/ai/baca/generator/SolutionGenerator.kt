package com.ai.baca.generator

import com.ai.baca.domain.Board
import kotlin.random.Random

class SolutionGenerator(private val random: Random = Random.Default) {

    fun generate(): Board {
        val cells = IntArray(81)
        fill(cells)
        return toBoard(cells)
    }

    private fun fill(cells: IntArray): Boolean {
        val index = cells.indexOfFirst { it == 0 }
        if (index == -1) return true

        val targetRow = index / 9
        val targetCol = index % 9

        for (digit in (1..9).shuffled(random)) {
            if (isValid(cells, targetRow, targetCol, digit)) {
                cells[index] = digit
                if (fill(cells)) return true
                cells[index] = 0
            }
        }
        return false
    }

    private fun isValid(cells: IntArray, targetRow: Int, targetCol: Int, digit: Int): Boolean {
        for (col in 0..8) if (cells[targetRow * 9 + col] == digit) return false
        for (row in 0..8) if (cells[row * 9 + targetCol] == digit) return false
        val boxRow = targetRow / 3 * 3
        val boxCol = targetCol / 3 * 3
        for (row in boxRow until boxRow + 3)
            for (col in boxCol until boxCol + 3)
                if (cells[row * 9 + col] == digit) return false
        return true
    }

    companion object {
        internal fun toBoard(cells: IntArray): Board {
            var board = Board.empty()
            for (i in cells.indices) {
                if (cells[i] != 0) board = board.withValue(i / 9, i % 9, cells[i])
            }
            return board
        }
    }
}
