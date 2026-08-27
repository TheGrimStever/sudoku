package com.ai.baca.sudoku

import com.ai.baca.domain.CellPosition
import com.ai.baca.game.PencilMarks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PencilMarksTest {
    private val position = CellPosition(row = 2, column = 4)

    @Test
    fun emptyPencilMarksContainNoDigits() {
        val pencilMarks = PencilMarks.empty()

        assertTrue(pencilMarks.digitsAt(position).isEmpty())
        for (digit in 1..9) {
            assertFalse(pencilMarks.contains(position, digit))
        }
    }

    @Test
    fun togglingDigitAddsAndRemovesIt() {
        val withDigit = PencilMarks.empty().toggle(position, 7)
        val withoutDigit = withDigit.toggle(position, 7)

        assertTrue(withDigit.contains(position, 7))
        assertFalse(withoutDigit.contains(position, 7))
    }

    @Test
    fun cellCanContainAllNineDigitsInOrder() {
        val pencilMarks = (1..9).fold(PencilMarks.empty()) { marks, digit ->
            marks.toggle(position, digit)
        }

        assertEquals((1..9).toSet(), pencilMarks.digitsAt(position))
    }

    @Test
    fun marksAreIndependentBetweenCells() {
        val otherPosition = CellPosition(row = 2, column = 5)
        val pencilMarks = PencilMarks.empty()
            .toggle(position, 3)
            .toggle(otherPosition, 8)

        assertEquals(setOf(3), pencilMarks.digitsAt(position))
        assertEquals(setOf(8), pencilMarks.digitsAt(otherPosition))
    }

    @Test
    fun clearRemovesEveryDigitFromOnlyTheRequestedCell() {
        val otherPosition = CellPosition(row = 8, column = 8)
        val pencilMarks = PencilMarks.empty()
            .toggle(position, 1)
            .toggle(position, 9)
            .toggle(otherPosition, 5)
            .clear(position)

        assertTrue(pencilMarks.digitsAt(position).isEmpty())
        assertEquals(setOf(5), pencilMarks.digitsAt(otherPosition))
    }

    @Test
    fun updatesDoNotMutatePreviousValue() {
        val original = PencilMarks.empty().toggle(position, 4)
        val updated = original.toggle(position, 6)

        assertEquals(setOf(4), original.digitsAt(position))
        assertEquals(setOf(4, 6), updated.digitsAt(position))
        assertNotEquals(original, updated)
    }

    @Test
    fun equalContentsHaveValueEquality() {
        val first = PencilMarks.empty().toggle(position, 2)
        val second = PencilMarks.empty().toggle(position, 2)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun digitsOutsideSudokuRangeAreRejected() {
        val pencilMarks = PencilMarks.empty()

        assertFailsWith<IllegalArgumentException> { pencilMarks.toggle(position, 0) }
        assertFailsWith<IllegalArgumentException> { pencilMarks.toggle(position, 10) }
        assertFailsWith<IllegalArgumentException> { pencilMarks.contains(position, 0) }
        assertFailsWith<IllegalArgumentException> { pencilMarks.contains(position, 10) }
    }
}
