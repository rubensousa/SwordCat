package com.rubensousa.swordcat.ui

import androidx.test.platform.app.InstrumentationRegistry

fun getString(id: Int, vararg args: Any): String {
    return InstrumentationRegistry.getInstrumentation().targetContext.getString(id, *args)
}
