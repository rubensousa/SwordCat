package com.rubensousa.swordcat

import android.app.Application
import coil3.annotation.ExperimentalCoilApi
import com.rubensousa.swordcat.ui.image.CoilInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SwordCatApp : Application() {

    @Inject
    lateinit var coilInitializer: CoilInitializer

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate() {
        super.onCreate()
        coilInitializer.init()
    }
}
