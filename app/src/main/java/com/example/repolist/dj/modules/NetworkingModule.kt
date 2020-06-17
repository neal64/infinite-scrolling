package com.example.repolist.dj.modules

import androidx.lifecycle.ViewModelProvider
import com.example.repolist.data.GithubServiceRepository
import com.example.repolist.data.retrofit.GitHubServices
import com.example.repolist.utils.APPConstants
import com.example.repolist.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
@ExperimentalCoroutinesApi

/*
RetrofitModule Module for Dependency Injection
*/

@Module
class NetworkingModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = Level.BASIC
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideOKHttpClient(httpLoggingInterceptor : HttpLoggingInterceptor ): OkHttpClient {
        return  OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGSON(): GsonConverterFactory {
        return  GsonConverterFactory.create()

    }
    @Singleton
    @Provides
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(APPConstants.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideGithubService(retrofit: Retrofit) : GitHubServices{
        return retrofit.create(GitHubServices::class.java)
    }

    @Provides
    fun provideGithubRepository(gitHubServices: GitHubServices): GithubServiceRepository {
        return GithubServiceRepository(gitHubServices)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(githubServiceRepository: GithubServiceRepository): ViewModelProvider.Factory {
        return ViewModelFactory(githubServiceRepository)
    }

}
