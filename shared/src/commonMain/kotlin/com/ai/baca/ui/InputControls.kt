package com.ai.baca.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputControls(
    isNoteMode: Boolean,
    canUndo: Boolean,
    canClear: Boolean,
    canCheck: Boolean,
    onUndo: () -> Unit,
    onNoteModeToggle: () -> Unit,
    onClear: () -> Unit,
    onCheck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        OutlinedButton(
            onClick = onUndo,
            enabled = canUndo,
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text("Undo")
        }

        if (isNoteMode) {
            FilledTonalButton(
                onClick = onNoteModeToggle,
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text("Notes")
            }
        } else {
            OutlinedButton(
                onClick = onNoteModeToggle,
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text("Notes")
            }
        }

        OutlinedButton(
            onClick = onClear,
            enabled = canClear,
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text("Clear")
        }

        OutlinedButton(
            onClick = onCheck,
            enabled = canCheck,
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text("Check")
        }
    }
}
