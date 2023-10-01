package com.movatica.ledtagprogrammer.extensions

fun ByteArray.padTo(blockSize: Int)
    = this + ByteArray(blockSize - this.size % blockSize)

fun ByteArray.padTo(blockSize: Int, padding: Byte)
    = this + ByteArray(blockSize - this.size % blockSize) { _ -> padding }