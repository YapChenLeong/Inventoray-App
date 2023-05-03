package com.example.inventory.adapters

import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.databinding.FragmentCalanderItemBinding
import java.time.LocalDate

class CalendarViewHolder internal constructor(itemView: View, onItemListener: CalendarAdapter.OnItemListener, var data: ArrayList<Any>) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val parent: ConstraintLayout
    val dayOfMonth: TextView
    val childView: RecyclerView
    private val onItemListener: CalendarAdapter.OnItemListener
    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }

    init {
        parent = itemView.findViewById(R.id.calendarParent)
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        childView = itemView.findViewById(R.id.childCalendarItem)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    fun bind(data: String, result: List<Any>, localDate: LocalDate?) {
        dayOfMonth.text = data
        val childMembersAdapter = CalendarChildAdapter(data, result, localDate)
        childView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,false)
        childView.adapter = childMembersAdapter

    }
}