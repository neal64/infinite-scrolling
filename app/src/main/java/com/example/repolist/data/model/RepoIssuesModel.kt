package com.example.repolist.data.model

import com.google.gson.annotations.SerializedName

data class RepoIssuesModel(
    @SerializedName("body") val body: String,
    @SerializedName("id") val id: Int,
    @SerializedName("state") val state: String,
    @SerializedName("title") val title: String
)