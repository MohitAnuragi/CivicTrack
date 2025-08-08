package com.example.civictrack

import android.app.Application
import com.mapmyindia.sdk.maps.MapmyIndia
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CivicTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapmyIndia.getInstance(this)
    }
}