package com.movatica.ledtagprogrammer

data class AnimationConfig (
    var text: String = "Hi!",
    var speed: Int = 1,
    var mode: Mode = Mode.FIXED,
    var flash: Boolean = false,
    var border: Boolean = false
    )