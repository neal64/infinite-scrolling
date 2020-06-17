package com.example.repolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.repolist.data.GithubServiceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi

/*
ViewModel Factory class
*/

class ViewModelFactory (private val repository: GithubServiceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepoViewModel::class.java)) {
            return RepoViewModel(repository) as T
        }
        throw IllegalArgumentException("Class is not known")
    }
}
