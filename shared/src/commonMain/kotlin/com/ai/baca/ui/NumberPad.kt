package com.ai.baca.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun NumberPad(
    onDigitSelected: (Int) -> Unit,
    completedDigits: Set<Int> = emptySet(),
    modifier: Modifier = Modifier,
    columns: Int = 9,
) {
    require(columns > 0)

    val strikethroughColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        (1..9).chunked(columns).forEach { digits ->
            Row(modifier = Modifier.fillMaxWidth()) {
                digits.forEach { digit ->
                    val completed = digit in completedDigits
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .alpha(if (completed) 0.35f else 1f)
                            .clickable(
                                enabled = !completed,
                                role = Role.Button,
                                onClick = { onDigitSelected(digit) },
                            )
                            .drawWithContent {
                                drawContent()
                                if (completed) {
                                    drawLine(
                                        color = strikethroughColor,
                                        start = Offset(x = size.width * 0.2f, y = size.height * 0.8f),
                                        end = Offset(x = size.width * 0.8f, y = size.height * 0.2f),
                                        strokeWidth = 2.dp.toPx(),
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = digit.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                }
            }
        }
    }
}
