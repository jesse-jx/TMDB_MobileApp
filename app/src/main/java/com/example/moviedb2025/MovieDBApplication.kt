package com.example.moviedb2025

import android.app.Application
import com.example.moviedb2025.database.AppContainer
import com.example.moviedb2025.database.DefaultAppContainer

class MovieDBApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}