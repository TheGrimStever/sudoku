package com.ai.baca

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.ai.baca.game.SudokuGame
import com.ai.baca.sample.SamplePuzzles
import com.ai.baca.ui.SudokuScreen

@Composable
@Preview
fun App() {
    val game = remember {
        SudokuGame(SamplePuzzles.easy)
    }
    MaterialTheme {
        SudokuScreen(game = game)
    }
}