package com.example.inventory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.DragStartHelper
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.*
import com.example.inventory.databinding.FragmentExpenseCategorySettingBinding
import com.example.inventory.databinding.FragmentSubCategoryBinding
import java.util.*

class SubCategoryAdapter(
    private val subListData: List<SubCategoryData>,
    private val context: Context,
    private val onIconClicked: (SubCategoryData) -> Unit,
    private val onRemoveClicked: (SubCategoryData) -> Unit,
    private val onDragClicked: (RecyclerView.ViewHolder) -> Unit): RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolders>(), ItemTouchHelperAdapter {


    inner class SubCategoryViewHolders(val binding:FragmentSubCategoryBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: SubCategoryData, position: Int, holder: SubCategoryViewHolders){
            val drawable = ContextCompat.getDrawable(itemView.context, item.imageResourceId)
            binding.iconView.setImageDrawable(drawable)
            binding.expenseCategoryName.text = item.description01


            binding.root.setOnClickListener {
                onIconClicked.invoke(item)
            }

            binding.iconRemove.setOnClickListener {
                onRemoveClicked.invoke(item)
            }

//            binding.imageDrag.setOnClickListener {
//                // Handle icon click
//                onDragClicked(this)
//            }

            binding.imageDrag.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    onDragClicked(this)
//                    touchHelper?.startDrag(this)
                }
                false
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryAdapter.SubCategoryViewHolders {
        return SubCategoryViewHolders(
            FragmentSubCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolders, position: Int) {
        when (holder){
            is SubCategoryViewHolders ->{
                holder.bind(subListData[position], position, holder)
            }
        }
    }

    override fun getItemCount(): Int {
       return subListData.size
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(subListData, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(subListData, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}