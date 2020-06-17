package com.example.repolist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.repolist.R
import kotlinx.android.synthetic.main.repo_footer_layout.view.*

class ReposLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState) = LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: LoadStateViewHolder, loadState: LoadState) = holder.bind(loadState)
}

class LoadStateViewHolder(parent: ViewGroup, retry: () -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.repo_footer_layout, parent, false)
) {
    private val constrainLayout  = itemView.parent_footer
    private val progressBar: ProgressBar = itemView.progressbar
    private val errorMsg = itemView.error
    private val retry: Button = itemView.retrybutton
        .also {
            it.setOnClickListener { retry() }
        }

    fun bind(loadState: LoadState) {
        constrainLayout.visibility = View.VISIBLE
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.localizedMessage
        }

        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = loadState is LoadState.Error
    }
}