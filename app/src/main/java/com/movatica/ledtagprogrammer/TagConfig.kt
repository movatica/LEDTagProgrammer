package com.movatica.ledtagprogrammer

import android.content.SharedPreferences
import com.movatica.ledtagprogrammer.extensions.getString

// Secondary constructor to help building it from SharedPreferences
fun TagConfig(sharedPreferences: SharedPreferences?): TagConfig {
    return if (sharedPreferences == null) {
        TagConfig()
    } else {
        val ledColor : LEDColor = sharedPreferences.getString("color")?.let{ LEDColor.valueOfOrNull(it) }?: TagConfig.DEFAULT_COLOR

        when (sharedPreferences.getString("resolution")) {
            "48x12" -> TagConfig(48, 12, ledColor)
            else -> TagConfig(ledColor = ledColor)
        }
    }
}

data class TagConfig
    constructor (
        val xResolution: Int = 44,
        val yResolution: Int = 11,
        val ledColor: LEDColor = DEFAULT_COLOR
    ) {

    /*
    LEDTag is defined by the rectangle
    (0, 0)              (xResolution-1, 0)
    (0, yResolution-1)  (xResolution-1, yResolution-1)
     */

    // Border animation starts in the lower right corner and runs corner clockwise
    // Fill an array with the indices of the border in the correct order
    val borderIndices = IntArray((xResolution - 1 + yResolution - 1) * 2)
    val size = xResolution * yResolution

    init {
        val maxX = xResolution-1
        val maxY = yResolution-1

        for (y in 0 until maxY) {
            // right line
            borderIndices[y] = index(maxX, maxY - y)
            // left line
            borderIndices[maxY + maxX + y] = index(0, y)
        }
        for (x in 0 until maxX) {
            // top line
            borderIndices[maxY + x] = index(maxX - x, 0)
            // bottom line
            borderIndices[maxY + maxX + maxY + x] = index(x, maxY)
        }
    }

    fun index(x: Int, y: Int) = x * yResolution + y

    companion object {
        val DEFAULT_COLOR = LEDColor.GREEN
    }
}