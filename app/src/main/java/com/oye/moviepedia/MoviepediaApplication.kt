package com.oye.moviepedia

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviepediaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        setupCrashlytics()
    }

    private fun setupCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }

}