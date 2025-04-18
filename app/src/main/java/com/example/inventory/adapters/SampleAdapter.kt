package com.example.inventory.adapters

import Common.Common
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.EmptyHeader
import com.example.inventory.data.Header
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding
import com.example.inventory.databinding.NoItemAvailableBinding
import com.example.inventory.databinding.ParentListItemBinding
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

class SampleAdapter(private val onItemClicked: (Item) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list : List<Any> = listOf()

    //overridden to return the viewType property of the Item object that given position in your item list
    //This ensures that the correct view type is assigned to each item
    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is Item -> Common.VIEWTYPE_ITEM
            is Header -> Common.VIEWTYPE_GROUP
            else -> Common.VIEWTPYE_NO_DATA
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
            Common.VIEWTPYE_NO_DATA -> NoItemViewHolder(
                NoItemAvailableBinding.inflate(
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
                holder.bind(list[position] as Header, position)
            }
            is ItemViewHolder -> holder.bind(list[position] as Item)
            is NoItemViewHolder -> holder.bind(list[position] as EmptyHeader)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(val binding: ItemListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.itemName.text = item.itemName
            binding.itemPrice.text = item.getFormattedPrice()
            binding.itemUnit.text = item.quantityInStock.toString()
            binding.root.setOnClickListener { onItemClicked.invoke(item) }
        }
    }

    /**
     * Display the header
     * */
    class HeaderViewHolder(val binding: ParentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Header, position: Int) {
//            binding.countryTitle.text = item.country
            val dateFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.UK)
            val dateString = sdf.format(item.date)

            val dateComponents = dateString.split("/")
            val day = dateComponents[0]
            val monthYear = dateComponents[1] + "/" + dateComponents[2]
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.UK).format(item.date)

            binding.textDay.text = day
//            binding.groupDate.text = monthYear
            binding.textNameDays.text = displayDayName(dayOfWeek)
            binding.sumExpense.text = item.sumExpense;
            binding.sumIncome.text = item.sumIncome;
        }

        private fun displayDayName(dayOfWeek: String): String{
            return when (dayOfWeek){
                "Monday" -> "Mon"
                "Tuesday" -> "Tue"
                "Wednesday" -> "Wed"
                "Thursday" -> "Thu"
                "Friday" -> "Fri"
                "Saturday" -> "Sat"
                "Sunday" -> "Sun"
                else -> "Invalid"
            }
        }
    }

    class NoItemViewHolder(val binding: NoItemAvailableBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EmptyHeader) {
            binding.messageNoItem.text = item.message
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val marginTopInPixels = recyclerView.context.resources.getDimensionPixelSize(R.dimen.margin_top_header) // Replace with your desired margin size in pixels
         val itemDecoration = TopMarginItemDecoration(marginTopInPixels)
        recyclerView.addItemDecoration(itemDecoration)
    }

    class TopMarginItemDecoration(private val marginTop: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val adapter = parent.adapter ?: return

            // Check if the position is the second header (assuming you want to add margin before the second header)
            if (position > 0 && adapter.getItemViewType(position) ==  Common.VIEWTYPE_GROUP) {
                outRect.top = marginTop
            }
        }
    }

}