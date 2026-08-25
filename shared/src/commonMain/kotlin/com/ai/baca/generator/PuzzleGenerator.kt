package com.ai.baca.generator

import com.ai.baca.domain.Board
import com.ai.baca.domain.Difficulty
import com.ai.baca.domain.Puzzle
import com.ai.baca.game.SudokuGame

class PuzzleGenerator(
    private val solutionGenerator: SolutionGenerator,
    private val uniquenessChecker: UniquenessChecker
) {

    fun generate(difficulty: Difficulty): Puzzle {

        return TODO()
    }
}