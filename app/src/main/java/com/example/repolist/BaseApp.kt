package com.example.repolist

import android.app.Application
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.repolist.dj.components.ApplicationComponent
import com.example.repolist.dj.components.DaggerApplicationComponent
import com.example.repolist.dj.modules.AppModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
@Suppress("DEPRECATION")
@ExperimentalPagingApi
@ExperimentalCoroutinesApi

/*
Base Application for Decadency Injection
*/

class BaseApp: Application() {

    companion object {
        var ctx: Context? = null
        lateinit var applicationComponent: ApplicationComponent
    }
    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        applicationComponent = initDaggerComponent()
    }

    fun initDaggerComponent():ApplicationComponent{
        applicationComponent =   DaggerApplicationComponent
            .builder()
            .appModule(AppModule(this))
            .build()
        return  applicationComponent
    }
}