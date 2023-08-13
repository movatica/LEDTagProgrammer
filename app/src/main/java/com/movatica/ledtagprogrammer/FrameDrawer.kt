package com.movatica.ledtagprogrammer

import android.graphics.*
import com.movatica.ledtagprogrammer.extensions.copy

class FrameDrawer
    constructor (
        private val tagConfig: TagConfig
    ) {

    var brightness = 0

    private val borderSize = 32
    private val gridSize = 24
    private val pixelHeight = 5
    private val pixelWidth = 11

    private val paint = Paint().apply {
        strokeCap = Paint.Cap.SQUARE
        strokeWidth = 0f
        style = Paint.Style.FILL_AND_STROKE
        flags = 0
    }

    private val coordinates = Array<Point>(tagConfig.xResolution * tagConfig.yResolution) { index ->
        Point(
            borderSize + gridSize * (index / tagConfig.yResolution) + gridSize / 2,
            borderSize + gridSize * (index % tagConfig.yResolution) + gridSize / 2
        )
    }

    private val background = drawFrameBackground()

    // draw a single LED rectangle on a canvas
    private fun drawLED(canvas: Canvas, paint: Paint, center: Point, width: Int, height: Int)
    {
        val pixelRect = Rect(
            center.x - width,
            center.y - height,
            center.x + width,
            center.y + height
        )

        canvas.save()
        canvas.rotate(-45f, center.x.toFloat(), center.y.toFloat())
        canvas.drawRect(pixelRect, paint)
        canvas.restore()
    }

    /*
    Draw the frame background.
     */
    private fun drawFrameBackground(): Bitmap {
        val imageHeight = borderSize * 2 + gridSize * tagConfig.yResolution
        val imageWidth = borderSize * 2 + gridSize * tagConfig.xResolution

        val bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // draw black rounded rectangle as background
        paint.color = Color.BLACK

        canvas.drawRoundRect(
            0f, imageHeight.toFloat(), imageWidth.toFloat(), 0f,
            borderSize.toFloat(), borderSize.toFloat(),
            paint)

        // draw dark pixels
        paint.color = tagConfig.ledColor.toColor(0)

        for (center in coordinates) {
            drawLED(canvas, paint, center, pixelWidth, pixelHeight)
        }

        return bitmap
    }

    /*
    Overload for trivial case (no active LEDs)
     */
    fun drawFrame(): Bitmap {
        return background.copy()
    }

    /*
    Draw a frame with active LEDs lighted
     */
    fun drawFrame(activeLEDs: BooleanArray): Bitmap {
        val bitmap = background.copy()
        val canvas = Canvas(bitmap)

        // draw active pixels bright
        paint.color = tagConfig.ledColor.toColor(brightness + 1)

        coordinates.forEachIndexed { index, center ->
            if (activeLEDs[index]) {
                drawLED(canvas, paint, center, pixelHeight, pixelHeight)
            }
        }

        return bitmap
    }
}
