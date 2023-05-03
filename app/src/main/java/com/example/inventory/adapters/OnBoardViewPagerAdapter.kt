package com.example.inventory.adapters

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardViewPagerAdapter (
    list: ArrayList<Fragment>,
    @NonNull fragmentManager: FragmentManager,
    @NonNull lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle ) {

    private val onBoardFragmentList : ArrayList<Fragment> = list
    override fun getItemCount(): Int {
        return onBoardFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return onBoardFragmentList[position]
    }


}