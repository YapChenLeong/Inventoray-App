package com.example.inventory.views

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.inventory.R
import com.example.inventory.databinding.FragmentBottomSheetBinding
import com.example.inventory.databinding.FragmentChooseItemBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomSheetFragment" // Define a TAG constant
    }
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = BottomSheetBehavior.from(view.parent as View)

        behavior.state = BottomSheetBehavior.STATE_EXPANDED // Set your desired state
//
//        (requireDialog() as BottomSheetDialog).dismissWithAnimation = true
////        val standardBottom = BottomSheetBehavior.from(binding.root)
//        BottomSheetBehavior.from(binding.bottomSheet).apply {
//            this.state=BottomSheetBehavior.STATE_COLLAPSED
//        }
//        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.modalBackground))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}