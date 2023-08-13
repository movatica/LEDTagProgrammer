package com.movatica.ledtagprogrammer

import android.graphics.Bitmap
import android.graphics.Point

class AnimationRenderer {
}

fun renderAnimation(tagConfig: TagConfig, brightness: Int = 3, animationConfig: AnimationConfig): List<Bitmap> {
    val renderer = FrameDrawer(tagConfig).apply {
        this.brightness = brightness
    }

    val textPoints = pointsFromText(animationConfig.text)
    val textWidth = textPoints.maxBy { p -> p.x }.x

    val noLEDs = makeMatrix(tagConfig)

    val frameCount : Int = when (animationConfig.mode) {
        Mode.LEFT -> tagConfig.xResolution * 4
        Mode.RIGHT -> tagConfig.xResolution * 4
        Mode.UP -> tagConfig.yResolution * 4
        Mode.DOWN -> tagConfig.yResolution * 4
        Mode.FIXED -> 4
        Mode.SNOWFLAKE -> 4
        Mode.PICTURE -> 4
        Mode.ANIMATION -> 4
        Mode.LASER -> 4
    }

    val frames = ArrayList<Bitmap>(frameCount)

    var frameIndex = 0
    var currentActive = noLEDs

    while (frameIndex < frameCount) {
        frameIndex++

        if (animationConfig.flash && frameIndex % 4 > 2) {
            currentActive = noLEDs
        }
        else {
            currentActive = when (animationConfig.mode) {
                    Mode.LEFT -> makeMatrix(tagConfig, textPoints,
                        Point(frameIndex % tagConfig.xResolution - tagConfig.xResolution, 0)
                    )
                    Mode.RIGHT -> makeMatrix(tagConfig, textPoints,
                        Point(textWidth - (frameIndex % textWidth), 0)
                    )
                    Mode.UP -> makeMatrix(tagConfig, textPoints,
                        Point(0, frameIndex % tagConfig.yResolution - tagConfig.yResolution)
                    )
                    Mode.DOWN -> makeMatrix(tagConfig, textPoints,
                        Point(0, tagConfig.yResolution - (frameIndex % tagConfig.yResolution))
                    )
                    Mode.FIXED -> makeMatrix(tagConfig, textPoints)
                    Mode.SNOWFLAKE -> TODO()
                    Mode.PICTURE -> TODO()
                    Mode.ANIMATION -> TODO()
                    Mode.LASER -> TODO()
                }
        }

        if (animationConfig.border) {
            addBorder(tagConfig, currentActive, frameIndex)
        }

        frames.add(renderer.drawFrame(currentActive))
    }

    return frames
}

// compile glyph bytes to point coordinates
private fun pointsFromText(text: String): Set<Point>
{
    val points : MutableSet<Point> = mutableSetOf()

    for ((i, char) in text.withIndex()) {
        val bytes = Glyphs[char] ?: continue
        val xOffset = i * 9

        for ((y, b) in bytes.withIndex()) {
            for (x in 0..7) {
                if ((b.toInt() and (1 shl x)) > 0) {
                    points.add(Point(xOffset + 8 - x, y))
                }
            }
        }
    }

    return points
}

private fun makeMatrix(tagConfig: TagConfig) = BooleanArray(tagConfig.size)

// the offset allows for easy shifting of the matrix, i.e. scrolling effects
private fun makeMatrix(tagConfig: TagConfig, activePoints: Set<Point>, offset: Point = Point(0,0)): BooleanArray {
    val matrix = makeMatrix(tagConfig)

    for (x in 0 until tagConfig.xResolution) {
        for (y in 0 until tagConfig.yResolution) {
            matrix[tagConfig.index(x, y)] = activePoints.contains(Point(offset.x + x, offset.y + y))
        }
    }

    return matrix
}

private fun addBorder(tagConfig: TagConfig, activeLEDs: BooleanArray, frameNumber: Int = 0) {
    val antInterval = 4
    val activeIndex = frameNumber % antInterval

    // Iterate over the indices of border LEDs and activate every <antInterval> one.
    // The indices are already precalculated in the correct counterclockwise order.
    for ((i, bi) in tagConfig.borderIndices.withIndex()) {
        activeLEDs[bi] = (i % antInterval == activeIndex)
    }
}