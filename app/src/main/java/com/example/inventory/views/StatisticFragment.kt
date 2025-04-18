package com.example.inventory.views

import Common.Common
import Common.CustomColors
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.adapters.CountryAdapter
import com.example.inventory.adapters.ItemListData
import com.example.inventory.data.Item
import com.example.inventory.data.TransactionListData
import com.example.inventory.databinding.FragmentStatisticBinding
//import com.example.inventory.roundTo
import kotlin.math.pow
import kotlin.math.round
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import com.example.inventory.viewModels.SharedDateTimeVM
import com.example.inventory.viewModels.SharedStatisticViewModel
import com.example.inventory.viewModels.TransactionViewModel
import com.example.inventory.viewModels.TransactionViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap
import java.util.HashSet
import java.util.Random
import kotlin.math.abs

class StatisticFragment : Fragment(), OnChartValueSelectedListener {

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

    private val sharedStatisticViewModel: SharedStatisticViewModel by activityViewModels()

    private var _binding: FragmentStatisticBinding? = null
    private val binding get() = _binding!!

    private var transactionData : List<Any> = listOf()
    private var selectedDate: LocalDate? = null

    private lateinit var adapter: CountryAdapter

    private var tf: Typeface? = null
    private var newItemDateList: MutableList<Item> = ArrayList()
    private var itemList: MutableList<Item> = ArrayList()
    private var listOfCountries: List<Pair<String, List<TransactionListData>>> = emptyList()
    private var data : PieData? = null
    var customValueFormatter: ValueFormatter? = null
    var isValueFormatterSet = false // Custom flag to track if the value formatter is set
    val formattedValuesMap = HashMap<Float, String>() // HashMap to store the formatted values

    private var monthYearTextView: TextView? = null

    val uniqueCategory = HashSet<String>()
    val duplicateCategories = HashSet<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            show()
            setDisplayShowCustomEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(true)
            setTitle(R.string.statistic)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            show()
            setDisplayShowCustomEnabled(false)
            setDisplayShowTitleEnabled(true)
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        adapter = CountryAdapter { clickedItems ->
            val itemListData = ItemListData(clickedItems)
            val action = StatisticFragmentDirections.actionStatisticFragmentToGroupItemListFragment(
                itemListData,
                clickedItems[0].itemCategory
            )//nav graph
            this.findNavController().navigate(action)
        }

        binding.statisticRecycleView.layoutManager = LinearLayoutManager(this.context)
        binding.statisticRecycleView.adapter = adapter

        selectedDate = LocalDate.now()
        val currentMonth = selectedDate!!.monthValue.toString().padStart(2, '0') // e.g., "03"
        val currentYear = selectedDate!!.year

        transactionViewModel.getTransactionDataFromMonth(currentMonth, currentYear.toString(), "Expense").observe(this.viewLifecycleOwner) { items ->
            getTransactionData(items)

            //Inside the observer, call submitList() on the adapter and pass in the new list.
            // This will update the RecyclerView with the new items on the list.
            let {
//                adapter.submitList(newItemDateList)
            }
        }

//        monthYearTextView = fragment?.view?.findViewById(R.id.sta_month_year_tv)
//        val previousMonthAction = fragment?.view?.findViewById<ImageView>(R.id.stat_previous_action)
//        val nextMonthAction = fragment?.view?.findViewById<ImageView>(R.id.stat_next_action)
//        previousMonthAction?.setOnClickListener{
//            previousMonthAction()
//        }
//        nextMonthAction?.setOnClickListener{
//            nextMonthAction()
//        }
    }

    private fun checkUniqueLabel(label: String): Boolean {
        return uniqueCategory.contains(label)
    }

    private fun setData() {
        transactionViewModel.getAllTransactionData.observe(this.viewLifecycleOwner) { items ->
            if (items.isNotEmpty()) {
                var groupItems = groupItem(items)
                listOfCountries = groupItems.toList()

                val entries: MutableList<PieEntry> = ArrayList()
                val colors = mutableListOf<Int>()

                //Create object array to store the data

//                listOfCountries.forEachIndexed { _, (country, items) ->
//                    val percentage = calculatePercentage(items).toFloat()
//                    val entry = PieEntry(percentage)
//                    entries.add(entry)
//                    colors.add(getRandomColor())
//                }

//                for (item in items) {
//                    entries.add(PieEntry(item.itemPrice.toFloat(), item.itemName))
//                }

                //Create dataset to provide styling and formatting options for the pie chart
                val dataSet = PieDataSet(entries, "Pie Chart")
                val customColors = listOf(
                    Color.parseColor("#4777c0"),
                    Color.parseColor("#a374c6"),
                    Color.parseColor("#4fb3e8"),
                    Color.parseColor("#99cf43"),
                    Color.parseColor("#fdc135"),
                    Color.parseColor("#fd9a47"),
                    Color.parseColor("#eb6e7a"),
                    Color.parseColor("#6785c2")
                )

                dataSet.colors = customColors
                dataSet.setValueTextColors(customColors)

//                dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
                dataSet.sliceSpace = 3f //set the space between each pie entry
                dataSet.selectionShift = 5f //expand the pie when it's selected
                //dataSet.setSelectionShift(0f);
                //dataSet.setSelectionShift(0f);

                dataSet.valueLinePart1OffsetPercentage = 80f // arrow line height size
                dataSet.valueLinePart1Length = 0.5f //value height
                dataSet.valueLinePart2Length = 0.8f //value width
                //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                dataSet.valueTextSize = 26f
                dataSet.valueTypeface = Typeface.DEFAULT_BOLD
                // Value formatting
//                dataSet.valueFormatter = object : ValueFormatter() {
//                    private val formatter = NumberFormat.getPercentInstance()
//
//                    override fun getFormattedValue(value: Float) =
//                        formatter.format(value / 100f)
//                }

                //Create PieData Object
                //encapsulates the complete data and settings for the pie chart, including one or more PieDataSet objects.
//                val data = PieData(dataSet)
//                data.setValueFormatter(PercentFormatter())
//                data.setValueTextSize(11f)
//                data.setValueTextColor(Color.BLACK)
//                data.setValueTypeface(tf)
                binding.pieChart!!.data = PieData(dataSet)

                val legend: Legend = binding.pieChart.legend
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.setDrawInside(false)
                legend.textSize = 15f
                legend.xOffset = 20f
                legend.isEnabled = false

                // Customize the legend entries to display country labels with percentages
                val legendEntries = mutableListOf<LegendEntry>()
                listOfCountries.forEachIndexed { index, (country, _) ->
//                    val percentage = calculatePercentage(listOfCountries[index].second).toFloat()
                    val legendEntry = LegendEntry()
                    legendEntry.label = country
                    legendEntry.formColor = colors[index]
                    legendEntries.add(legendEntry)
                }

                legend.setCustom(legendEntries)


                // undo all highlights
                binding.pieChart.highlightValues(null)
                binding.pieChart.invalidate()
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) return
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", xIndex: " + e.x
                    + ", DataSet index: " + h!!.dataSetIndex
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }


    private fun groupItem(items: List<TransactionListData>): Map<String, List<TransactionListData>> {
        return items.groupBy { it.itemCategory }
    }

    private fun calculatePercentage(items: List<TransactionListData>, totalItemPrice: Double): Float {
//        val totalItems = items.size.toDouble() //array size
        val totalPrice = items.sumByDouble { it.amountValue.toDouble() }

//        var percentValue = String.format("%.1f".format((totalPrice / totalItemPrice) * 100))


        return (totalPrice.toFloat() * 100) / totalItemPrice.toFloat()

    }
    private fun getRandomColor(): Int {
        val random = Random()
        val hue = random.nextInt(360)
        val saturation = 1.0f
        val value = 1.0f
        return Color.HSVToColor(floatArrayOf(hue.toFloat(), saturation, value))
    }

    /**
     * previous button
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun previousMonthAction() {
        selectedDate = selectedDate!!.minusMonths(1)
        sharedStatisticViewModel.updateDate(selectedDate!!)
        setMonthView(true)
    }

    /**
     * next button
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextMonthAction() {
        selectedDate = selectedDate!!.plusMonths(1)
        sharedStatisticViewModel.updateDate(selectedDate!!)
        setMonthView(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView(flag: Boolean) {
        if(flag){
            val currentMonth = selectedDate!!.monthValue.toString().padStart(2, '0') // e.g., "03"
            val currentYear = selectedDate!!.year
            transactionViewModel.getTransactionDataFromMonth(currentMonth,currentYear.toString(), "Expense").observe(this.viewLifecycleOwner) { items ->
                getTransactionData(items)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTransactionData(items: List<TransactionListData>) {
        if (items.isNotEmpty()) {
//              adapter.list = combineTransactionData(items)
            var groupItems = groupItem(items)
            val sortedGroups = groupItems.toList().sortedByDescending { it.second.size }
            val sortedGroups2 = groupItems.toList().sortedByDescending { (_, groupItemList) ->
                groupItemList.sumByDouble { it.amountValue.toDouble() }
            }
            listOfCountries = groupItems.toList()
            adapter.setData(groupItems.toList(), items)
//              adapter.notifyDataSetChanged()
            var totalItemCount = sortedGroups.flatMap { it.second }.size

            val totalItemPrice = items.sumByDouble { it.amountValue.toDouble() }
//                binding.pieChart.description.isEnabled = true //disabled the description label below the chart
//                binding.pieChart.setUsePercentValues(true)
//                binding.pieChart.setEntryLabelTextSize(13f) //set entry label text size
//                binding.pieChart.centerText = "Pie\nChart"
//                binding.pieChart.setCenterTextSize(22f)
//                binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
//                binding.pieChart.setCenterTextColor(Color.parseColor("#222222"))

            //Create an instance Typeface class, which represents a typeface or font family that can be use to style text in Android applications.
            //CreateFromAsset is the 'Asset Manager' object for the current application context
            //which can be obtained using the 'getAssets()' method,
            //The second argument is the relative path of the font file in the assets folder.
            tf = Typeface.createFromAsset(context?.assets, "OpenSans-Regular.ttf")

            //Design the center text (color, font size, etc.)
            binding.pieChart.setCenterTextTypeface(
                Typeface.createFromAsset(
                    context?.assets,
                    "OpenSans-Light.ttf"
                )
            )

            //Set position of the chart
            binding.pieChart.setExtraOffsets(40f, 25f, 40f, 25f)
//                binding.pieChart.setExtraOffsets(50f, 25f, 0f, 25f)
//                binding.pieChart.renderer = CustomPieChartRenderer(binding.pieChart, 10f)

//                val entries: ArrayList<PieEntry> = ArrayList()
//                entries.add(PieEntry(50f, "A"))
//                entries.add(PieEntry(50f, "B"))
//                entries.add(PieEntry(10f, "C"))
//                entries.add(PieEntry(10f, "D"))
//                entries.add(PieEntry(25f, "E"))
//                entries.add(PieEntry(25f, "F"))
//                entries.add(PieEntry(5f, "G"))
//                entries.add(PieEntry(12f, "H"))

            val entries: MutableList<PieEntry> = ArrayList()

            //Create object array to store the data
            sortedGroups2.forEachIndexed { _, (country, items) ->
                val percentage = calculatePercentage(items, totalItemPrice)
                val entry = PieEntry(percentage, country)
                entries.add(entry)
            }
            // Create a dataset with the data and customize its appearance
            val dataSet = PieDataSet(entries, "Pie chart")

            // Chart colors
            val colors = CustomColors.pieChartColors

            dataSet.colors = colors
            dataSet.setValueTextColor(Color.BLACK);
//                dataSet.valueTextColor = Color.BLACK;
            binding.pieChart.rotationAngle = 10f
            binding.pieChart.isRotationEnabled = false
            binding.pieChart.animateY(1000, Easing.EaseInOutQuad)

            binding.pieChart.setDrawEntryLabels(false); // Disable drawing the entry labels inside the slices
            binding.pieChart.setEntryLabelTextSize(14f); // Set the text size of the labels
            binding.pieChart.setEntryLabelColor(Color.BLACK); // Set the color of the entry labels

            // Value lines
//                dataSet.valueLinePart1Length = 0.3f
//                dataSet.valueLinePart2Length = 1.0f
            dataSet.valueLineWidth = 2f
//                dataSet.valueLinePart1OffsetPercentage = 115f  // Line starts outside of chart
            dataSet.isUsingSliceColorAsValueLineColor = false // Line with color

            // Value text appearance
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

//                dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE;
//                dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE // display text & label outside
            dataSet.valueTextSize = 16f // size of value text
            dataSet.valueTypeface = Typeface.DEFAULT_BOLD

            // Value formatting
//                dataSet.valueFormatter = object : ValueFormatter() {
//                    private val formatter = NumberFormat.getPercentInstance()
//
//                    override fun getFormattedValue(value: Float) =
//                        formatter.format(value / 100f)
//                }
            binding.pieChart.setUsePercentValues(true)

            dataSet.selectionShift = 5f

            // Hole
            binding.pieChart.isDrawHoleEnabled = true
            binding.pieChart.holeRadius = 50f

            // Center text
            binding.pieChart.setDrawCenterText(true)
            binding.pieChart.setCenterTextSize(20f)
            binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            binding.pieChart.setCenterTextColor(Color.parseColor("#222222"))
            binding.pieChart.centerText = "Center\ntext"

            // Disable legend & description
            binding.pieChart.legend.isEnabled = false
            binding.pieChart.description = null
            val maxEntries = 1
            var entryCount = 0

            dataSet.valueFormatter = object : ValueFormatter() {
                private val formatter = NumberFormat.getPercentInstance()
                override fun getFormattedValue(value: Float): String {
//                        if(data!!.dataSetCount > 0){
//                            val firstDataSet = data!!.getDataSetByIndex(0) as PieDataSet
//                            val valueFormatter = firstDataSet.valueFormatter
//                            valueFormatter
//                            Log.d("firstDataSet","$firstDataSet")
//                            Log.d("firstDataSet","$valueFormatter")
//                        }
                    val roundedValue = value.roundTo(2)

                    val formattedValue = String.format("%.1f", value)
                    var valueR = "%.1f".format(value).toFloat()

                    var entryw: String? = null
                    var spannableString: SpannableString? = null

                    for (slice in entries) {
                        slice?.label.also { entryw = it }
                        if(entries.size == uniqueCategory.size){
                            uniqueCategory.clear()
                            entryCount = 0
                        }
                        val roundedValue2 = slice.y.roundTo(2)

                        if (roundedValue== roundedValue2 && !uniqueCategory.contains(entryw)) {
                            uniqueCategory.add(entryw.toString())

//                                        val entry = dataSet.values.find { it.y == value }
                            val category = entryw ?: ""

                            val percentValue = String.format("%.1f%%", value)

                            if (entryCount < 2) {
                                // Create a SpannableString with the category on top and the value below
                                spannableString = SpannableString("$category\n$percentValue")
                                spannableString.setSpan(
                                    StyleSpan(Typeface.BOLD),
                                    0,
                                    category.length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                entryCount++ // Increment the entry count
                                break // Stop processing after 8 entries
                            }else{
                                break;
                            }
                        }
                    }

                    return spannableString?.toString() ?: "" // Return an empty string if spannableString is null
                }
            }



            binding.pieChart.data = PieData(dataSet)
            binding.pieChart.invalidate()


//                /** First Circle */
//                //First Circle: //creating a "hole" if set true without this property also true
//                binding.pieChart.isDrawHoleEnabled = true
//                //First Circle: //set hole back-ground color to white
//                binding.pieChart.setHoleColor(Color.WHITE)
//                //First Circle: SetHoleRadius method take a float value that represents the radius of the hole.
//                binding.pieChart.holeRadius = 58f //set radius to 58 units (pixels)
//                //Set Text to center
//                binding.pieChart.setDrawCenterText(true)
//
//                /** Second Circle */
//                //Second Circle: Set the size Hole which nex to the hole
//                binding.pieChart.transparentCircleRadius = 61f //set circle look blur
//                //Second Circle: Set circle color
//                binding.pieChart.setTransparentCircleColor(Color.RED)
//                binding.pieChart.setTransparentCircleAlpha(110)
//
//                /** Third Circle */
//                //Set starting rotation angle, 0 represent starting from "east" or "3 o'clock rotation clockwise
//                binding.pieChart.rotationAngle = 0f
//                //Enable rotation of the chart by touch, default is true
//                binding.pieChart.isRotationEnabled = true
//                //Enable stand out visually when tapping or clicking on a data point in the chart, default is true
//                binding.pieChart.isHighlightPerTapEnabled = true
//                //animate pie-chart from Y direction
//                binding.pieChart.animateY(1000, Easing.EaseInOutQuad)
//                binding.pieChart.setDrawEntryLabels(false)
//                //add a selection listener
//                binding.pieChart.setOnChartValueSelectedListener(this)
////
//                val legend: Legend = binding.pieChart.legend
//                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//                legend.orientation = Legend.LegendOrientation.VERTICAL
//                legend.setDrawInside(false)
//                legend.isEnabled = false
//
////                 Customize the legend entries to display country labels with percentages
//                val legendEntries = mutableListOf<LegendEntry>()
//                listOfCountries.forEachIndexed { index, (country, _) ->
//                    val percentage = calculatePercentage(listOfCountries[index].second).toFloat()
//                    val label = "$country: $percentage%"
//                    val legendEntry = LegendEntry()
//                    legendEntry.label = label
//                    legendEntries.add(legendEntry)
//                }
//
//                // Call a method to create data for your pie chart
//                setData();
        }else {
            (binding.statisticRecycleView.adapter as CountryAdapter).setEmptyData(emptyList())
            binding.pieChart.clear()
            binding.pieChart.setNoDataText("No data available")
            binding.pieChart.setNoDataTextColor(Color.GRAY)
            binding.pieChart.invalidate()
        }

//        monthYearTextView!!.text = monthYearFromDate(selectedDate!!)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onPreviousAction() {
        // Your custom logic
        previousMonthAction()
        Log.d("StatisticFragment", "Previous action triggered")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onNextAction() {
        // Your custom logic
        nextMonthAction()
        Log.d("StatisticFragment", "Next action triggered")
    }

}

private fun Float.roundTo(i: Int): Any {
    val factor = 10.0.pow(i)
    return round(this * factor) / factor
}



//fun Double.roundTo(decimals: Int): Double {
//    val factor = 10.0.pow(decimals)
//    return round(this * factor) / factor
//}
