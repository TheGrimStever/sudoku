package com.ai.baca.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ai.baca.domain.CellPosition
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
    var snapshot by remember(game) { mutableStateOf(game.snapshot()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Sudoku",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val useSideNumberPad = maxWidth >= 680.dp
            val numberPadWidth = 144.dp
            val spacing = 16.dp
            val boardSize = if (useSideNumberPad) {
                minOf(maxHeight, maxWidth - numberPadWidth - spacing, 480.dp)
            } else {
                minOf(maxWidth, maxHeight - 48.dp - spacing, 480.dp)
            }.coerceAtLeast(0.dp)
            val selectCell: (CellPosition) -> Unit = { position ->
                game.selectCell(position)
                snapshot = game.snapshot()
            }
            val enterDigit: (Int) -> Unit = { digit ->
                game.enterDigit(digit)
                snapshot = game.snapshot()
            }

            if (useSideNumberPad) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SudokuBoard(
                        snapshot = snapshot,
                        onCellSelected = selectCell,
                        modifier = Modifier.size(boardSize),
                    )
                    NumberPad(
                        onDigitSelected = enterDigit,
                        columns = 3,
                        modifier = Modifier.width(numberPadWidth),
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    SudokuBoard(
                        snapshot = snapshot,
                        onCellSelected = selectCell,
                        modifier = Modifier.size(boardSize),
                    )
                    NumberPad(
                        onDigitSelected = enterDigit,
                        modifier = Modifier.width(boardSize),
                    )
                }
            }
        }
    }
}
