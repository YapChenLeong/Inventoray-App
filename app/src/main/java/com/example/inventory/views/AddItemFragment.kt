/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory.views

import android.app.DatePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.DateTypeConverter
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.databinding.FragmentAddItemBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    // to share the ViewModel across fragments.
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .itemDao()
        )
    }
    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    lateinit var item: Item
    private lateinit var inputDate: Date
    private lateinit var inputDate2: LocalDate

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        val country = resources.getStringArray(R.array.countries)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_country, country)
        binding.itemCountry.setAdapter(arrayAdapter)
        return binding.root
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemCount.text.toString(),
        )
    }

    /**
     * Binds views with the passed in [item] information.
     */
    private fun bind(item: Item) {
        val price = "%.2f".format(item.itemPrice)
        binding.apply {
            itemCountry.setText(item.country, TextView.BufferType.SPANNABLE)
            val country = resources.getStringArray(R.array.countries)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_country, country)
            binding.itemCountry.setAdapter(arrayAdapter)

            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(price, TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    /**
     * Inserts the new Item into database and navigates up to list fragment.
     */
    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemCountry.text.toString(),
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString(),
                inputDate
            )
//            val action = AddItemFragmentDirections.actionAddItemFragmentToTabsViewPagerFragment()
            val action = AddItemFragmentDirections.actionAddItemFragmentToTabsViewPagerFragment()
            findNavController().navigate(action)
//            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
//            findNavController().navigate(action)
        }
    }

    /**
     * Updates an existing Item in the database and navigates up to list fragment.
     */
    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.itemCountry.text.toString(),
                this.binding.itemName.text.toString(),
                this.binding.itemPrice.text.toString(),
                this.binding.itemCount.text.toString(),
                inputDate

            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToTabsViewPagerFragment()
            findNavController().navigate(action)
//            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
//            findNavController().navigate(action)
        }
    }

    /**
     * Called when the view is created.
     * The itemId Navigation argument determines the edit item  or add new item.
     * If the itemId is positive, this method retrieves the information from the database and
     * allows the user to update it.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            show()
            setDisplayShowCustomEnabled(false)
            setDisplayShowTitleEnabled(true)
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        val bottomMenuBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
//        val bottomButton = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)

        bottomNavigationView.visibility = View.GONE
//        bottomMenuBar.visibility = View.GONE
//        bottomButton.visibility = View.GONE

        var defaultCalendar = Calendar.getInstance()
        val year: Int = defaultCalendar.get(Calendar.YEAR)
        val month: Int = defaultCalendar.get(Calendar.MONTH)
        val day: Int = defaultCalendar.get(Calendar.DAY_OF_MONTH)

//        val df = SimpleDateFormat("dd/MMM/yyyy (EEE)", Locale.getDefault()) //format 10/October/2022
        val df = SimpleDateFormat("dd/MM/yyyy (EEE)", Locale.getDefault()) //format 10/10/2022
        binding.itemDate.setText("Today " + df.format(defaultCalendar.time))
        inputDate = defaultCalendar.time

        var newCalendar: Calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            newCalendar.set(Calendar.YEAR, year)
            newCalendar.set(Calendar.MONTH, month)//Months are indexed from 0 not 1 so 10 is November and 11 will be December.
            newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(newCalendar)
        }

        //        This method cannot use due to will reset the time every time click it
//        binding.itemDate.setOnClickListener {
//            val date_picker = this.context?.let { it1 ->
//                DatePickerDialog(it1, android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
//                    val selectDate = Calendar.getInstance()
//                    selectDate.set(Calendar.YEAR, i)
//                    selectDate.set(Calendar.MONTH, i2)
//                    selectDate.set(Calendar.DAY_OF_MONTH, i3)
//                    updateLabel(selectDate)
//
//                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
//            }
//            date_picker?.show()
//        }

        // now create instance of the material date picker builder
//        val materialDateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        // now define the properties of the materialDateBuilder that is title text as SELECT A DATE
//        materialDateBuilder.setTitleText("SELECT A DATE")
//        materialDateBuilder.setTheme(R.style.ThemeOverlay_App_DatePicker)

        // now build the material date picker dialog
//        val materialDatePicker : MaterialDatePicker<*> = materialDateBuilder.build()
//
//        binding.itemDate.setOnClickListener {
//            // getSupportFragmentManager() to interact with the fragments associated with the material design date picker tag is to get any error in logcat
//            materialDatePicker.show(parentFragmentManager, "Material Picker")
////
//        }

        // now handle the positive button click from the material design date picker
//        materialDatePicker.addOnPositiveButtonClickListener {
//            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
//
//            selectedDates = dateFormatter.format(it)
//            var defaultCalendar = Calendar.getInstance()
//            var currentDate = dateFormatter.format(defaultCalendar.time)
//
//            if(currentDate == selectedDates){
//                binding.itemDate.setText("Today : $selectedDates")
//            }
//            else{
//                binding.itemDate.setText("Selected Date is : $selectedDates")
//            }


            // if the user clicks on the positive button that is ok button update the selected date
//            binding.itemDate.setText("Selected Date is : " + selectedDate)
            // in the above statement, getHeaderText
            // is the selected date preview from the
            // dialog
//        }



            binding.itemDate.setOnClickListener {
                context?.let { it -> DatePickerDialog(it, datePicker, newCalendar.get(Calendar.YEAR),  newCalendar.get(Calendar.MONTH),  newCalendar.get(Calendar.DAY_OF_MONTH)).show() }
            }


        //slider picker
//        binding.itemDate.setOnClickListener {
//            val getDate: Calendar = Calendar.getInstance()
//            context?.let { it ->
//                DatePickerDialog(
//                    it,
//                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                    DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
//
//                        var selectDate = Calendar.getInstance()
//                        selectDate.set(Calendar.YEAR, i)
//                        selectDate.set(Calendar.MONTH, i2)
//                        selectDate.set(Calendar.DAY_OF_MONTH, i3)
//
//                        var formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.US)
//                        val date: String = formatDate.format(selectDate.time)
//                        binding.itemDate.setText("Selected Date is : $date")
//
//                    },
//                    getDate.get(Calendar.YEAR),
//                    getDate.get(Calendar.MONTH),
//                    getDate.get(Calendar.DAY_OF_MONTH)
//                ).show()
//
//            }
//        }

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    private fun updateLabel(calendar: Calendar) {
        var defaultCalendar = Calendar.getInstance()
        val dateFormat = "dd/MM/yyyy (EEE)"
        val sdf = SimpleDateFormat(dateFormat, Locale.UK)

        var currentDate = sdf.format(defaultCalendar.time)
        var selectedDate = sdf.format(calendar.time)
        inputDate = calendar.time



        if(currentDate == selectedDate){
            binding.itemDate.setText("Today " + sdf.format(calendar.time))
        }
        else{
            binding.itemDate.setText(sdf.format(calendar.time))
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}

private fun Window?.setBackgroundDrawable(): Int {
    return Color.TRANSPARENT
}
