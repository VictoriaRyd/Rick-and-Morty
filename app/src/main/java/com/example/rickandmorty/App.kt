package com.example.rickandmorty

import android.app.Application
import android.content.Context
import com.example.rickandmorty.di.AppComponent
import com.example.rickandmorty.di.AppModule
import com.example.rickandmorty.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context = this))
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }