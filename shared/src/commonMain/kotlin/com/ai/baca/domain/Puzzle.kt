package com.ai.baca.domain

data class Puzzle(
    val givens: Board,
    val solution: Board,
    val difficulty: Difficulty
) {
}