package com.example.inventory.views

import Common.Common
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.adapters.ChooseItemsAdapter
import com.example.inventory.adapters.SubcategoryListAdapter
import com.example.inventory.data.*
import com.example.inventory.databinding.FragmentChooseItemBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import com.example.inventory.viewModels.TransactionViewModel
import com.example.inventory.viewModels.TransactionViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.math.exp

class ChooseItemFragment : Fragment(), BottomSheetTabsFragment.OnBottomSheetDateSetListener {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }
    private val transactionViewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: FragmentChooseItemBinding? = null
    private val binding get() = _binding!!
    val dummyData = DataResources().loadExpensesData()
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private lateinit var bottomSheetFragment: BottomSheetFragment
    private lateinit var bottomSheetTabsFragment: BottomSheetTabsFragment

    private var originalRootHeight = 0
    private var originalRecycleViewHeight = 0
    private var bottomSheetHeight = 0
    private var flagHideNavBottom = 0
    private var hasFocusEditText = false
    private var hasOpenedBottomSheet = false
    private var hasAlreadySetKeyboardSize = false
    private var selectedIconResourceId = 0
    private var selectedIconTypeString : String = ""
    private var selectedIconCategory : String = ""


    val imm: InputMethodManager by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    private var windowHeightMethod = InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    private lateinit var itemDescription : TextInputEditText
    private lateinit var labelNumber: TextView
    private lateinit var buttonDate: TextView
    private lateinit var inputDate: Date
    private var mDateSetListener: OnDateSetListener? = null

    private var itemHasExpanded : Boolean = false

    private var singleItem : CategorySettings? = null

    private var dateAndTime: Date? = null

    private lateinit var bottomSheetImage : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChooseItemBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.constraintLayout)
        val bottomSheetView: View = view.findViewById(R.id.SampleBottomSheet)
        itemDescription = bottomSheetView.findViewById(R.id.item_descriptions)
        labelNumber = bottomSheetView.findViewById(R.id.label_number)
        var button7 = view.findViewById<TextView>(R.id.button_seven)
        var button8 = view.findViewById<TextView>(R.id.button_eight)
        var button9 = view.findViewById<TextView>(R.id.button_nine)
        buttonDate = view.findViewById<TextView>(R.id.button_date)
        var button4 = view.findViewById<TextView>(R.id.button_four)
        var button5 = view.findViewById<TextView>(R.id.button_five)
        var button6 = view.findViewById<TextView>(R.id.button_six)
        var buttonPlus = view.findViewById<TextView>(R.id.button_plus)
        var button1 = view.findViewById<TextView>(R.id.button_one)
        var button2 = view.findViewById<TextView>(R.id.button_two)
        var button3 = view.findViewById<TextView>(R.id.button_three)
        var buttonMinus = view.findViewById<TextView>(R.id.button_minus)
        var buttonDot = view.findViewById<TextView>(R.id.button_dot)
        var button0 = view.findViewById<TextView>(R.id.button_zero)
        var buttonCancel = view.findViewById<ImageButton>(R.id.button_cancel)
        var buttonDone = view.findViewById<ImageButton>(R.id.button_done)
        buttonDone.tag = "done"

        dismissKeyboard(itemDescription)
        itemDescription.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.action == KeyEvent.ACTION_DOWN) {
                // Call your function when the down arrow is pressed
                return@setOnKeyListener true
            }
            false
        }
        itemDescription.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dismissKeyboard(itemDescription)
                true
            } else {
                false
            }
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

//        var bottomSheet = view.findViewById<LinearLayout>(R.id.LinearBottomSheet)
        var bottomSheet = view.findViewById<ConstraintLayout>(R.id.SampleBottomSheet)
        bottomSheetImage = bottomSheet.findViewById(R.id.item_image)
//        var extraViewHeight = binding.extraView.height
        val displayMetrics = resources.displayMetrics
//        val dpHeight = binding.extraView.height / (displayMetrics.densityDpi / 160f)

        val decorView = requireActivity().window.decorView


        view.setOnSystemUiVisibilityChangeListener {
            flagHideNavBottom = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        var actionBar = (activity as AppCompatActivity?)!!.supportActionBar


//        var buttonZero = view.findViewById<TextView>(R.id.button_zero)
//        buttonZero.setOnClickListener {
//            Toast.makeText(requireContext(), "TextView Clicked!", Toast.LENGTH_SHORT).show();
//        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
//        (bottomSheetBehavior as BottomSheetBehavior<View>).state = BottomSheetBehavior.STATE_HIDDEN
//            handleGridItemClick(position)
//            val modalBottomSheet = BottomSheetFragment()
//            modalBottomSheet.show(requireActivity().supportFragmentManager, modalBottomSheet.tag)

//        bottomSheetFragment = BottomSheetFragment()
        binding.root.requestLayout()
        originalRecycleViewHeight = binding.gridChooseItemRecycleView.height
//        hideBottomNavigationBar()

        itemDescription.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                button7.visibility = View.GONE
                button8.visibility = View.GONE
                button9.visibility = View.GONE
                buttonDate.visibility = View.GONE
                button4.visibility = View.GONE
                button5.visibility = View.GONE
                button6.visibility = View.GONE
                buttonPlus.visibility = View.GONE
                button1.visibility = View.GONE
                button2.visibility = View.GONE
                button3.visibility = View.GONE
                buttonMinus.visibility = View.GONE
                buttonDot.visibility = View.GONE
                button0.visibility = View.GONE
                buttonCancel.visibility = View.GONE
                buttonDone.visibility = View.GONE

                hasFocusEditText = true
                bottomSheet.requestLayout()
                // The EditText gained focus, execute your function here
//                inputText.visibility = View.GONE
            }else{
                hasFocusEditText = false
            }
        }
        button0.setOnClickListener {
            appendNumberToTextView("0")
        }
        button1.setOnClickListener {
            appendNumberToTextView("1")
        }
        button2.setOnClickListener {
            appendNumberToTextView("2")
        }
        button3.setOnClickListener {
            appendNumberToTextView("3")
        }
        button4.setOnClickListener {
            appendNumberToTextView("4")
        }
        button5.setOnClickListener {
            appendNumberToTextView("5")
        }
        button6.setOnClickListener {
            appendNumberToTextView("6")
        }
        button7.setOnClickListener {
            appendNumberToTextView("7")
        }
        button8.setOnClickListener {
            appendNumberToTextView("8")
        }
        button9.setOnClickListener {
            appendNumberToTextView("9")
        }
        buttonPlus.setOnClickListener {
            performOperation("+")
        }
        buttonMinus.setOnClickListener {
            performOperation("-")
        }

        buttonCancel.setOnClickListener{
            clearLastCharacter()
        }
        buttonDot.setOnClickListener {
            appendDecimalToTextView(".")
        }
        buttonDone.setOnClickListener {
            when (buttonDone.tag) {
                "equal" -> {
                   calculateNumbers()
                }
               "done" -> {
                    addTransactionData()
                }
                else -> {

                }
            }

        }

        //SET UP DATE TIME PICKER
        var defaultCalendar = Calendar.getInstance()
        val df = SimpleDateFormat("dd MMM", Locale.getDefault())
        buttonDate.text = "Today \n" + df.format(defaultCalendar.time)
        inputDate = defaultCalendar.time

        var newCalendar: Calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            newCalendar.set(Calendar.YEAR, year)
            newCalendar.set(Calendar.MONTH, month)//Months are indexed from 0 not 1 so 10 is November and 11 will be December.
            newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        buttonDate.setOnClickListener {
            val bottomSheetTabsFragment = BottomSheetTabsFragment()
            // Call the method to show the bottom sheet
            bottomSheetTabsFragment.onBottomSheetSetListener = this
            bottomSheetTabsFragment.show(requireActivity().supportFragmentManager, bottomSheetTabsFragment.tag)

//            val datePickerBottomSheet = DateWheelPickerFragment()
//            datePickerBottomSheet.onDateSetListener = this
//            datePickerBottomSheet.show(requireActivity().supportFragmentManager, datePickerBottomSheet.tag)
        }

//        buttonDate.setOnClickListener {
//            val builder = MaterialDatePickerDialog.Builder.setTitle(getString(R.string.set_start_date))
//                .setNegativeButtonText(getString(R.string.cancel))
//                .setPositiveButtonText(getString(R.string.ok))
//                // Below values can be set from the style as well (materialDatePickerViewStyle)
//                .setDate(OffsetDateTime.now().plusDays(0).toInstant().toEpochMilli()) // default current date
//                .setDateFormat(MaterialDatePickerView.DateFormat.DD_MMMM_YYYY) // default DateFormat.DD_MMM_YYYY (05 Feb 2021)
//                .setTheme(R.style.ThemeOverlay_Dialog_DatePicker) // default R.style.ThemeOverlay_Dialog_MaterialDatePicker
//                .setFadeAnimation(350L, 1050L, .3f, .7f)
//                .build()
//
//            builder.setOnDatePickListener { selectedDate -> // selected date as long value
//                val df = SimpleDateFormat("dd MMM", Locale.getDefault())
//                buttonDate.text = "Today \n" + df.format(Date(selectedDate))
//                Toast.makeText(
//                    it.context,
//                    "${builder.getDayOfMonth()}-${builder.getMonth()}-${builder.getYear()}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            builder.show(requireNotNull(this.fragmentManager), MaterialDatePickerDialog::class.simpleName)
//        }


//        DATE WHEEL PICKER DIALOG
//        buttonDate.setOnClickListener{
//            val cal = Calendar.getInstance()
//            val year = cal[Calendar.YEAR]
//            val month = cal[Calendar.MONTH]
//            val day = cal[Calendar.DAY_OF_MONTH]
//
//            val dialog = DatePickerDialog(
//                requireActivity(),
//                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                mDateSetListener,
//                year, month, day
//            )
//            // Set positive button text and listener
//            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set") { _, _ ->
//                val selectedYear = dialog.datePicker.year
//                val selectedMonth = dialog.datePicker.month
//                val selectedDay = dialog.datePicker.dayOfMonth
//
//                // Format the selected date
//                val formattedDate = SimpleDateFormat("dd MMM", Locale.getDefault())
//                    .format(Calendar.getInstance().apply {
//                        set(Calendar.YEAR, selectedYear)
//                        set(Calendar.MONTH, selectedMonth)
//                        set(Calendar.DAY_OF_MONTH, selectedDay)
//                    }.time)
//
//                // Update your button text with the formatted date
//                buttonDate.text = "Today \n$formattedDate"
//            }
//
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            dialog.show()
//        }
//
//        mDateSetListener =
//            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
//                var month = month
//                month = month + 1
//                val date = "$month/$day/$year"
//                val formattedDate = SimpleDateFormat("dd MMM", Locale.getDefault())
//                    .format(Calendar.getInstance().apply {
//                        set(Calendar.YEAR, year)
//                        set(Calendar.MONTH, month - 1)
//                        set(Calendar.DAY_OF_MONTH, day)
//                    }.time)
//                buttonDate.text = "Today \n$formattedDate"
//            }


//        DATE DIALOG
//        buttonDate.setOnClickListener {
//            context?.let { it -> DatePickerDialog(it, datePicker, newCalendar.get(Calendar.YEAR),  newCalendar.get(Calendar.MONTH),  newCalendar.get(Calendar.DAY_OF_MONTH)).show() }
//        }


        //This function is executed when the text value change
        labelNumber.addTextChangedListener {
            var currentText = labelNumber.text.toString()
            var tokens = currentText.toCharArray() //exp: [2][+][3]
            var hasNumber = tokens[0] in '0'..'9'
            var number2 = if(tokens.size > 2) tokens[2] else ""
            val hasNumbers = tokens.isNotEmpty() && (tokens[0] in '0'..'9' || (tokens[0] == '-' && tokens.size > 1 && tokens[1] in '0'..'9'))

            val cleanedExpression = currentText.replace(",", "")
            // Split the input string by operators while keeping the operators in the result
            val segmentsWithOperators = cleanedExpression.split("(?=[+\\-*/])|(?<=[+\\-*/])".toRegex()).toMutableList()

            // Check if the TextView content contains the operators
            val hasOperator = currentText?.contains("+")!! || currentText?.contains("-")!!
            val lastCharOperator = if (currentText.isNotEmpty()) currentText.last().toString() else ""
            // Set the appropriate image resource based on the presence of operators
            if (hasNumber && (lastCharOperator == "+" || lastCharOperator == "-")) {
                buttonDone.setImageResource(R.drawable.ic_grey_done_24)
                buttonDone.tag = "done"
            } else {
                if(hasNumber && hasOperator && (lastCharOperator != "+" || lastCharOperator != "-")) {
                    buttonDone.setImageResource(R.drawable.ic_black_equal_24)
                    buttonDone.tag = "equal"
                }else{
                    buttonDone.setImageResource(R.drawable.ic_grey_done_24)
                    buttonDone.tag = "done"
                }
            }
            val operators = listOf("+", "-", "*", "/")

//            val itemDescriptionParams = itemDescription.layoutParams as ConstraintLayout.LayoutParams
//            val labelNumberParams = labelNumber.layoutParams as ConstraintLayout.LayoutParams
//
//            var basePercent = 0.1f
//            var maxWidth = itemDescriptionParams.matchConstraintPercentWidth + labelNumberParams.matchConstraintPercentWidth
//            val increment = 0.01f // Increment step for each character beyond 5
//
//            var labelPercent = if(labelNumber.text.length > 1) {
//                basePercent + ((labelNumber.text.length - 1) * increment)
//            }else{
//                basePercent
//            }
//            val labelPercentBigDecimal = BigDecimal(labelPercent.toString()).setScale(2, RoundingMode.DOWN)
//            labelPercent = labelPercentBigDecimal.toFloat()
//
//            if(labelPercent > basePercent){
//                itemDescriptionParams.matchConstraintPercentWidth = (itemDescriptionParams.matchConstraintPercentWidth - (labelPercent - basePercent))
//            }
//
//            labelNumberParams.matchConstraintPercentWidth = labelPercent
//
////            labelNumberParams.matchConstraintDefaultWidth = labelPercent
//            labelNumber.layoutParams = labelNumberParams
//            itemDescription.layoutParams = itemDescriptionParams
        }


        viewModel.getExpenseData.observe(this.viewLifecycleOwner) { expensesCategory ->
            var groupCategory = groupHeaderAndBody2(expensesCategory as List<ExpenseDataWithSubExpense>)

            val chooseItemAdapter = ChooseItemsAdapter(binding.root, groupCategory, requireActivity(), binding.gridChooseItemRecycleView){ expenseItem, position ->

//            if(expenseItem.mList.size > 0){
//                showChildData(expenseItem.mList, position)
//                return@ChooseItemsAdapter
//            }

                if(expenseItem.type == "AddExItem"){
//                  val action = ChooseItemFragmentDirections.actionChooseItemFragmentToAddCategoryFragment(
                    val action = ChooseItemTabsFragmentDirections.actionChooseItemTabsFragmentToAddCategoryFragment(
                        title = getString(R.string.add_category_title), categoryData = singleItem ?: CategorySettings("", 0, "", emptyList())

                    )
                    this.findNavController().navigate(action)
                    return@ChooseItemsAdapter // Stop further execution of the code
                }

                if(expenseItem.type == "AdjustExItem"){
//                    val action = ChooseItemFragmentDirections.actionChooseItemFragmentToCategorySettingTabsFragment(
                    val action = ChooseItemTabsFragmentDirections.actionChooseItemTabsFragmentToCategorySettingTabsFragment(
                        getString(R.string.adjust_category)
                    )
                    this.findNavController().navigate(action)
                    return@ChooseItemsAdapter // Stop further execution of the code
                }
                if(expenseItem.type == "Expense"){
                    selectedIconResourceId = expenseItem.imageResourceId
                    selectedIconTypeString = expenseItem.type
                    selectedIconCategory = expenseItem.description
                    bottomSheetImage.setImageDrawable(ContextCompat.getDrawable(requireActivity(), expenseItem.imageResourceId))
                    if ((bottomSheetBehavior as BottomSheetBehavior<View>).state == BottomSheetBehavior.STATE_COLLAPSED) {
                        (bottomSheetBehavior as BottomSheetBehavior<View>).state = BottomSheetBehavior.STATE_EXPANDED
                        (bottomSheetBehavior as BottomSheetBehavior<View>).isDraggable = false
                        originalRootHeight = binding.root.height
                        originalRecycleViewHeight = binding.gridChooseItemRecycleView.height
                        //with bottom action bar : 2079, without bottom action bar 1979
                        bottomSheetHeight = bottomSheet.height // 900px == 300d
                        val recycleHeight = binding.gridChooseItemRecycleView.height // 1979
                        val newRecycleHeight = originalRootHeight - bottomSheetHeight // 1079
                        binding.gridChooseItemRecycleView.layoutParams.height = newRecycleHeight
                        binding.gridChooseItemRecycleView.requestLayout() //reflect the changes
                        binding.gridChooseItemRecycleView.layoutManager?.scrollToPosition(position)
//                binding.root.layoutParams.height = bottomSheetHeight + newRecycleHeight
//                binding.root.requestLayout()
                    } else {
                        val rootHeight = binding.root.height
                        if(!hasAlreadySetKeyboardSize){
                            if(rootHeight != originalRootHeight){
                                val newContentHeight = rootHeight - bottomSheetHeight
                                binding.gridChooseItemRecycleView.layoutParams.height = newContentHeight
                                binding.gridChooseItemRecycleView.layoutManager?.scrollToPosition(position)
                            }else{
                                val newContentHeight = originalRootHeight - bottomSheetHeight
                                binding.gridChooseItemRecycleView.layoutParams.height = newContentHeight
                                binding.gridChooseItemRecycleView.layoutManager?.scrollToPosition(position)
                            }
                        }
                    }
                }

                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    if ((bottomSheetBehavior as BottomSheetBehavior<View>).state == BottomSheetBehavior.STATE_EXPANDED){
                        (bottomSheetBehavior as BottomSheetBehavior<View>).state = BottomSheetBehavior.STATE_COLLAPSED
                        binding.gridChooseItemRecycleView.layoutParams.height = originalRootHeight
                        binding.gridChooseItemRecycleView.requestLayout()
                    }
                }

//            val viewTreeObserver = binding.root.viewTreeObserver
//
//            val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    // Your logic here
//                    var rootHeight = binding.root.height
//                    val recycleHeight = binding.gridChooseItemRecycleView.height
//
//                    if(rootHeight !== originalRootHeight){
//                        val newContentHeight = rootHeight - bottomSheetHeight
//                        binding.gridChooseItemRecycleView.layoutParams.height = newContentHeight
//                        binding.gridChooseItemRecycleView.requestLayout()
//                        binding.root.viewTreeObserver.removeOnPreDrawListener(this)
//
//                    }else {
//                        val newContentHeight = originalRecycleViewHeight - bottomSheetHeight
//                        binding.gridChooseItemRecycleView.layoutParams.height = newContentHeight
//                        binding.gridChooseItemRecycleView.requestLayout()
//                        binding.root.viewTreeObserver.removeOnPreDrawListener(this)
//                    }
//
//                    // Remove the listener to stop further callbacks
//                    viewTreeObserver.removeOnPreDrawListener(this)
//
//                    // Return true to continue with the drawing, or false to cancel it
//                    return true
//                }
//            }
//            viewTreeObserver.addOnPreDrawListener(preDrawListener)

//            binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    // This callback will be called when the layout is complete
//                    // Here, you can get updated dimensions, including the height of the root view
//
//                    // For example, you can get the height of the root view
//                    val rootViewHeight = binding.root.height
//                    val keyBoardHeight = windowHeightMethod.invoke(imm) as Int
//
//                    if (hasFocusEditText && hasAlreadySetKeyboardSize) {
//                        val newContentHeight = originalRootHeight - bottomSheet.height - keyBoardHeight
//                        binding.gridChooseItemRecycleView.layoutParams.height = newContentHeight
//                        binding.gridChooseItemRecycleView.requestLayout()
//                    }
//
//                    // Do something with the updated dimensions
//
//                    // Remove the listener to avoid multiple calls
//                }
//            })

                binding.root.viewTreeObserver.addOnPreDrawListener { // Your logic here
                    var rootHeight = binding.root.height
                    val recycleHeight = binding.gridChooseItemRecycleView.height

                    val keyBoardHeight = windowHeightMethod.invoke(imm) as Int
                    hasAlreadySetKeyboardSize = keyBoardHeight > 0

                    if (hasFocusEditText && hasAlreadySetKeyboardSize) {
                        val newContentHeight = originalRootHeight - bottomSheet.height - keyBoardHeight
                        binding.root.layoutParams.height = newContentHeight + bottomSheet.height
                        binding.root.requestLayout()
                        binding.gridChooseItemRecycleView.layoutParams.height = newContentHeight
                        binding.gridChooseItemRecycleView.requestLayout()
                    } else {
                        if (rootHeight !== originalRootHeight) {
                            if (keyBoardHeight == 0) {
                                hasFocusEditText = false
                                itemDescription.clearFocus()
                                button7.visibility = View.VISIBLE
                                button8.visibility = View.VISIBLE
                                button9.visibility = View.VISIBLE
                                buttonDate.visibility = View.VISIBLE
                                button4.visibility = View.VISIBLE
                                button5.visibility = View.VISIBLE
                                button6.visibility = View.VISIBLE
                                buttonPlus.visibility = View.VISIBLE
                                button1.visibility = View.VISIBLE
                                button2.visibility = View.VISIBLE
                                button3.visibility = View.VISIBLE
                                buttonMinus.visibility = View.VISIBLE
                                buttonDot.visibility = View.VISIBLE
                                button0.visibility = View.VISIBLE
                                buttonCancel.visibility = View.VISIBLE
                                buttonDone.visibility = View.VISIBLE
                                binding.root.layoutParams.height = originalRootHeight
                                binding.root.requestLayout()
                            }
                            val newContentHeight = originalRootHeight - bottomSheetHeight
                            binding.gridChooseItemRecycleView.layoutParams.height = newContentHeight
                            binding.gridChooseItemRecycleView.requestLayout()
                            //                            binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                        }
                    }


                    // Return true to continue with the drawing, or false to cancel it
                    true
                }
                //            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
            }
//        val chooseItemAdapter = ChooseItemsAdapter(dummyData, requireActivity(), requireActivity().supportFragmentManager)

            val layoutManager = GridLayoutManager(this.context, 4)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (chooseItemAdapter.getItemViewType(position)) {
                        Common.VIEWTYPE_GROUP -> 1
                        Common.VIEWTYPE_ITEM -> 4
                        else -> 1
                    }
                }
            }
            binding.gridChooseItemRecycleView.layoutManager = layoutManager
            binding.gridChooseItemRecycleView.adapter = chooseItemAdapter


//        binding.gridChooseItemRecycleView.setHasFixedSize(true)
        }
    }


    private fun formatExpression(expression: String): String{
        //exclude the comma
        val cleanedExpression = expression.replace(",", "")
        // Split the expression based on the operator
        val pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)([+\\-*/]?)(\\d*(?:\\.\\d*)?)")
        val matcher = pattern.matcher(cleanedExpression)
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val formattedExpression = StringBuilder()

        if (matcher.matches()) {
            val leftNumber = matcher.group(1)
            val operator = matcher.group(2)
            val rightNumber = matcher.group(3)

            if (leftNumber.isNotEmpty()) {
                formattedExpression.append(
                    if (leftNumber.contains(".")) {
                        numberFormat.format(leftNumber.toDouble())
                    } else {
                        numberFormat.format(leftNumber.toLong())
                    }
                )
            }

            if (operator.isNotEmpty()) {
                formattedExpression.append(operator)
            }

            if (rightNumber.isNotEmpty()) {
                formattedExpression.append(
                    if (rightNumber.contains(".")) {
                        numberFormat.format(rightNumber.toDouble())
                    } else {
                        numberFormat.format(rightNumber.toLong())
                    }
                )
            }
        }else{
            return expression
        }
        return formattedExpression.toString()
    }

    private fun calculateNumbers() {
        var expression = labelNumber.text.toString()
        var tokens = expression.toCharArray()

        //Stack class
        var values = Stack<Double>()
        var operators = Stack<Char>()
        var i = 0
//        while (i < tokens.size) {
//            println("begin: $i")
//            println("tokensize: ${tokens.size}")
//            when{
//                tokens[i] in '0'..'9' -> {
//                    val sbuf = StringBuffer()
//                    while (i < tokens.size && tokens[i] in '0'..'9'){
//                        sbuf.append(tokens[i++])
//                    }
//                    values.push(sbuf.toString().toDouble())//push value into the stack
//                    i--
//
//                }
//                tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' -> {
//                    println("operator: ${tokens[i]}")
//                    operators.push(tokens[i])
//                }
//            }
//            i++
//        }
//        var result = applyOp(operators.pop(), values.pop(), values.pop())//popped elements
//
//        var intResult = result.toInt()
//        labelNumber.text = intResult.toString()
        val cleanedExpression = expression.replace(",", "")
        // Split the input string by operators while keeping the operators in the result
        val segmentsWithOperators = cleanedExpression.split("(?=[+\\-*/])|(?<=[+\\-*/])".toRegex()).toMutableList()
        var result = applyOp(segmentsWithOperators[0].toDouble(), segmentsWithOperators[1], segmentsWithOperators[2].toDouble())

        labelNumber.text = formatExpression(displayResult(result, segmentsWithOperators[0].toDouble(), segmentsWithOperators[2].toDouble()))
    }

    private fun applyOp(number1: Double?, op: String, number2: Double?): Double {
//        val num1 = number1 ?: return 0.0
//        val num2 = number2 ?: return 0.0
        return if (number1 != null && number2 != null) {
            when(op){
                "+" -> number1 + number2
                "-" -> number1 - number2
                "*" -> number1 * number2
                "/" -> if (number2 == 0.0) throw UnsupportedOperationException("Cannot divide by zero") else number1 / number2
                else -> 0.0
            }
        }else{
            0.0
        }
    }

    private fun displayResult(result: Double, number1: Double, number2: Double): String {
        fun getDecimalPlaces(number: Double): Int {
            val decimalPart = number.toString().substringAfter(".") //get the decimal number
            return decimalPart.length
        }
        val number1Decimals = getDecimalPlaces(number1)
        val number2Decimals = getDecimalPlaces(number2)
        val maxDecimals = maxOf(number1Decimals, number2Decimals)

        return if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            String.format("%.${maxDecimals}f", result)
        }
    }

    private fun addTransactionData(){
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_confirmation_dialog_layout, null)

            if (isEntryValid()) {
                transactionViewModel.addNewTransaction(
                    selectedIconResourceId,
                    selectedIconTypeString,
                    selectedIconCategory,
                    itemDescription.text.toString(),
                    labelNumber.text.toString(),
                    dateAndTime ?: Date() // Use the current date if null, This ensures that a Date is always passed.
                )
//                val action = ChooseItemFragmentDirections.actionChooseItemFragmentToTabsViewPagerFragment()
                val action = ChooseItemTabsFragmentDirections.actionChooseItemTabsFragmentToItemListFragment()
                findNavController().navigate(action)
            }

            val dialogMessage = dialogView.findViewById<TextView>(R.id.dialogMessage)
//            dialogMessage.text = "Description :"+ itemDescription.text.toString() + "\n" +
//                    "Amount: " + labelNumber.text.toString() + "\n" +
//                    "Date: " +  dateAndTime

//            val builder = android.app.AlertDialog.Builder(requireContext())
//            val dialog = builder.setView(dialogView).create()
//
//            dialogView.findViewById<TextView>(R.id.btnDelete).setOnClickListener {
//                dialog.dismiss()
//            }
//
//            dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
//                dialog.dismiss()
//            }
//            dialog.show()

        Toast.makeText(requireContext(), "Item name:" + itemDescription.text.toString() + labelNumber.text.toString(), Toast.LENGTH_LONG).show()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    private fun isEntryValid(): Boolean {
        return transactionViewModel.isEntryValid(
            selectedIconTypeString,
            itemDescription.text.toString(),
            labelNumber.text.toString()
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun countSubItemsByExpenseId(item:List<ExpenseDataWithSubExpense>): Map<String, Int> {
        val subItemCounts = mutableMapOf<String, Int>()

        item.forEach { expenseData ->
            val count = subItemCounts.getOrDefault(expenseData.expenseId, 0)
            subItemCounts[expenseData.expenseId] = count + 1
        }
        return subItemCounts
    }

   private fun groupHeaderAndBody2(items: List<ExpenseDataWithSubExpense>): List<Any>{
       var list : MutableList<Any> = mutableListOf()
       var subList : MutableList<SubCategoryLists> = mutableListOf()

       var headId : String? = null
       var header : Int = 0

       for(item in items){
           if((headId == null || headId != item.expenseId) && header<=3){
               list.add(HeaderCategory(item.expenseId, item.imageResourceId, item.expenseName, item.type, item.subExpenseCount))
               headId = item.expenseId
               header++
               if(header == 4){
                   header = 0
                   val newList = ArrayList(subList)
                   for (subItem in newList){
                       list.add(BodySubCategory(subItem.expenseId, newList))
                   }
                   subList.clear()
               }
           }else{
               if(item.subExpenseId != null && item.subExpenseId.isNotBlank() || item.expenseId == headId){
                   subList.add(SubCategoryLists(item.expenseId,item.subExpenseId,item.subExpenseName))
                   continue
               }
           }

           if(item.subExpenseId != null && item.subExpenseId.isNotBlank()){
               subList.add(SubCategoryLists(item.expenseId,item.subExpenseId,item.subExpenseName))
           }
       }

       list.add(HeaderCategory("001", R.drawable.ic_add_circle_outline_black, "Add Category", "AddExItem", 0))
       list.add(HeaderCategory("002", R.drawable.ic_setting_black, "Edit", "AdjustExItem", 0))

       return list
   }

//    private fun groupHeaderAndBody(items: List<ExpenseItems>): List<Any> {
//        var list : MutableList<Any> = mutableListOf()
//
//        var subList : MutableList<ExpenseItems> = mutableListOf()
//
//        var previousTitle : String? = null
//        var header : Int = 0
//        var body : Int = 0
//
//        for(item in items){
//            if(previousTitle == null || previousTitle != getString(item.stringResourceId) || header<=3){
//                list.add(HeaderCategory(item.stringResourceId,item.imageResourceId,item.type,item.mList.size))
//                header++
//                if(header == 4){
//                    header = 0
//                    for (subItem in subList){
//                        list.add(ExpenseItems(subItem.stringResourceId,subItem.imageResourceId,subItem.type,subItem.mList))
//                    }
//                    subList.clear()
//                }
//                previousTitle = resources.getString(item.stringResourceId)
//            }else{
//                header = 0
//                for (subItem in subList){
//                    list.add(ExpenseItems(subItem.stringResourceId, subItem.imageResourceId, subItem.type, subItem.mList))
//                }
//                subList.clear()
//            }
//
//            if(item.mList.isNotEmpty()){
//                subList.add(ExpenseItems(item.stringResourceId,item.imageResourceId,item.type,item.mList))
//            }
//
//        }
//
//        return list
//    }

    private fun showChildData(dataSet :  List<SubCategoryLists>,  position: Int) {
        val subAdapter = SubcategoryListAdapter(dataSet)
//            binding.subcategoryRecycleView.layoutManager = LinearLayoutManager(binding.root.context)
//            binding.subcategoryRecycleView.adapter = subAdapter
        // Initialize child RecyclerView

//        val childRecyclerView: RecyclerView = view?.findViewById(R.id.subcategory_recycle_lists_view)!!
//        childRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        childRecyclerView.adapter = subAdapter

        // Optionally, you can set visibility or animate the appearance of the child RecyclerView
//        childRecyclerView.visibility = View.VISIBLE
    }

    private fun appearDatePicker() {}

    private fun appendNumberToTextView(number: String) {
        var segments = labelNumber.text.toString().split("+", "-", "*", "/")

        if(labelNumber.text.toString() == "0"){ //first digit number
            labelNumber.text = number
        }else{//second digit number
            labelNumber.append(number)
        }

        // Split the input string by operators while keeping the operators in the result
        val segmentsWithOperators = labelNumber.text.toString().split("(?=[+\\-*/])|(?<=[+\\-*/])".toRegex()).toMutableList()
        if(segmentsWithOperators[0].contains(".") || segmentsWithOperators[segmentsWithOperators.size - 1].contains(".")){
            for (i in segmentsWithOperators.indices) {
                val segment = segmentsWithOperators[i]
                if (segment.contains(".")) {
                    val parts = segment.split(".")
                    if (parts.size == 2) {
                        val integerPart = parts[0]
                        val decimalPart = parts[1]
                        if (decimalPart.length > 2) {
                            // Truncate the decimal part to 2 digits
                            segmentsWithOperators[i] = "$integerPart.${decimalPart.take(2)}"
                        }
                    }
                }
                // Reconstruct the text from segments
                val truncatedText = segmentsWithOperators.joinToString("")
                labelNumber.text = truncatedText
            }
        }

        labelNumber.text = formatExpression(labelNumber.text.toString())
    }

    //This function is executed when + or - button is pressed
    private fun performOperation(operator: String){
        var currentExpression = labelNumber.text.toString().replace(",", "")

        val lastChar = if (currentExpression.isNotEmpty()) currentExpression.last().toString() else ""
        val hasOperator = currentExpression.contains("+") || currentExpression.contains("-")
        var result: String

        if (hasOperator) {
            result = if(lastChar == "+" || lastChar == "-"){
                // If the last character is already an operator, replace it with the clicked operator
                val newExpression = currentExpression.substring(0, currentExpression.length - 1) + operator
                calculateResult(newExpression, operator)
            }else{
                calculateResult(currentExpression, operator)
            }
            labelNumber.text = formatExpression(result)
        }else{
            labelNumber.append(operator)
        }
    }

    private fun calculateResult(currentExpression: String, operator: String):String {
        try {
            val result = ExpressionBuilder(currentExpression).build().evaluate()
            val formattedResult = if (result % 1 == 0.0) {
                // If the result is an integer, remove the decimal part
                result.toLong().toString() + operator
            } else {
                // If the result has a decimal part, display it as is
                result.toString() + operator
            }
            return formattedResult
        } catch (e: Exception) {
            return currentExpression
            // Handle the exception (e.g., invalid expression)
//            labelNumber.text = "Error"
        }
    }

    // Function to clear the last character from the TextView
    private fun clearLastCharacter() {
        val currentText = labelNumber.text.toString()
        if (currentText.isNotEmpty() && currentText != "0") {
            // Remove the last character
            val newText =
                if(currentText.length == 1){
                    "0"
                }else{
                   currentText.substring(0, currentText.length - 1)
                }
            labelNumber.text = newText
        }
    }

    //This function is executed when dot button is clicked
    private fun appendDecimalToTextView(decimal: String){
        var currentText = labelNumber.text.toString()
        val lastChar = if (currentText.isNotEmpty()) currentText.last().toString() else ""

        val segments = currentText.split("+", "-", "*", "/")

        if(segments.size == 1){
            if(!segments[0].contains(".")){
                labelNumber.append(decimal)
            }
        }else if(segments.size == 2){
            if(!segments[segments.size - 1].contains(".")){
                labelNumber.append(decimal)
            }
        }

        if(lastChar == "+" || lastChar == "-"){
            labelNumber.text = "$currentText" + "0$decimal"
        }

    }

    private fun dismissKeyboard(itemDescription: TextInputEditText?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(itemDescription?.windowToken, 0)
    }

    private fun hideSystemUI() {
        view?.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
            )
    }


    private fun handleGridItemClick(position: Int) {
        val lastItemPosition = dummyData.size - 1
        // Show the bottom sheet
        binding.gridChooseItemRecycleView.scrollToPosition(position)
        // Update the content of the bottom sheet based on the selected item
        // Example: bottomSheet.findViewById<TextView>(R.id.textView).text = items[position]
    }

    private fun isNavigationBarHidden(): Boolean {
        val activity = requireActivity()
        val decorView = activity.window.decorView

        val uiVisibility = decorView.systemUiVisibility
//        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if(uiVisibility == 0){
            return false
        }else{
            requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            return true
        }
    }

    private fun hideBottomNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // If running on Android 11 or higher, use the new immersive mode
            requireActivity().window.setDecorFitsSystemWindows(false)
            requireActivity().window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            requireActivity().window.insetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            // For versions lower than Android 11
            requireActivity().window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        }
    }

    override fun onDateTimeSet(selectedDate: String, selectedDateTime: Date) {
        buttonDate.text = selectedDate
        dateAndTime = selectedDateTime
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
//                val action = ChooseItemFragmentDirections.actionChooseItemFragmentToTabsViewPagerFragment()
                val action = ChooseItemTabsFragmentDirections.actionChooseItemTabsFragmentToItemListFragment()
                this.findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}