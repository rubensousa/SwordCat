package com.rubensousa.swordcat.app

import android.app.Application
import coil3.annotation.ExperimentalCoilApi
import com.rubensousa.swordcat.BuildConfig
import com.rubensousa.swordcat.ui.image.CoilInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SwordCatApp : Application() {

    @Inject
    lateinit var coilInitializer: CoilInitializer

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate() {
        super.onCreate()
        coilInitializer.init()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
