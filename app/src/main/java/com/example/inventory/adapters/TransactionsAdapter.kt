package com.example.inventory.adapters

import Common.Common
import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.*
import com.example.inventory.databinding.ItemListItem2Binding
import com.example.inventory.databinding.ItemListItemBinding
import com.example.inventory.databinding.NoItemAvailableBinding
import com.example.inventory.databinding.ParentListItemBinding
import com.example.inventory.databinding.TransactionItemListBinding
import java.lang.IllegalArgumentException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class TransactionsAdapter(private val onItemClicked: (TransactionListData) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list : List<Any> = listOf()

    //overridden to return the viewType property of the Item object that given position in your item list
    //This ensures that the correct view type is assigned to each item
    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is TransactionListData -> Common.VIEWTYPE_ITEM
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
                TransactionItemListBinding.inflate(
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeaderViewHolder -> {
                holder.bind(list[position] as Header, position)
            }
            is ItemViewHolder -> holder.bind(list[position] as TransactionListData)
            is NoItemViewHolder -> holder.bind(list[position] as EmptyHeader)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(val binding: TransactionItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(item: TransactionListData) {
            val drawable = ContextCompat.getDrawable(itemView.context, item.imgResourceID)
            binding.itemImageView.setImageDrawable(drawable)
            binding.itemCategory.text = item.itemCategory
            binding.itemCategoryDetail.text = item.description01
            if(item.type == "Expense"){
                binding.itemAmount.text = "-"+"RM" + item.amountValue
                binding.itemAmount.setTextAppearance(R.style.Widget_Inventory_ExpensePriceRed)
            }else if(item.type == "Income"){
                binding.itemAmount.text = "+"+"RM" + item.amountValue
                binding.itemAmount.setTextAppearance(R.style.Widget_Inventory_ExpensePriceGreen)
            }
            binding.root.setOnClickListener { onItemClicked.invoke(item) }
        }
    }

    class HeaderViewHolder(val binding: ParentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private fun declareDecimalValue(amountValue: String): String{
            val transactionAmount = amountValue.toDoubleOrNull()?: 0.0

            val maxDecimalPlaces = amountValue.toBigDecimalOrNull()?.scale() ?: 0

            // Adjust decimal format dynamically
            val decimalFormat = when {
                maxDecimalPlaces == 0 -> DecimalFormat("#")
                maxDecimalPlaces == 1 -> DecimalFormat("#.0")
                else -> DecimalFormat("#.${"0".repeat(maxDecimalPlaces)}") // Ensures dynamic precision
            }
            val formattedPrice = decimalFormat.format(transactionAmount)
            return (formattedPrice ?: 0).toString()
        }

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
            val sumIncome = item.sumIncome.toDouble()
            val sumExpense = item.sumExpense.toDouble()
            binding.sumIncome.text = "RM"+declareDecimalValue(item.sumIncome)
            binding.sumExpense.text = "RM"+declareDecimalValue(item.sumExpense)
//            binding.sumIncome.text = if (sumIncome > 0) {
//                "RM ${String.format(sumIncome.toString())}"
//            } else if ((sumIncome) % 1 == 0.0) {
//                "RM ${sumIncome.toInt()}"
//            } else {
//                "- RM ${String.format(abs(sumIncome).toString())}"
//            }
//
//            binding.sumExpense.text = if (sumExpense > 0) {
//                "RM ${String.format(sumExpense.toString())}"
//            } else if ((sumExpense) % 1 == 0.0) {
//                "RM ${sumExpense.toInt()}"
//            } else {
//                "- RM ${String.format(abs(sumExpense).toString())}"
//            }

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