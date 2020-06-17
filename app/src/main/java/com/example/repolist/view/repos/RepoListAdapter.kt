package com.example.repolist.view.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.repolist.data.model.RepositoryModel
import kotlinx.android.synthetic.main.repo_item_list.view.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.repolist.R
import com.example.repolist.utils.APPConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
@ExperimentalCoroutinesApi



class RepoListAdapter : PagingDataAdapter<RepositoryModel, ViewHolder>(
    COMAPRE_LIST
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as MyViewHolder).bindTo(repoItem)
        }
    }

    companion object {
        private val COMAPRE_LIST = object : DiffUtil.ItemCallback<RepositoryModel>() {
            override fun areItemsTheSame(
                oldItem: RepositoryModel,
                newItem: RepositoryModel
            ): Boolean = oldItem.full_name == newItem.full_name

            override fun areContentsTheSame(
                oldItem: RepositoryModel,
                newItem: RepositoryModel
            ): Boolean = oldItem == newItem
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var repoModel: RepositoryModel? = null
        private val txtRepoName = itemView.repo_name
        private val txtDesc = itemView.repo_desc
        private val txtForksCount = itemView.forks_count
        private val txtLangType = itemView.language_type
        private val txtstargazers = itemView.stargazers_count
        private val txtIssueCount = itemView.open_issue_count
        private val constraintLayout = itemView.repo_constraint_layout
        val resources = this.itemView.context.resources

        fun bindTo(reposList: RepositoryModel?) {
            if (reposList != null)
                displayRepositoryList(reposList) else
                constraintLayout.visibility = View.GONE
        }


        private fun displayRepositoryList(repoModel: RepositoryModel) {
            this.repoModel = repoModel
            txtRepoName.text = repoModel.full_name

            if(repoModel.description != null){
                txtDesc.visibility = View.VISIBLE
                txtDesc.text = repoModel.description
            }else txtDesc.visibility = View.GONE


            txtstargazers.text = repoModel.stargazers_count.toString()
            txtForksCount.text = repoModel.forks_count.toString()
            txtIssueCount.text = resources.getString(R.string.str_open_issues,repoModel.open_issues_count.toString())

            if(repoModel.language != null) {
                txtLangType.text = repoModel.language
            }else txtLangType.text = resources.getString(R.string.str_lang)


            constraintLayout.also {
                it.setOnClickListener {
                        view ->
                    val bundle = bundleOf(APPConstants.NAME to repoModel.name)
                        view.findNavController()
                            .navigate(R.id.action_homeFragment_to_issuesFragment, bundle)
                }
            }
        }
    }
}




