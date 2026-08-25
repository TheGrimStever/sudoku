package com.ai.baca.sample

import com.ai.baca.domain.Board
import com.ai.baca.domain.Puzzle
import com.ai.baca.domain.Difficulty

object SamplePuzzles {
    val easy = Puzzle(
        givens = Board.fromRows(
            "530070000",
            "600195000",
            "098000060",
            "800060003",
            "400803001",
            "700020006",
            "060000280",
            "000419005",
            "000080079",
        ),
        solution = Board.fromRows(
            "534678912",
            "672195348",
            "198342567",
            "859761423",
            "426853791",
            "713924856",
            "961537284",
            "287419635",
            "345286179",
        ),
        difficulty = Difficulty.EASY,
    )
}