package com.example.inventory.adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

internal class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener,
    private val transactionData: List<Any>,
    private val localDate: LocalDate?,
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolders>() {

    private var currentDate = Calendar.getInstance()
    var listData : List<Any> = transactionData

    var onItemClick: (() -> Any)? = null
    class CalendarViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parent: ConstraintLayout = itemView.findViewById(R.id.calendarParent)
        val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
        val childView: RecyclerView = itemView.findViewById(R.id.childCalendarItem)
//        init {
//            itemView.setOnClickListener {
//                onItemClick?.invoke(dayOfMonth.text as String[adapterPosition])
//            }
//        }
        fun bind(data: String, listData: List<Any>, localDate: LocalDate?) {
            dayOfMonth.text = data
            val childMembersAdapter = CalendarChildAdapter(data, listData, localDate)
            childView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,false)
//            childView.layoutManager = GridLayoutManager(itemView.context,4)
            childView.adapter = childMembersAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolders {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.fragment_calander_item, parent, false)
//        view.setBackgroundColor(Color.parseColor("#EAEBEA"))
        val layoutParams = layout.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()

         return CalendarViewHolders(layout)
//        return CalendarViewHolder(view, onItemListener, transactionData)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolders, position: Int) {
//        holder.dayOfMonth.text = daysOfMonth[position]
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        val currentMonth = currentDate.get(Calendar.MONTH) + 1
        val currentYear = currentDate.get(Calendar.YEAR)

        localDate?.let {
            var calendarMonth = it.monthValue
            var calendarYear = it.year
            if(currentMonth == calendarMonth && currentYear == calendarYear){
                if (currentDay.toString() == daysOfMonth[position]){
                    holder.dayOfMonth.setBackgroundColor(Color.parseColor("#e5e5e5"))
//            holder.dayOfMonth.setTextColor(Color.WHITE)
                }
            }

        }
//
//
//        //empty the box if don't have value
//        if (daysOfMonth[position] == ""){
//            holder.parent.setBackgroundColor(Color.parseColor("#EAEBEA"))
//        }
//

        holder.bind(daysOfMonth[position], listData, localDate)
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}

