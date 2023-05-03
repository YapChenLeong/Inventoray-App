package com.example.inventory.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.Item
import java.time.LocalDate
import java.util.*

class CalendarChildAdapter(
    var date: String,
    transactionData: List<Any>,
    var localDate: LocalDate?

) : RecyclerView.Adapter<CalendarChildAdapter.CalendarChildViewHolder>() {

    private var rowData: List<Any> = listOf()

    init {
        this.rowData = transactionData
    }

    //    var onItemClick: ((String) -> Unit)? = null
    inner class CalendarChildViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cellData = view.findViewById<TextView>(R.id.data_row)
        @RequiresApi(Build.VERSION_CODES.O)
        fun binding(rowData: Item) {
            var cal = Calendar.getInstance()
            cal.time = rowData.date
            var dataDay = cal[Calendar.DAY_OF_MONTH]
            var dataYear = cal[Calendar.YEAR]
            var dataMonth = cal[Calendar.MONTH] + 1

            localDate?.let {
                var calendarYear = it.year
                var calendarMonth = it.monthValue

                if(date != ""){
                    if(dataDay == date.toInt() && dataYear == calendarYear && dataMonth == calendarMonth) {
                        cellData.text = rowData.itemPrice.toString()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CalendarChildViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_calendar_child_item, parent,
            false
        )
        return CalendarChildViewHolder(layout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarChildViewHolder, position: Int) {
        holder.binding(rowData[position] as Item)
    }

    override fun getItemCount(): Int = rowData.size

}
