package com.example.inventory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.CategoryLists
import com.example.inventory.data.CategoryType
import com.example.inventory.data.ExpenseItems

class CategoryNameParentAdapter(
    private val dataset: List<CategoryType>,
    private val onIconClicked: (Int) -> Unit
    ) : RecyclerView.Adapter<CategoryNameParentAdapter.CategoryParentViewHolders>() {

    inner class CategoryParentViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleCategory: TextView = itemView.findViewById(R.id.titleCategory)
        val iconsRecView: RecyclerView = itemView.findViewById(R.id.grid_icons_recycle_view)

        fun bind(item: CategoryType) {
            titleCategory.text = item.title
            val childMembersAdapter = CategoryChildAdapter(item.mList){ iconResourceId ->
                onIconClicked(iconResourceId)
            }
            iconsRecView.layoutManager = GridLayoutManager(itemView.context,5)
            iconsRecView.adapter = childMembersAdapter

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryParentViewHolders {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_new_category, parent, false)
        return CategoryParentViewHolders(view)
    }

    override fun onBindViewHolder(holder: CategoryParentViewHolders, position: Int) {
        when (holder){
            is CategoryParentViewHolders ->{
                holder.bind(dataset[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}