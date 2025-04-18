package com.example.inventory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.CategoryLists
import com.example.inventory.data.CategoryType
import com.example.inventory.data.ExpenseItems

class CategoryChildAdapter(val allItemIcons: List<CategoryLists>, private val onIconClicked: (Int) -> Unit):RecyclerView.Adapter<CategoryChildAdapter.CategoryChildViewHolders>(){
    inner class CategoryChildViewHolders(itemView: View): RecyclerView.ViewHolder(itemView){

        val icon: ImageView = itemView.findViewById(R.id.iconView)

        fun bind(listIcons: CategoryLists) {
            val drawable = ContextCompat.getDrawable(itemView.context, listIcons.iconResourceId)
            icon.setImageDrawable(drawable)

            icon.setOnClickListener {
                onIconClicked(listIcons.iconResourceId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryChildViewHolders {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_icons_gridlayout,parent,false)
        return CategoryChildViewHolders(view)
    }

    override fun onBindViewHolder(holder: CategoryChildAdapter.CategoryChildViewHolders, position: Int) {
        holder.bind(allItemIcons[position])
    }

    override fun getItemCount(): Int {
        return allItemIcons.size
    }
}