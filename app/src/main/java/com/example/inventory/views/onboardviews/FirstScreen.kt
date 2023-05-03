package com.example.inventory.views.onboardviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.inventory.R
import com.example.inventory.databinding.FragmentFirstScreenBinding
import com.example.inventory.databinding.FragmentViewPagerOnboardingBinding

class FirstScreen : Fragment() {

    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFirstScreenBinding.inflate(inflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardViewPager)

        binding.next.setOnClickListener {
            viewPager?.currentItem = 1
        }


        return binding.root
    }

}