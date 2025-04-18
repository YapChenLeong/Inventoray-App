package com.example.inventory.views

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.inventory.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        val bar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
//        val bottomButton = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
//        bar.visibility = View.GONE
        view.visibility = View.GONE
//        bottomButton.visibility = View.GONE
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


        Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_itemListFragment)

        }, 1000)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

//    private fun onBoardingFinished(): Boolean{
//        val sharedPre = requireActivity().getSharedPreferences("", Context.MODE_PRIVATE)
//        return sharedPre.getBoolean("Finished", false)
//    }

}