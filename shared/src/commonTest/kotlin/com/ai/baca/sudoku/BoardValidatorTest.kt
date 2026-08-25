package com.ai.baca.sudoku

import com.ai.baca.domain.Board
import com.ai.baca.domain.BoardValidator
import com.ai.baca.domain.CellPosition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BoardValidatorTest {
    private val validator = BoardValidator()

    @Test
    fun findsEveryCellParticipatingInDuplicates() {
        val board = Board.fromRows(
            "500000005",
            "500000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
            "000000000",
        )

        assertEquals(
            setOf(CellPosition(0, 0), CellPosition(0, 8), CellPosition(1, 0)),
            validator.findConflictingCells(board),
        )
    }

    @Test
    fun placementRejectsRowColumnAndBoxDuplicates() {
        val board = Board.fromRows(
            "100000000",
            "000000000",
            "000000000",
            "000000000",
            "000010000",
            "000000000",
            "000000000",
            "000000000",
            "000000001",
        )

        assertFalse(validator.isValidPlacement(board, CellPosition(0, 8), 1))
        assertFalse(validator.isValidPlacement(board, CellPosition(8, 0), 1))
        assertFalse(validator.isValidPlacement(board, CellPosition(3, 3), 1))
        assertTrue(validator.isValidPlacement(board, CellPosition(0, 8), 2))
    }

}
