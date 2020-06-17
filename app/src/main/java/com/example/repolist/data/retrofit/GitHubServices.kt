package com.example.repolist.data.retrofit

import com.example.repolist.data.model.RepoIssuesModel
import com.example.repolist.data.model.RepositoryModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
Retrofit APIs interface class
*/

interface GitHubServices {
    @GET("orgs/google/repos")
    suspend fun getGitHubRepositories(@Query("type") type : String, @Query("page") page : Int, @Query("per_page") pageSize : Int) : List<RepositoryModel>

    @GET("repos/google/{name}/issues")
    suspend fun getGitHubRepositoriesIssues(@Path("name") name : String, @Query("state") state: String, @Query("page") page : Int, @Query("per_page") pageSize : Int) : List<RepoIssuesModel>

}

