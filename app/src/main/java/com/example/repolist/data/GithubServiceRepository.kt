package com.example.repolist.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.repolist.data.model.RepoIssuesModel
import com.example.repolist.data.model.RepositoryModel
import com.example.repolist.data.retrofit.GitHubServices
import com.example.repolist.data.source.RepoIssueSource
import com.example.repolist.data.source.GithubRepoSource
import com.example.repolist.utils.APPConstants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
@ExperimentalCoroutinesApi

/*
GithubService Repository class
* */

class GithubServiceRepository(private val services: GitHubServices) {

    fun getGitRepos(): Flow<PagingData<RepositoryModel>> {
        return Pager(
            config = PagingConfig(pageSize = APPConstants.PAGE_SIZE),
            pagingSourceFactory = {
                GithubRepoSource(
                    services,
                    APPConstants.ALL_STATE
                )
            }
        ).flow
    }

    fun getGitReposIssues(name: String, state: String): Flow<PagingData<RepoIssuesModel>> {
        return Pager(
            config = PagingConfig(pageSize = APPConstants.PAGE_SIZE),
            pagingSourceFactory = {
                RepoIssueSource(
                    services,
                    name,
                    state
                )
            }
        ).flow
    }

}