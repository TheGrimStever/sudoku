package com.ai.baca.generator

import com.ai.baca.domain.Board

class UniquenessChecker {

    fun hasUniqueSolution(board: Board): Boolean {
        val cells = IntArray(81) { board[it / 9, it % 9] }
        return countSolutions(cells, limit = 2) == 1
    }

    private fun countSolutions(cells: IntArray, limit: Int): Int {
        val index = cells.indexOfFirst { it == 0 }
        if (index == -1) return 1

        val targetRow = index / 9
        val targetCol = index % 9
        var count = 0

        for (digit in 1..9) {
            if (isValid(cells, targetRow, targetCol, digit)) {
                cells[index] = digit
                count += countSolutions(cells, limit)
                cells[index] = 0
                if (count >= limit) return count
            }
        }
        return count
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
}
