/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory.adapters

import Common.Common
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding
import com.example.inventory.databinding.ParentListItemBinding
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

/**
 * [ListAdapter] implementation for the recyclerview.
 */

//class ItemListAdapter(private val onItemClicked: (Item) -> Unit) : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {
class ItemListAdapter(private val onItemClicked: (Item) -> Unit) :
/**
 *  Add extend ListAdapter. Pass in the Item and ItemListAdapter.ItemViewHolder as parameters
 * Add the constructor parameter DiffCallback; the ListAdapter will use this to figure out what changed in the list.
 * */
    ListAdapter<Item, RecyclerView.ViewHolder>(DiffCallback) {

    class GroupItemViewHolder(private var binding: ParentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun groupBind(item: Item) {
//            binding.countryTitle.text = item.country
            val dateFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.UK)
            val dateString = sdf.format(item.date)
//            binding.groupDate.text = dateString
        }
    }

    class ItemViewHolder(private var binding: ItemListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun detailBind(item: Item) {
            binding.itemName.text = item.itemName
            binding.itemPrice.text = item.getFormattedPrice()
            binding.itemQuantity.text = item.quantityInStock.toString()
        }
    }
//    class ItemViewHolder(private var binding: ItemListItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: Item) {
//            binding.itemName.text = item.itemName
//            binding.itemPrice.text = item.getFormattedPrice()
//            binding.itemQuantity.text = item.quantityInStock.toString()
//        }
//    }

    /**############################ onCreateViewHolder ############################**/
    /**
     * onCreateViewHolder() method returns a new ViewHolder when RecyclerView needs one.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Common.VIEWTYPE_GROUP -> GroupItemViewHolder(ParentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            Common.VIEWTYPE_ITEM -> ItemViewHolder(ItemListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        return ItemViewHolder(
//            ItemListItemBinding.inflate(LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
    /**############################ end onCreateViewHolder ############################**/

    /**############################ onBindViewHolder ############################**/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            if(current.id >= 0){
                onItemClicked(current)
            }
        }
        when(holder){
            is GroupItemViewHolder -> holder.groupBind(current as Item)
            is ItemViewHolder -> holder.detailBind(current as Item)
        }
    }
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val current = getItem(position)
//        holder.itemView.setOnClickListener {
//            onItemClicked(current)
//        }
//        holder.bind(current)
//    }
    /**############################ end onBindViewHolder ############################**/

    override fun getItemViewType(position: Int): Int {
        return currentList[position].viewType
    }

    override fun getItemCount(): Int = currentList.size

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {

            /**
             * Called by the DiffUtil to decide whether two object represent the same item.
             * For example, if your item have unique ids, this method should check id equality
             */
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return when {
                    oldItem.id === newItem.id -> { true }
                    else -> { true }
                }
            }


            /**
             * Called by the DiffUtil when it wants to check whether two items have the same data.
             * DiffUtil uses this information to detect if the contents of an item has changed.
             * DiffUtil uses this method to check equality instead of Object.equals(Object),
             * so that you can change its behavior depending on your UI.
             *
             * For example, if your are using DiffUtil with a RecycleView.Adapter, you should return
             * whether the items' visual representations are the same.
             *
             * This method is called only if areItemsTheSame return true for these
             *
             */
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return when {
                    oldItem.itemName != newItem.itemName -> { false }
                    oldItem.itemPrice != newItem.itemPrice -> { false }
                    oldItem.quantityInStock != newItem.quantityInStock -> { false }
                    else -> true
                }
            }
        }
    }
}
