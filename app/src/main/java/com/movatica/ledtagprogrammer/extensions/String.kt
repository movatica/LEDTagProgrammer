package com.movatica.ledtagprogrammer.extensions

fun String.titlecase() = lowercase().replaceFirstChar { it.titlecase() }