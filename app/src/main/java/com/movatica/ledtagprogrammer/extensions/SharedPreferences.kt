package com.movatica.ledtagprogrammer.extensions

import android.content.SharedPreferences

fun SharedPreferences.getString(key: String) = this.getString(key, null)