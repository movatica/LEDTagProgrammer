package com.movatica.ledtagprogrammer

import com.movatica.ledtagprogrammer.extensions.padTo
import java.time.LocalDateTime

class BytecodeGenerator {
}

fun compileBytecode(text: String, speed: Int, mode: Int, blink: Boolean, border: Boolean, brightness: Int) : ByteArray {
    // begin with header
    var payload = compileHeaderBytes(text.length, speed, mode, blink, border, brightness)

    // render pixelated string
    payload += compileText(text)

    // add padding to 64 byte blocks
    payload = payload.padTo(64)

    return payload
}

private fun compileText(text: String) : ByteArray {
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

    currentDateToBytes().copyInto(header, 38)

    return header
}