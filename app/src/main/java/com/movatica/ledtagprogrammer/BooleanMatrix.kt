package com.movatica.ledtagprogrammer

data class BooleanMatrix(val width: Int, val height: Int) {
    val size = width * height
    private val data = BooleanArray(size)

    private fun index(c: Int, r: Int) = c * height + r

    fun get(c: Int, r: Int) = data[index(c, r)]
    fun set(c: Int, r: Int, value: Boolean) { data[index(c, r)] = value }

    fun replaceWith(other: BooleanMatrix, colOffset: Int = 0, rowOffset: Int = 0) {
        for (sourceCol in 0 until other.width) {
            val targetCol = sourceCol + colOffset

            if (targetCol in 0 until width) {
                for (sourceRow in 0 until other.height) {
                    val targetRow = sourceRow + rowOffset

                    if (targetRow in 0 until height) {
                        set(targetCol, targetRow, other.get(sourceCol, sourceRow))
                    }
                }
            }
        }
    }

    companion object {
        fun fromBinaryString(string: String, trueVal: Char = '1', rowEnd: Char = '\n') : BooleanMatrix {
            val lines = string.split(rowEnd)

            if (lines.isEmpty()) return BooleanMatrix(0,0)

            val matrix = BooleanMatrix(lines.first().length, lines.size)

            for ((row, line) in lines.withIndex()) {
                for ((column, char) in line.withIndex()) {
                    matrix.set(column, row, char == trueVal)
                }
            }

            return matrix
        }
    }
}
