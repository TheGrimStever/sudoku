package com.ai.baca.generator

import com.ai.baca.domain.Board
import com.ai.baca.domain.Difficulty
import com.ai.baca.domain.Puzzle
import kotlin.random.Random

class PuzzleGenerator(
    private val solutionGenerator: SolutionGenerator,
    private val uniquenessChecker: UniquenessChecker,
    private val random: Random = Random.Default,
) {

    fun generate(difficulty: Difficulty): Puzzle {
        val solution = solutionGenerator.generate()
        val pendingGivens = IntArray(81) { solution[it / 9, it % 9] }
        val minClues = when (difficulty) {
            Difficulty.EASY -> 36
            Difficulty.MEDIUM -> 27
            Difficulty.HARD -> 22
        }

        for (index in (0..80).shuffled(random)) {
            if (pendingGivens.count { it != 0 } <= minClues) break
            val backup = pendingGivens[index]
            pendingGivens[index] = 0
            if (!uniquenessChecker.hasUniqueSolution(toBoard(pendingGivens))) {
                pendingGivens[index] = backup
            }
        }

        return Puzzle(
            givens = toBoard(pendingGivens),
            solution = solution,
            difficulty = difficulty,
        )
    }

    private fun toBoard(workingGrid: IntArray): Board {
        var board = Board.empty()
        for (i in workingGrid.indices) {
            if (workingGrid[i] != 0) board = board.withValue(i / 9, i % 9, workingGrid[i])
        }
        return board
    }
}
