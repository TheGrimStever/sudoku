package com.ai.baca.sudoku

import com.ai.baca.domain.BoardValidator
import com.ai.baca.generator.SolutionGenerator
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class SolutionGeneratorTest {
    private val boardValidator = BoardValidator()

    @Test
    fun generatedBoardHasNoEmptyCells() {
        val board = SolutionGenerator(Random(0)).generate()

        for (row in 0..8) {
            for (column in 0..8) {
                assertNotEquals(0, board[row, column], "Expected non-zero at row=$row column=$column")
            }
        }
    }

    @Test
    fun generatedBoardIsAValidSudoku() {
        val board = SolutionGenerator(Random(0)).generate()

        assertTrue(boardValidator.isValidBoard(board))
    }

    @Test
    fun differentSeedsProduceDifferentBoards() {
        val first = SolutionGenerator(Random(1)).generate()
        val second = SolutionGenerator(Random(2)).generate()

        val identical = (0..8).all { row ->
            (0..8).all { column -> first[row, column] == second[row, column] }
        }
        assertFalse(identical)
    }
}
