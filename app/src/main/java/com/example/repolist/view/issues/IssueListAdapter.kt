package com.example.repolist.view.issues

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.repolist.R
import com.example.repolist.data.model.RepoIssuesModel
import kotlinx.android.synthetic.main.repo_issue_items_list.view.*


class IssueListAdapter : PagingDataAdapter<RepoIssuesModel, RecyclerView.ViewHolder>(COMAPRE_LIST)  {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as MyIssuesViewHolder).bindTo(repoItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_issue_items_list, parent, false)
        return MyIssuesViewHolder(view)
    }

    companion object {
        private val COMAPRE_LIST = object : DiffUtil.ItemCallback<RepoIssuesModel>() {
            override fun areItemsTheSame(
                oldItem: RepoIssuesModel,
                newItem: RepoIssuesModel
            ): Boolean = oldItem.id== newItem.id

            override fun areContentsTheSame(
                oldItem: RepoIssuesModel,
                newItem: RepoIssuesModel
            ): Boolean = oldItem == newItem
        }
    }

    inner class MyIssuesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var repoModel: RepoIssuesModel? = null
        private val txtTitle = itemView.txt_title
        private val txtStatus = itemView.txt_status
        val resources = this.itemView.context.resources
        private val txtNoIssues = itemView.no_issues

        fun bindTo(reposList: RepoIssuesModel?) {
            if (reposList != null) {
                displayRepositoryList(reposList)
                txtNoIssues.visibility = View.GONE
            } else {
                txtTitle.visibility = View.GONE
                txtStatus.visibility = View.GONE
                txtNoIssues.visibility = View.VISIBLE
            }
        }

        private fun displayRepositoryList(repoModel: RepoIssuesModel) {
            this.repoModel = repoModel
            txtTitle.text = repoModel.title
            txtStatus.text = resources.getString(R.string.str_issue_status, repoModel.state)
        }
    }
}