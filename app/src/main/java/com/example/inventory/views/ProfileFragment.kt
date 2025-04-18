package com.example.inventory.views

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.inventory.R
import com.example.inventory.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var androidEditText: AppCompatEditText? = null
    private var editText: EditText? =  null
    private var hasFocusEditText = false
    private val visibleBounds = Rect()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            show()
            setDisplayShowCustomEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(true)
            setTitle(R.string.profile)
        }
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

        val bottomSheetBehavior: BottomSheetBehavior<*>?
//        val bottomSheet = view.findViewById<View>(R.id.SampleBottomSheet)
        val bottomSheet = view.findViewById<View>(R.id.InputBottomSheet)

        androidEditText = view.findViewById(R.id.android_editText)
        editText = view.findViewById(R.id.normal_editText)

        // Set OnFocusChangeListener
        // Set OnFocusChangeListener
        with(androidEditText) {
            // Set OnFocusChangeListener
            // Set OnFocusChangeListener
            this?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    hasFocusEditText = true
                } else {
                    // EditText lost focus
                    // Your code here
                }
            })
        }
//
//        with(editText) {
//            // Set OnFocusChangeListener
//            // Set OnFocusChangeListener
//            this?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
//                if (hasFocus) {
//                    editText?.visibility = View.INVISIBLE
//                } else {
//                    // EditText lost focus
//                    // Your code here
//                }
//            })
//        }

        androidEditText?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.action == KeyEvent.ACTION_DOWN) {
                // Call your function when the down arrow is pressed
                return@setOnKeyListener true
            }
            false
        }


        binding.root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            private var wasKeyboardShown = false

            override fun onPreDraw(): Boolean {
                val imm: InputMethodManager by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
                val windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
                val height = windowHeightMethod.invoke(imm) as Int

                binding.root.getWindowVisibleDisplayFrame(visibleBounds)
                val visibleBoundsHeight = visibleBounds.height()
                val rootViewHeight = binding.root.rootView.height
                val rootHeight = binding.root.height

                val heightDiff = rootViewHeight - rootHeight
                val isOpen = heightDiff > 0


                if (wasKeyboardShown) {
                    // Keyboard is about to be closed
//                    onKeyboardClosed()
                    wasKeyboardShown = false
                } else if (!wasKeyboardShown) {
                    // Keyboard is about to be opened
//                    onKeyboardOpened()
                    wasKeyboardShown = true
                }

                return true
            }
        })

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        binding.expand.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            val modalBottomSheet = BottomSheetFragment()
//            modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetFragment.TAG)
        }

        binding.collapse.setOnClickListener {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun onKeyboardOpened() {
        TODO("Not yet implemented")
    }

    private fun onKeyboardClosed() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}