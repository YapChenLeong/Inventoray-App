package com.example.inventory.adapters

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.inventory.views.CalendarItemFragment
import com.example.inventory.views.ItemListFragment

class TabsViewPagerAdapter(
    list: ArrayList<Fragment>,
    @NonNull fragmentManager: FragmentManager,
    @NonNull lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle ) {

    private val fragmentList : ArrayList<Fragment> = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
//        return when(position) {
//            0 -> {
//                ItemListFragment()
//            }
//            1 -> {
//                CalendarItemFragment()
//            }else-> {
//                Fragment()
//            }
//        }
    }


//    private val mFragmentList = ArrayList<Fragment>()
//    private val mFragmentTitleList = ArrayList<String>()
    //call by ViewPager & TabLayout
//    override fun getCount(): Int {
//        return mFragmentTitleList.size
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return mFragmentList[position]
//    }

    //called by TabLayout Class
//    override fun getPageTitle(position: Int): CharSequence? {
//        return mFragmentTitleList[position]
//
//    }
//
//    fun addFragment(fragment: Fragment, title: String){
//        mFragmentList.add(fragment)
//        mFragmentTitleList.add(title)
//    }


}