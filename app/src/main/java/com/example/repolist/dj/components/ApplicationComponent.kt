package com.example.repolist.dj.components

import androidx.paging.ExperimentalPagingApi
import com.example.repolist.BaseApp
import com.example.repolist.dj.modules.AppModule
import com.example.repolist.dj.modules.NetworkingModule
import com.example.repolist.view.issues.IssueFragment
import com.example.repolist.view.repos.HomeFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton
@ExperimentalCoroutinesApi
@ExperimentalPagingApi

/*
Application Component Dependency Injection
*/

@Singleton
@Component(modules = [NetworkingModule::class, AppModule::class])
interface ApplicationComponent {

    fun inject(baseApp: BaseApp)

    fun inject(homeFragment: HomeFragment)

    fun inject(issueFragment: IssueFragment)

}