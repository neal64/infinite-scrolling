package com.example.repolist.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.repolist.data.GithubServiceRepository
import com.example.repolist.data.model.RepositoryModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import androidx.paging.cachedIn
import com.example.repolist.data.model.RepoIssuesModel
@ExperimentalCoroutinesApi

class RepoViewModel (val repository: GithubServiceRepository) : ViewModel() {

    fun repositoryDataResult(): Flow<PagingData<RepositoryModel>> {
        return repository.getGitRepos().cachedIn(viewModelScope)
    }

    fun repositoryIssueList(name : String, state: String): Flow<PagingData<RepoIssuesModel>> {
        return repository.getGitReposIssues(name, state).cachedIn(viewModelScope)
    }

}