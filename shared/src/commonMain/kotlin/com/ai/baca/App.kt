package com.ai.baca

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ai.baca.domain.Difficulty
import com.ai.baca.game.SudokuGame
import com.ai.baca.generator.PuzzleGenerator
import com.ai.baca.generator.SolutionGenerator
import com.ai.baca.generator.UniquenessChecker
import com.ai.baca.ui.DifficultySelectionScreen
import com.ai.baca.ui.LoadingScreen
import com.ai.baca.ui.SudokuScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
@Preview
fun App() {
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) }

    MaterialTheme {
        Box(modifier = Modifier.safeDrawingPadding()) {
            val difficulty = selectedDifficulty
            if (difficulty == null) {
                DifficultySelectionScreen(
                    onDifficultySelected = { selectedDifficulty = it },
                )
            } else {
                val game by produceState<SudokuGame?>(initialValue = null, key1 = difficulty) {
                    value = withContext(Dispatchers.Default) {
                        val puzzle = PuzzleGenerator(
                            solutionGenerator = SolutionGenerator(),
                            uniquenessChecker = UniquenessChecker(),
                        ).generate(difficulty)
                        SudokuGame(puzzle)
                    }
                }

                if (game == null) {
                    LoadingScreen()
                } else {
                    SudokuScreen(
                        game = game!!,
                        onNewGame = { selectedDifficulty = null },
                    )
                }
            }
        }
    }
}
