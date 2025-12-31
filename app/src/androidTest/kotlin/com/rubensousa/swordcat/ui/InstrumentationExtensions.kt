package com.rubensousa.swordcat.ui

import androidx.test.platform.app.InstrumentationRegistry

fun getString(id: Int): String {
    return InstrumentationRegistry.getInstrumentation().targetContext.getString(id)
}
