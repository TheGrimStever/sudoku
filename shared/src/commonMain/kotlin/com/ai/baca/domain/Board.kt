package com.ai.baca.domain

class Board private constructor(
    private val cells: IntArray
) {
    operator fun get(row: Int, column: Int): Int =
       cells[indexOf(row, column)]

    fun withValue(row: Int, column: Int, value: Int): Board {
        require(value in 0..9)

        val updated = cells.copyOf()
        updated[indexOf(row, column)] = value

        return Board(updated)
    }


    companion object {
        fun empty() = Board(IntArray(81))

        fun indexOf(row: Int, column: Int): Int {
            require(row in 0..8 && column in 0..8)

            return row * 9 + column
        }

        // Hardcoding a puzzle to test other game logic
        fun fromRows(vararg rows: String): Board {
            require(rows.size == 9)
            require((rows.all { it.length == 9 }))

            val cells = rows
                .joinToString(separator = "")
                .map { character ->
                    when (character) {
                        '.', '0' -> 0
                        in '1'..'9' -> character.digitToInt()
                        else -> error("Invalid board character: $character")
                    }
                }
                .toIntArray()
            return Board(cells)
        }
    }

}