package com.example.inventory.adapters

import Common.Common
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Header
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding
import com.example.inventory.databinding.ParentListItemBinding
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class SampleAdapter(private val onItemClicked: (Item) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list : List<Any> = listOf()

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is Item -> Common.VIEWTYPE_ITEM
            else -> Common.VIEWTYPE_GROUP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Common.VIEWTYPE_GROUP -> HeaderViewHolder(
                ParentListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            Common.VIEWTYPE_ITEM -> ItemViewHolder(
                ItemListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeaderViewHolder -> {
                holder.bind(list[position] as Header)
            }
            is ItemViewHolder -> holder.bind(list[position] as Item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(val binding: ItemListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.itemName.text = item.itemName
            binding.itemPrice.text = item.getFormattedPrice()
            binding.itemQuantity.text = item.quantityInStock.toString()
            binding.root.setOnClickListener { onItemClicked.invoke(item) }
        }

    }

    class HeaderViewHolder(val binding: ParentListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Header) {
//            binding.countryTitle.text = item.country
            val dateFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.UK)
            val dateString = sdf.format(item.date)
            binding.groupDate.text = dateString
        }

    }


}