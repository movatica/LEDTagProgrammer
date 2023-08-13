package com.movatica.ledtagprogrammer

import java.time.LocalDateTime

class BytecodeGenerator {
}

fun compileBytecode(text: String, speed: Int, mode: Int, blink: Boolean, border: Boolean, brightness: Int) : ByteArray {
    // begin with header
    var payload = compileHeaderBytes(text.length, speed, mode, blink, border, brightness)

    // render pixelated string
    payload += compileText(text)

    // add padding to 64 byte blocks
    payload += ByteArray(64 - payload.size % 64)

    return payload
}

private fun compileText(text: String) : ByteArray {
    var textBytes : ByteArray = byteArrayOf()

    for (char in text) {
        textBytes += Glyphs[char] ?: byteArrayOf()
    }

    return textBytes
}

private fun compileHeaderBytes(length: Int, speed: Int, mode: Int, blink: Boolean, border: Boolean, brightness: Int) : ByteArray {
    // TODO: for now, we only support one message, so everything is set 8 times
    val length = length.coerceIn(1,8192)
    val speed = speed.coerceIn(0,8)
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
    header[6] = (if (blink) 0xFF else 0x00).toByte()

    // border effect
    header[7] = (if (border) 0xFF else 0x00).toByte()

    for (i in 0..7) {
        // speed and mode
        header[8+i] = (16*speed + mode).toByte()
        // length
        header[17+2*i-1] = (length / 256).toByte()
        header[17+2*i] = (length % 256).toByte()
    }

    // current date
    val currentDT = LocalDateTime.now()
    header[38] = (currentDT.year % 100).toByte()
    header[39] = currentDT.monthValue.toByte()
    header[40] = currentDT.dayOfMonth.toByte()
    header[41] = currentDT.hour.toByte()
    header[42] = currentDT.minute.toByte()
    header[43] = currentDT.second.toByte()

    return header
}