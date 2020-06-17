package com.example.repolist.data.model

import com.google.gson.annotations.SerializedName

data class RepositoryModel(
    @SerializedName("description") val description: String,
    @SerializedName("forks_count") val forks_count: Int,
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val full_name: String,
    @SerializedName("language") val language: String,
    @SerializedName("open_issues_count") val open_issues_count: Int,
    @SerializedName("stargazers_count") val stargazers_count: Int
)