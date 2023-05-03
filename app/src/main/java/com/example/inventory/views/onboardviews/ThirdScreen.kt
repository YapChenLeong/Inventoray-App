package com.example.inventory.views.onboardviews

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.inventory.R
import com.example.inventory.databinding.FragmentSecondScreenBinding
import com.example.inventory.databinding.FragmentThirdScreenBinding

class ThirdScreen : Fragment() {

    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentThirdScreenBinding.inflate(inflater, container, false)

        binding.finish.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardViewPagerFragment_to_TabsViewPagerFragment)
//            onBoardingFinished()
        }

        return binding.root
    }
//
//    private fun onBoardingFinished(){
//        val sharedPre = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
//        val editor = sharedPre.edit()
//        editor.putBoolean("Finished", true)
//        editor.apply()
//    }
}