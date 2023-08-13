package com.movatica.ledtagprogrammer

import android.graphics.Color

enum class LEDColor {
    RED,
    GREEN,
    BLUE,
    ORANGE,
    PINK,
    WHITE;

    fun toColor(brightness: Int = 0): Int {
        val hue = when (this) {
            WHITE -> 0f
            RED -> 0f
            ORANGE -> 40f
            GREEN -> 120f
            BLUE -> 240f
            PINK -> 300f
        }

        val saturation = when (this) {
            WHITE -> 0f
            else -> 1f
        }

        val value = when (brightness) {
            0 -> 0.24f
            1 -> 0.64f
            2 -> 0.76f
            3 -> 0.88f
            else -> 1.0f
        }

        return Color.HSVToColor(floatArrayOf(hue, saturation, value))
    }

    companion object {
        @JvmStatic
        fun valueOfOrNull(value: String): LEDColor? {
            return try {
                valueOf(value)
            } catch (e: java.lang.IllegalArgumentException) {
                null
            }
        }
    }
}