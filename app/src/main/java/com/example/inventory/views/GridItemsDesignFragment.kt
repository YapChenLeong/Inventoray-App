package com.example.inventory.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inventory.R
import com.example.inventory.databinding.FragmentGridItemsDesignBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class GridItemsDesignFragment : Fragment() {

    private var _binding: FragmentGridItemsDesignBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGridItemsDesignBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        binding.itemImageView.setOnClickListener {
//            val view: View = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
//            val dialog = BottomSheetDialog(requireActivity())
//            dialog.setContentView(view)
//            dialog.show()
//        }

    }

}