package com.movatica.ledtagprogrammer

import com.movatica.ledtagprogrammer.extensions.padTo
import java.time.LocalDateTime

class BytecodeGenerator {
    // TODO: implement a class for this
}

fun compileBytecode(animationConfig: AnimationConfig, brightness: Int) : ByteArray {
    // TODO: support all 8 slots
    // begin with header
    var payload = compileHeaderBytes(animationConfig, brightness)

    // render pixelated string
    payload += compileText(animationConfig.text)

    // add padding to 64 byte blocks
    payload = payload.padTo(64)

    return payload
}

private fun compileText(text: String) : ByteArray {
    // TODO: support 48x12 displays
    var textBytes : ByteArray = byteArrayOf()

    for (char in text) {
        textBytes += Glyphs[char] ?: byteArrayOf()
    }

    return textBytes
}

private fun currentDateToBytes() : ByteArray {
    val currentDT = LocalDateTime.now()

    return byteArrayOf(
        (currentDT.year % 100).toByte(),
        currentDT.monthValue.toByte(),
        currentDT.dayOfMonth.toByte(),
        currentDT.hour.toByte(),
        currentDT.minute.toByte(),
        currentDT.second.toByte())
}

private fun compileHeaderBytes(animationConfig: AnimationConfig, brightness: Int) : ByteArray {
    // TODO: support all 8 slots
    val length = animationConfig.text.length.coerceIn(1,8192)
    val speed = animationConfig.speed.coerceIn(0,8)
    val mode = animationConfig.mode.toInt()
    val brightness = brightness.coerceIn(0,3)

    val header = byteArrayOf(
        0x77, 0x61, 0x6e, 0x67, 0x00, 0x00, 0x00, 0x00, 0x40, 0x40,
        0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00)

    // brightness
    header[5] = when(brightness) {
        0 -> 0x40.toByte()
        1 -> 0x20.toByte()
        2 -> 0x10.toByte()
        else -> 0x00.toByte()
    }

    // blink effect
    header[6] = (if (animationConfig.flash) 0xFF else 0x00).toByte()

    // border effect
    header[7] = (if (animationConfig.border) 0xFF else 0x00).toByte()

    for (i in 1..7) {
        // speed and mode
        header[8+i] = (16*speed + mode).toByte()
        // length
        //header[17+2*i-1] = (length / 256).toByte()
        //header[17+2*i] = (length % 256).toByte()
    }
    // length
    header[18] = (length / 256).toByte()
    header[19] = (length % 256).toByte()

    currentDateToBytes().copyInto(header, 38, 0, 5)

    return header
}