@file:Suppress("DEPRECATION")

package com.example.repolist.view.repos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.repolist.R
import com.example.repolist.dj.components.DaggerApplicationComponent
import com.example.repolist.dj.modules.NetworkingModule
import com.example.repolist.view.ReposLoadStateAdapter
import com.example.repolist.viewmodel.RepoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@ExperimentalPagingApi
@ExperimentalCoroutinesApi


/*
Repository Repository List Fragment
*/
class HomeFragment : Fragment() {
    private lateinit var root: View
    private val mAdapter = RepoListAdapter()
    private var loadDataJob: Job? = null
    private lateinit var rv: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var try_again_button: Button
    lateinit var mViewModel: RepoViewModel

    @Inject
    lateinit var mViewModelProvider : ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        injectDependency()
        initView()
        return root
    }

    private fun initView() {
        rv = root.findViewById(R.id.rv)
        progress = root.findViewById(R.id.progress)
        try_again_button = root.findViewById(R.id.try_again_button)
        mViewModel = ViewModelProvider(this, mViewModelProvider).get(RepoViewModel::class.java)
        initAdapter()
        requestRepo()
        lifecycleScope.launch {
            mAdapter.dataRefreshFlow.collect {
                rv.scrollToPosition(0)
            }
        }
        try_again_button.setOnClickListener { mAdapter.retry() }
    }

    private fun initAdapter() {
        rv.adapter = mAdapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter(mAdapter::retry),
            footer = ReposLoadStateAdapter(mAdapter::retry)
        )
        mAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh !is LoadState.NotLoading) {
                rv.visibility = View.GONE
                progress.isVisible = loadState.refresh is LoadState.Loading
                try_again_button.isVisible = loadState.refresh is LoadState.Error
            } else {
                rv.visibility = View.VISIBLE
                progress.visibility = View.GONE
                try_again_button.visibility = View.GONE
                val errorState = when {
                    loadState.append is LoadState.Error -> {
                        loadState.append as LoadState.Error
                    }
                    loadState.prepend is LoadState.Error -> {
                        loadState.prepend as LoadState.Error
                    }
                    else -> {
                        null
                    }
                }
                errorState?.let {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.str_req_error, "${it.error}"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun requestRepo() {
        loadDataJob?.cancel()
        loadDataJob = lifecycleScope.launch {
            mViewModel.repositoryDataResult().collectLatest { pagingData ->
                mAdapter.submitData(pagingData)
            }
        }
    }

    private fun injectDependency() {
        val listComponent = DaggerApplicationComponent.builder()
            .networkingModule(NetworkingModule())
            .build()
        listComponent.inject(this)
    }
}
