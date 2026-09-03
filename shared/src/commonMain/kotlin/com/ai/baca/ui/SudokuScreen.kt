package com.ai.baca.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.focusable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
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
fun SudokuScreen(game: SudokuGame, onNewGame: () -> Unit) {
    var snapshot by remember(game) { mutableStateOf(game.snapshot()) }
    var victoryAcknowledged by remember(game) { mutableStateOf(false) }
    var checkResultMessage by remember(game) { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                when (event.key) {
                    Key.One, Key.NumPad1 -> { game.enterDigit(1); snapshot = game.snapshot(); true }
                    Key.Two, Key.NumPad2 -> { game.enterDigit(2); snapshot = game.snapshot(); true }
                    Key.Three, Key.NumPad3 -> { game.enterDigit(3); snapshot = game.snapshot(); true }
                    Key.Four, Key.NumPad4 -> { game.enterDigit(4); snapshot = game.snapshot(); true }
                    Key.Five, Key.NumPad5 -> { game.enterDigit(5); snapshot = game.snapshot(); true }
                    Key.Six, Key.NumPad6 -> { game.enterDigit(6); snapshot = game.snapshot(); true }
                    Key.Seven, Key.NumPad7 -> { game.enterDigit(7); snapshot = game.snapshot(); true }
                    Key.Eight, Key.NumPad8 -> { game.enterDigit(8); snapshot = game.snapshot(); true }
                    Key.Nine, Key.NumPad9 -> { game.enterDigit(9); snapshot = game.snapshot(); true }
                    Key.Backspace, Key.Delete -> { game.clearSelectedCell(); snapshot = game.snapshot(); true }
                    Key.DirectionUp -> {
                        snapshot.selectedCell?.let { pos ->
                            if (pos.row > 0) { game.selectCell(CellPosition(pos.row - 1, pos.column)); snapshot = game.snapshot() }
                        }
                        true
                    }
                    Key.DirectionDown -> {
                        snapshot.selectedCell?.let { pos ->
                            if (pos.row < 8) { game.selectCell(CellPosition(pos.row + 1, pos.column)); snapshot = game.snapshot() }
                        }
                        true
                    }
                    Key.DirectionLeft -> {
                        snapshot.selectedCell?.let { pos ->
                            if (pos.column > 0) { game.selectCell(CellPosition(pos.row, pos.column - 1)); snapshot = game.snapshot() }
                        }
                        true
                    }
                    Key.DirectionRight -> {
                        snapshot.selectedCell?.let { pos ->
                            if (pos.column < 8) { game.selectCell(CellPosition(pos.row, pos.column + 1)); snapshot = game.snapshot() }
                        }
                        true
                    }
                    else -> false
                }
            },
    ) {
        val useSideNumberPad = maxWidth >= 680.dp
        val numberPadWidth = 260.dp
        val spacing = 16.dp
        val controlHeight = 48.dp
        val titleHeight = 48.dp
        val compactInputHeight = controlHeight + spacing + 48.dp
        val boardSize = if (useSideNumberPad) {
            minOf(maxHeight - titleHeight - spacing, maxWidth - numberPadWidth - spacing, 480.dp)
        } else {
            minOf(maxWidth, maxHeight - compactInputHeight - spacing, 480.dp)
        }.coerceAtLeast(0.dp)

        val selectCell: (CellPosition) -> Unit = { position ->
            game.selectCell(position)
            snapshot = game.snapshot()
        }
        val enterDigit: (Int) -> Unit = { digit ->
            game.enterDigit(digit)
            snapshot = game.snapshot()
        }
        val undo: () -> Unit = {
            game.undo()
            snapshot = game.snapshot()
        }
        val toggleNoteMode: () -> Unit = {
            game.toggleNoteMode()
            snapshot = game.snapshot()
        }
        val clearSelectedCell: () -> Unit = {
            game.clearSelectedCell()
            snapshot = game.snapshot()
        }
        val canClear = snapshot.selectedCell?.let { position ->
            snapshot.givens[position.row, position.column] == 0 &&
                (
                    snapshot.entries[position.row, position.column] != 0 ||
                        snapshot.pencilMarks.digitsAt(position).isNotEmpty()
                )
        } == true
        val canCheck = (0..8).any { row ->
            (0..8).any { column -> snapshot.entries[row, column] != 0 }
        }
        val checkForErrors: () -> Unit = {
            val incorrectCells = game.checkForErrors()
            snapshot = game.snapshot()
            checkResultMessage = if (incorrectCells.isEmpty()) {
                "No mistakes found so far. Keep going!"
            } else {
                val entryWord = if (incorrectCells.size == 1) "entry" else "entries"
                "Found ${incorrectCells.size} incorrect $entryWord, highlighted on the board."
            }
        }

        if (useSideNumberPad) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = Alignment.Top,
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Sudoku",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Spacer(modifier = Modifier.height(spacing))
                    SudokuBoard(
                        snapshot = snapshot,
                        onCellSelected = selectCell,
                        modifier = Modifier.size(boardSize),
                    )
                }
                Column(
                    modifier = Modifier.width(numberPadWidth),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    OutlinedButton(
                        onClick = onNewGame,
                        modifier = Modifier
                            .height(controlHeight)
                            .fillMaxWidth(),
                    ) {
                        Text("New Game")
                    }
                    InputControls(
                        isNoteMode = snapshot.isNoteMode,
                        canUndo = snapshot.canUndo,
                        canClear = canClear,
                        canCheck = canCheck,
                        onUndo = undo,
                        onNoteModeToggle = toggleNoteMode,
                        onClear = clearSelectedCell,
                        onCheck = checkForErrors,
                    )
                    NumberPad(
                        onDigitSelected = enterDigit,
                        completedDigits = snapshot.completedDigits,
                        columns = 3,
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Sudoku",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    OutlinedButton(onClick = onNewGame) {
                        Text("New Game")
                    }
                }
                SudokuBoard(
                    snapshot = snapshot,
                    onCellSelected = selectCell,
                    modifier = Modifier.size(boardSize),
                )
                Column(
                    modifier = Modifier.width(boardSize),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                ) {
                    InputControls(
                        isNoteMode = snapshot.isNoteMode,
                        canUndo = snapshot.canUndo,
                        canClear = canClear,
                        canCheck = canCheck,
                        onUndo = undo,
                        onNoteModeToggle = toggleNoteMode,
                        onClear = clearSelectedCell,
                        onCheck = checkForErrors,
                    )
                    NumberPad(
                        onDigitSelected = enterDigit,
                        completedDigits = snapshot.completedDigits,
                    )
                }
            }
        }

        if (snapshot.isComplete && !victoryAcknowledged) {
            AlertDialog(
                onDismissRequest = { victoryAcknowledged = true },
                title = { Text("Puzzle Complete!") },
                text = { Text("Congratulations, you solved the puzzle!") },
                confirmButton = {
                    Button(onClick = onNewGame) {
                        Text("New Game")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { victoryAcknowledged = true }) {
                        Text("View Board")
                    }
                },
            )
        }

        checkResultMessage?.let { message ->
            AlertDialog(
                onDismissRequest = { checkResultMessage = null },
                title = { Text("Check Puzzle") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = { checkResultMessage = null }) {
                        Text("OK")
                    }
                },
            )
        }
    }
}
