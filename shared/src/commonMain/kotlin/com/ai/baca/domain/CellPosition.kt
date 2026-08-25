package com.ai.baca.domain

/**
 *  I'm making this optional at the moment. I'm thinking this might be a clean way to pass around the selected
 *  location cleanly, letting only Board.kt know the data structure detail of the IntArray.
 *  So Might delete this later, setting up the rest first to see if this class makes sense.
 */
class CellPosition(
    val row: Int,
    val column: Int
) {
    init {
        require(row in 0..8)
        require(column in 0..8)
    }
}