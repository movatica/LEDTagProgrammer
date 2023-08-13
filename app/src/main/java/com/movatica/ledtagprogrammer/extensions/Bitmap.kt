package com.movatica.ledtagprogrammer.extensions

import android.graphics.Bitmap

fun Bitmap.copy() = this.copy(this.config, this.isMutable)