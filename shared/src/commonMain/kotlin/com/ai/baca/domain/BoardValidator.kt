package com.ai.baca.domain

class BoardValidator {

    fun findConflictingCells(board: Board): Set<CellPosition> {
        val conflicts = mutableSetOf<CellPosition>()

        for (row in 0..8) {
            addDuplicates(
                positions = (0..8).map { column -> CellPosition(row, column) },
                board = board,
                conflicts = conflicts,
            )
        }

        for (column in 0..8) {
            addDuplicates(
                positions = (0..8).map { row -> CellPosition(row, column) },
                board = board,
                conflicts = conflicts,
            )
        }

        for (boxRow in 0..2) {
            for (boxColumn in 0..2) {
                val positions = buildList {
                    for (row in boxRow * 3 until boxRow * 3 + 3) {
                        for (column in boxColumn * 3 until boxColumn * 3 + 3) {
                            add(CellPosition(row, column))
                        }
                    }
                }
                addDuplicates(positions, board, conflicts)
            }
        }

        return conflicts
    }

    fun isValidPlacement(
        board: Board,
        position: CellPosition,
        value: Int
    ): Boolean {

        require(value in 1..9)

        for (column in 0..8) {
            if (column != position.column && board[position.row, column] == value) return false
        }
        for (row in 0..8) {
            if (row != position.row && board[row, position.column] == value) return false
        }

        val boxStartRow = position.row / 3 * 3
        val boxStartColumn = position.column / 3 * 3
        for (row in boxStartRow until boxStartRow + 3) {
            for (column in boxStartColumn until boxStartColumn + 3) {
                if ((row != position.row || column != position.column) && board[row, column] == value) {
                    return false
                }
            }
        }

        return true
    }

    fun isValidBoard(board: Board): Boolean {

        return findConflictingCells(board).isEmpty()
    }

    private fun addDuplicates(
        positions: List<CellPosition>,
        board: Board,
        conflicts: MutableSet<CellPosition>,
    ) {
        positions
            .filter { board[it.row, it.column] != 0 }
            .groupBy { board[it.row, it.column] }
            .values
            .filter { matchingPositions -> matchingPositions.size > 1 }
            .forEach(conflicts::addAll)
    }
}
