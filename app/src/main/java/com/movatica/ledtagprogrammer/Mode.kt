package com.movatica.ledtagprogrammer

import com.movatica.ledtagprogrammer.extensions.titlecase

enum class Mode(
    val index: Int
    ) {
    LEFT(0),
    RIGHT(1),
    UP(2),
    DOWN(3),
    FIXED(4),
    SNOWFLAKE(5),
    PICTURE(6),
    ANIMATION(7),
    LASER(8);

    fun toInt() = index
    fun toByte() = index.toByte()

    override fun toString(): String {
        return super.toString().titlecase()
    }

    companion object {
        @JvmStatic
        fun valueOfIndex(index: Int) : Mode {
            return when(index) {
                0 -> LEFT
                1 -> RIGHT
                2 -> UP
                3 -> DOWN
                4 -> FIXED
                5 -> SNOWFLAKE
                6 -> PICTURE
                7 -> ANIMATION
                8 -> LASER
                else -> throw IndexOutOfBoundsException()
            }
        }
    }
}