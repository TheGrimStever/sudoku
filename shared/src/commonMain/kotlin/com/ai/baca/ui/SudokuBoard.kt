package com.ai.baca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ai.baca.domain.CellPosition
import com.ai.baca.game.GameSnapshot


/**
 * For now, this is in ./shared/src/commonMain/kotlin/com/ai/baca/ui
 *
 * This is because ./composeApp isn't set up as a Gradle module yet.
 * This is just to get the app playable and test the game logic.
 *
 * MOVE IT BACK ONCE THE GRADLE MODULE IS SET UP
 */

@Composable
fun SudokuBoard(
    snapshot: GameSnapshot,
    onCellSelected: (CellPosition) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val boardSize = minOf(maxWidth, maxHeight, 480.dp)

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(boardSize),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                repeat(9) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        repeat(9) { column ->
                            val given = snapshot.givens[row, column]
                            val entry = snapshot.entries[row, column]
                            val value = if (given != 0) given else entry
                            val position = CellPosition(row, column)
                            val conflicting = position in snapshot.conflictingCells
                            val selected = snapshot.selectedCell?.let {
                                it.row == row && it.column == column
                            } == true
                            val related = snapshot.selectedCell?.let {
                                it.row == row ||
                                    it.column == column ||
                                    (it.row / 3 == row / 3 && it.column / 3 == column / 3)
                            } == true

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        when {
                                            conflicting -> MaterialTheme.colorScheme.errorContainer
                                            selected -> MaterialTheme.colorScheme.primaryContainer
                                            related -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                    )
                                    .clickable { onCellSelected(position) },
                                contentAlignment = Alignment.Center,
                            ) {
                                if (value != 0) {
                                    Text(
                                        text = value.toString(),
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = if (given != 0) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        },
                                        color = if (conflicting) {
                                            MaterialTheme.colorScheme.error
                                        } else {
                                            Color.Unspecified
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }

            val cellLineColor = MaterialTheme.colorScheme.outline
            val boxLineColor = MaterialTheme.colorScheme.onSurface
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellStrokeWidth = 0.5.dp.toPx()
                val boxStrokeWidth = 2.dp.toPx()

                for (boundary in 1..8) {
                    val x = size.width * boundary / 9f
                    val y = size.height * boundary / 9f
                    val isBoxBoundary = boundary % 3 == 0
                    val color = if (isBoxBoundary) boxLineColor else cellLineColor
                    val strokeWidth = if (isBoxBoundary) boxStrokeWidth else cellStrokeWidth

                    drawLine(color, Offset(x, 0f), Offset(x, size.height), strokeWidth)
                    drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth)
                }

                val inset = boxStrokeWidth / 2f
                drawRect(
                    color = boxLineColor,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - boxStrokeWidth, size.height - boxStrokeWidth),
                    style = Stroke(boxStrokeWidth),
                )
            }
        }
    }
}
