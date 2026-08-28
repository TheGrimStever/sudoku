package com.ai.baca.sudoku

import com.ai.baca.domain.BoardValidator
import com.ai.baca.domain.Difficulty
import com.ai.baca.generator.PuzzleGenerator
import com.ai.baca.generator.SolutionGenerator
import com.ai.baca.generator.UniquenessChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PuzzleGeneratorTest {
    private val boardValidator = BoardValidator()
    private val uniquenessChecker = UniquenessChecker()

    companion object {
        private val puzzle = PuzzleGenerator(
            solutionGenerator = SolutionGenerator(Random(42)),
            uniquenessChecker = UniquenessChecker(),
            random = Random(42),
        ).generate(Difficulty.EASY)
    }

    @Test
    fun easyPuzzleHasAtLeast36Clues() {
        val clueCount = (0..8).sumOf { row ->
            (0..8).count { column -> puzzle.givens[row, column] != 0 }
        }

        assertTrue(clueCount >= 36, "Expected at least 36 clues but got $clueCount")
    }

    @Test
    fun generatedPuzzleHasUniqueSolution() {
        assertTrue(uniquenessChecker.hasUniqueSolution(puzzle.givens))
    }

    @Test
    fun solutionIsAValidCompletedBoard() {
        assertTrue(boardValidator.isValidBoard(puzzle.solution))

        for (row in 0..8) {
            for (column in 0..8) {
                assertTrue(puzzle.solution[row, column] != 0, "Expected non-zero at row=$row column=$column")
            }
        }
    }

    @Test
    fun puzzleGenerationWorksFromBackgroundDispatcher() = runTest {
        val puzzle = withContext(Dispatchers.Default) {
            PuzzleGenerator(
                solutionGenerator = SolutionGenerator(Random(99)),
                uniquenessChecker = UniquenessChecker(),
                random = Random(99),
            ).generate(Difficulty.EASY)
        }

        assertTrue(uniquenessChecker.hasUniqueSolution(puzzle.givens))
        assertTrue(boardValidator.isValidBoard(puzzle.solution))
    }

    @Test
    fun everyGivenMatchesTheSolution() {
        for (row in 0..8) {
            for (column in 0..8) {
                val given = puzzle.givens[row, column]
                if (given != 0) {
                    assertEquals(
                        puzzle.solution[row, column],
                        given,
                        "Given at row=$row column=$column does not match solution"
                    )
                }
            }
        }
    }
}
