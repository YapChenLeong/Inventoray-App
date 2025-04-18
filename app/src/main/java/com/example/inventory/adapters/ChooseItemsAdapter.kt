package com.example.inventory.adapters

import Common.Common
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.TAG
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.custom.GridSpacingItemDecoration
import com.example.inventory.data.*
import com.example.inventory.databinding.*
import com.example.inventory.views.BottomSheetFragment
import com.example.inventory.views.GridItemsDesignFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.IllegalArgumentException

class ChooseItemsAdapter(
    private val root: View,
    private val dataset: List<Any>,
    private val context: Context,
    private val recyclerView : RecyclerView,
    private val itemClickListener: (HeaderCategory, position:Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var isBottomSheetVisible = false
    private val expandedItems: MutableSet<Int> = mutableSetOf()

    private var expandedSubItem : Boolean = false

    private var selectedStringResourceId: String? = null
    private var selectedItemPosition: Int? = null
    private var previousItemPosition: Int? = null
    private var runOneTime: Int = 0
    private var foundRecord: Boolean = false
    private var turnOn: Boolean = false
    private var currentSelectedPosition : Int? = null
    private var currentPositionExpanded : Int? = null
    private var previousPositionExpanded : Int? = null

    inner class HeaderChooseItemViewHolder(
        val binding: FragmentGridItemsDesignBinding
        ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: HeaderCategory, position: Int) {
            val drawable = ContextCompat.getDrawable(binding.root.context, item.imageResourceId)
            binding.itemImageView.setImageDrawable(drawable)
            if(item.subSize > 0){
                binding.itemName.text = item.description + " (" + item.subSize + ")"
            }else{
                binding.itemName.text = item.description
            }

//            val subAdapter = SubcategoryListAdapter(item.mList)
//            binding.subcategoryRecycleView.layoutManager = LinearLayoutManager(context)
//            binding.subcategoryRecycleView.adapter = subAdapter

            binding.itemImageView.setOnClickListener {
                selectedStringResourceId = item.headId
                selectedItemPosition = position

                turnOn = true

                // Notify adapter that the visibility has changed
                notifyDataSetChanged()

                itemClickListener(item, position)
            //Modal Bottom Sheet
//                if(!isBottomSheetVisible){
//                    val contentHeight = calculateContentHeight()
//                    val itemPosition = adapterPosition
//                    val lastItemPosition = dataset.size - 1
//                    val itemsVisibleBelowBottomSheet = lastItemPosition - itemPosition
//                    val recycleViewHeight = recyclerView.height
////                    if (itemsVisibleBelowBottomSheet >= 0) {
////                        val scrollPosition = itemPosition + itemsVisibleBelowBottomSheet
////                        recyclerView.scrollToPosition(scrollPosition)
////                    }
//                    recyclerView.addItemDecoration(GridSpacingItemDecoration(context.resources.getDimensionPixelSize(R.dimen.grid_spacing)))
//
//                    //setRecyclerViewHeight(recyclerView.height - contentHeight)
//
//
//                    val recyclerViewHeight = recyclerView.height
//                    val bottomSheetHeight = bottomSheet.height
////                    recyclerView.smoothScrollToPosition(position)
//
//                    (bottomSheetBehavior as BottomSheetBehavior<View>).state = BottomSheetBehavior.STATE_EXPANDED
//                    (bottomSheetBehavior as BottomSheetBehavior<View>).isDraggable = false
//                    isBottomSheetVisible = true
////                    (bottomSheetBehavior as BottomSheetBehavior<View>).peekHeight = contentHeight
//                }else{
//                    (bottomSheetBehavior as BottomSheetBehavior<View>).state = BottomSheetBehavior.STATE_COLLAPSED
//                    isBottomSheetVisible = false
//
//                    setRecyclerViewHeight(RecyclerView.LayoutParams.MATCH_PARENT)
//
//                }
            }
//
//                binding.itemImageView.setOnClickListener {
//                    //Modal Bottom Sheet
////                val modalBottomSheet = BottomSheetFragment()
////                modalBottomSheet.show(fragmentManager, BottomSheetFragment.TAG)
////                dialog.dismiss()
////                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//
//                }


//            val bottomSheetBehavior: BottomSheetBehavior<*>?
//            val bottomSheet = binding.root.findViewById<View>(R.id.LinearBottomSheet)
//
//            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
//            binding.itemImageView.setOnClickListener {
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
        }
    }

//        inner class BodyChooseItemViewHolder(val binding: FragmentSubcategoryListBinding) : RecyclerView.ViewHolder(binding.root) {
    inner class BodyChooseItemViewHolder(val binding: FragmentSubcategoryRecycleViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data : BodySubCategory, position: Int){
            selectedStringResourceId = null
            runOneTime = 1

            val subAdapter = SubcategoryListAdapter(data.mList)
            binding.subcategoryRecycleListView2.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            binding.subcategoryRecycleListView2.adapter = subAdapter

            subAdapter.notifyDataSetChanged()
        }

        fun removeView(data : BodySubCategory, position: Int){
            val subAdapter = SubcategoryListAdapter(emptyList())
            binding.subcategoryRecycleListView2.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            binding.subcategoryRecycleListView2.adapter = subAdapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataset[position]) {
            is HeaderCategory -> Common.VIEWTYPE_GROUP
            is BodySubCategory -> Common.VIEWTYPE_ITEM
            else -> Common.VIEWTPYE_NO_DATA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (ViewType.values()[viewType]) {
//            ViewType.GRID -> HeaderChooseItemViewHolder(
//                FragmentGridItemsDesignBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//
//            ViewType.LINEAR -> BodyChooseItemViewHolder(
//                FragmentSubcategoryListBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }
        return when(viewType){
            Common.VIEWTYPE_GROUP -> HeaderChooseItemViewHolder(
                FragmentGridItemsDesignBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            Common.VIEWTYPE_ITEM -> BodyChooseItemViewHolder(
                FragmentSubcategoryRecycleViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            Common.VIEWTPYE_NO_DATA -> SampleAdapter.NoItemViewHolder(
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
            is HeaderChooseItemViewHolder -> {
                holder.bind(dataset[position] as HeaderCategory, position)
            }
            is BodyChooseItemViewHolder -> {
//                holder.itemView.visibility = if (groupVisibilityMap[position] == true) View.VISIBLE else View.GONE

                if (dataset[position] is BodySubCategory) {
                    var subItem = dataset[position] as BodySubCategory

                    currentSelectedPosition = position.toInt()

                    if(subItem.headId == selectedStringResourceId){
                            holder.bind(dataset[position] as BodySubCategory, position)
                            holder.itemView.visibility = View.VISIBLE
                            expandedSubItem = true
                            currentPositionExpanded = position.toInt()
                            previousPositionExpanded = position.toInt() //4

                    }else {
                        if(previousPositionExpanded!= null && previousPositionExpanded == currentPositionExpanded && expandedSubItem){
                            holder.removeView(dataset[position] as BodySubCategory, position)
                        }
                    }
//                    else{
//                        if(expandedSubItem){
//                            holder.removeView(dataset[position] as ExpenseItems, position)
//                        }
//                    }
                }
            }
        }

//        holder.image.setImageResource(item.imageResourceId)
//        holder.itemName.text = context.resources.getString(item.stringResourceId)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}