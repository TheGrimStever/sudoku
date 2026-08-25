package com.ai.baca.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ai.baca.game.SudokuGame

/**
 * For now, this is in ./shared/src/commonMain/kotlin/com/ai/baca/ui
 *
 * This is because ./composeApp isn't set up as a Gradle module yet.
 * This is just to get the app playable and test the game logic.
 *
 * MOVE IT BACK ONCE THE GRADLE MODULE IS SET UP
 */

@Composable
fun SudokuScreen(game: SudokuGame) {
    val snapshot = game.snapshot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Sudoku",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(24.dp))

        SudokuBoard(snapshot = snapshot)
    }
}