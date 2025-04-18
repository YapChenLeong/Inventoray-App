package com.example.inventory.adapters

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetTabsAdapter(

    private val fragmentList: List<Fragment>,
    @NonNull fragmentManager: FragmentManager,
    @NonNull lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle ) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}