package com.example.inventory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.SubCategoryLists
import com.example.inventory.databinding.FragmentGridItemsDesignBinding
import com.example.inventory.databinding.FragmentSubcategoryListBinding

    class SubcategoryListAdapter(
        private var dataset : List<SubCategoryLists>
        ) : RecyclerView.Adapter<SubcategoryListAdapter.SubcategoryItemViewHolder>() {

    class SubcategoryItemViewHolder(val binding: FragmentSubcategoryListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data : SubCategoryLists, position: Int){
            binding.subcategoryName.text = data.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoryItemViewHolder {
        return SubcategoryItemViewHolder(
            FragmentSubcategoryListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SubcategoryItemViewHolder, position: Int) {
//        holder.itemView.layoutParams.width = parentGridSpans * 1100

        when (holder){
            is SubcategoryItemViewHolder -> {
                holder.bind(dataset[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun updateData(newData: List<SubCategoryLists>) {
        dataset = emptyList()
        dataset = newData

        notifyItemRemoved(0)
        notifyItemChanged(0)
        notifyDataSetChanged()
    }
}