package com.example.inventory.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.viewModels.InventoryViewModel
import java.util.*

class ItemTouchHelperCallback2  (
    private val adapter: CategorySettingAdapter,
    private val viewModel: InventoryViewModel,
    private val expenseId: String?

) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val onTouchItem = adapter.getItemAtPosition(viewHolder.adapterPosition)
        val targetItem = adapter.getItemAtPosition(target.adapterPosition)
        adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        var defaultCalendar = Calendar.getInstance()
        if(onTouchItem != null){
            viewModel.updateExpenseItem(onTouchItem.expenseId, defaultCalendar.time, viewHolder.adapterPosition)
        }
        if(targetItem != null){
            viewModel.updateExpenseItem(targetItem.expenseId, defaultCalendar.time, target.adapterPosition)
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

}