package com.example.repolist.dj.modules

import androidx.paging.ExperimentalPagingApi
import com.example.repolist.BaseApp
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton
@ExperimentalPagingApi
@ExperimentalCoroutinesApi

/*
Application Module for Dependency Injection
*/

@Module
class AppModule (private val app: BaseApp) {

    @Provides
    @Singleton
    fun provideContext() = app
}