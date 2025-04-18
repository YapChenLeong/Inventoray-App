package com.example.inventory.adapters

import Common.Common
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.*
import com.example.inventory.databinding.FragmentExpenseCategorySetting2Binding
import com.example.inventory.databinding.FragmentExpenseCategorySettingBinding
import java.lang.IllegalArgumentException
import java.util.*


class CategorySettingAdapter(
    private val dataset: List<CategorySettings>,
    private val context: Context,
    private val onItemClicked: (CategorySettings, Any?) -> Unit,
    private val onRemoveClicked: (CategorySettings) -> Unit,
    private val onDragClicked: (RecyclerView.ViewHolder, String) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    inner class CategoryCategoryViewHolders(var binding: FragmentExpenseCategorySettingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: CategorySettings){
            val drawable = ContextCompat.getDrawable(itemView.context, item.imageResourceId)
            binding.iconView.setImageDrawable(drawable)
            if(item.mList.isNullOrEmpty()){
                binding.expenseCategoryName.text = item.title
            }

            binding.root.setOnClickListener {
                onItemClicked.invoke(item, binding.expenseCategoryName.text)
            }

            binding.imageEdit.setOnClickListener {
                onItemClicked.invoke(item, "imageClicked")
            }

            binding.iconRemove.setOnClickListener {
                onRemoveClicked.invoke(item)
            }

            binding.imageDrag.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    onDragClicked(this, item.expenseId)
//                    touchHelper?.startDrag(this)
                }
                false
            }
        }
    }

    inner class CategoryCategoryViewHolders2(var binding: FragmentExpenseCategorySetting2Binding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: CategorySettings){
            val drawable = ContextCompat.getDrawable(itemView.context, item.imageResourceId)
            binding.iconView.setImageDrawable(drawable)
            if(item.mList.isNullOrEmpty()){
                binding.expenseCategoryName.text = item.title

            }else{
                binding.expenseCategoryName.text = item.title + " (" + item.mList.size + ")"
                val displayString = item.mList.joinToString(", ") { it.description!! }
                binding.expenseSubCategoryName.text = displayString
            }

            binding.root.setOnClickListener {
                onItemClicked.invoke(item, binding.expenseCategoryName.text)
            }

            binding.imageEdit.setOnClickListener {
                onItemClicked.invoke(item, "imageClicked")
            }

            binding.iconRemove.setOnClickListener {
                onRemoveClicked.invoke(item)
            }

            binding.imageDrag.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    onDragClicked(this, item.expenseId)
//                    touchHelper?.startDrag(this)
                }
                false
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(dataset[position].mList.isNullOrEmpty()){
            Common.VIEWTYPE_GROUP
        }else{
            Common.VIEWTYPE_ITEM
        }
//
//        return when (dataset[position].mList) {
//            is HeaderCategory -> Common.VIEWTYPE_GROUP
//            is BodySubCategory -> Common.VIEWTYPE_ITEM
//            else -> Common.VIEWTPYE_NO_DATA
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType){
            Common.VIEWTYPE_GROUP -> CategoryCategoryViewHolders(
                FragmentExpenseCategorySettingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            Common.VIEWTYPE_ITEM -> CategoryCategoryViewHolders2(
                FragmentExpenseCategorySetting2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is CategoryCategoryViewHolders ->{
                holder.bind(dataset[position])
//                holder.itemView.setOnClickListener {
//                    onItemClicked(dataset[position])
//                }
            }
            is CategoryCategoryViewHolders2 ->{
                holder.bind(dataset[position])
            }
        }
    }

    override fun getItemCount(): Int {
       return dataset.size
    }

    fun getItemAtPosition(position: Int): CategorySettings {
        return dataset[position]
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dataset, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dataset, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }



}