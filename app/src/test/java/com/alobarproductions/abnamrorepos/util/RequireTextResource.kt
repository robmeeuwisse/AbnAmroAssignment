package com.alobarproductions.abnamrorepos.util

internal fun Any.requireTextResource(name: String): String =
    checkNotNull(javaClass.getResource(name)) { "Missing Java resource file '$name'" }.readText()
