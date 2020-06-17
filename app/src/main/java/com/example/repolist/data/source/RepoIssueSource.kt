package com.example.repolist.data.source


import androidx.paging.PagingSource
import com.example.repolist.data.model.RepoIssuesModel
import com.example.repolist.data.retrofit.GitHubServices
import com.example.repolist.utils.APPConstants
import retrofit2.HttpException
import java.io.IOException

/*
Repository Issues Paging source List class
*/

private const val STARTING_PAGE = 1

class RepoIssueSource(
    private val service: GitHubServices, private val name: String,
    private val state: String
) : PagingSource<Int, RepoIssuesModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoIssuesModel> {

        val position = params.key ?: STARTING_PAGE

        return try {
            val response =
                service.getGitHubRepositoriesIssues(
                    name, state, position,
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