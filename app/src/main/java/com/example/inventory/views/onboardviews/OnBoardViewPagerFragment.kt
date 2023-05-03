package com.example.inventory.views.onboardviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inventory.R
import com.example.inventory.adapters.OnBoardViewPagerAdapter
import com.example.inventory.databinding.FragmentViewPagerOnboardingBinding
import com.example.inventory.databinding.FragmentViewPagerTabsBinding
import com.example.inventory.views.CalendarItemFragment
import com.example.inventory.views.ItemListFragment

class OnBoardViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerOnboardingBinding.inflate(inflater, container, false)

        val onBoardFragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val adapter = OnBoardViewPagerAdapter(
            onBoardFragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.onboardViewPager.adapter = adapter


        return binding.root
    }

}