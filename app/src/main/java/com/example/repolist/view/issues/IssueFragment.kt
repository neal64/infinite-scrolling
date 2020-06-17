package com.example.repolist.view.issues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.repolist.R
import com.example.repolist.data.retrofit.GitHubServices
import com.example.repolist.dj.components.ApplicationComponent
import com.example.repolist.dj.components.DaggerApplicationComponent
import com.example.repolist.dj.modules.NetworkingModule
import com.example.repolist.utils.APPConstants
import com.example.repolist.view.ReposLoadStateAdapter
import com.example.repolist.viewmodel.RepoViewModel
import kotlinx.android.synthetic.main.fragment_issues.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("DEPRECATION")
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@OptIn(InternalCoroutinesApi::class)


/*
Repository IssueList Fragment
*/

class IssueFragment : Fragment() {
    private lateinit var root: View
    private val mAdapter = IssueListAdapter()
    private var loadIssuesJob: Job? = null
    lateinit var applicationComponent: ApplicationComponent
    lateinit var mViewModel: RepoViewModel

    @Inject
    lateinit var mViewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var githubService: GitHubServices

    private var mName: String? = null
    private lateinit var state: String
    private lateinit var rv: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var try_again_button: Button
    private lateinit var actionBarSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectDependency()
        root = inflater.inflate(R.layout.fragment_issues, container, false)
        initView()
        return root
    }


    private fun initView() {
        mViewModel = ViewModelProvider(this, mViewModelProvider).get(RepoViewModel::class.java)
        mName = arguments?.getString(APPConstants.NAME)
        actionBarSpinner = root.action_bar_spinner
        rv = root.issuesRv
        progress = root.progress
        try_again_button = root.try_again_button
        actionBarSpinner.adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.statesArray)
            )
        }
        actionBarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Default ALl state call
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                state = when (p2) {
                    0 -> APPConstants.ALL_STATE
                    1 -> APPConstants.OPEN_STATE
                    else -> APPConstants.CLOSE_STATE
                }
                requestRepo()
            }
        }
        initAdapter()

        lifecycleScope.launch {
            mAdapter.dataRefreshFlow.collect {
                rv.scrollToPosition(0)
            }
        }
        root.try_again_button.setOnClickListener { mAdapter.retry() }
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
        loadIssuesJob?.cancel()
        loadIssuesJob = lifecycleScope.launch {
            mViewModel.repositoryIssueList(mName!!, state).collectLatest { it ->
                mAdapter.submitData(it)
            }
        }
    }


    private fun injectDependency() {
        applicationComponent = DaggerApplicationComponent.builder()
            .networkingModule(NetworkingModule())
            .build()
        applicationComponent.inject(this)
    }

}