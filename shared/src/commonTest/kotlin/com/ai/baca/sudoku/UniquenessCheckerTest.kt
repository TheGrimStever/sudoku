package com.ai.baca.sudoku

import com.ai.baca.domain.Board
import com.ai.baca.generator.UniquenessChecker
import com.ai.baca.sample.SamplePuzzles
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UniquenessCheckerTest {
    private val checker = UniquenessChecker()

    @Test
    fun returnsTrueForKnownUniquePuzzle() {
        assertTrue(checker.hasUniqueSolution(SamplePuzzles.easy.givens))
    }

    @Test
    fun returnsTrueForCompletedBoard() {
        assertTrue(checker.hasUniqueSolution(SamplePuzzles.easy.solution))
    }

    @Test
    fun returnsFalseForUnderConstrainedBoard() {
        val board = Board.fromRows(
            "100000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
        )

        assertFalse(checker.hasUniqueSolution(board))
    }
}
