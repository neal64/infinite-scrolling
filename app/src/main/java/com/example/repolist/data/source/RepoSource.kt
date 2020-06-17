package com.example.repolist.data.source

import androidx.paging.PagingSource
import com.example.repolist.data.model.RepositoryModel
import com.example.repolist.data.retrofit.GitHubServices
import com.example.repolist.utils.APPConstants
import retrofit2.HttpException
import java.io.IOException

/*
Repository Paging source List class
*/

private const val STARTING_PAGE = 1

class GithubRepoSource (private val service: GitHubServices, private val state: String) : PagingSource<Int, RepositoryModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryModel> {

        val position = params.key ?: STARTING_PAGE

        return try {
            val response = service.getGitHubRepositories(state, position,
                APPConstants.PAGE_SIZE
            )
            val repos = response
            LoadResult.Page(
                data = repos,
                prevKey = if (position == STARTING_PAGE) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}