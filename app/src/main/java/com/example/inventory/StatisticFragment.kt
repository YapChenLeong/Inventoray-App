package com.example.inventory

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.inventory.databinding.FragmentStatisticBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class StatisticFragment : Fragment() {
    private var _binding: FragmentStatisticBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentStatisticBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowCustomEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(R.string.statistic)

        binding.pieChart.description.isEnabled = false //disabled the description label below the chart
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.setEntryLabelTextSize(12f) //set entry label text size
        binding.pieChart.centerText = "Pie Chart"
        binding.pieChart.setCenterTextSize(22f)



        val l: Legend = binding.pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = false

        // Call a method to create data for your pie chart
        setData();

        return binding.root
    }

    private fun setData() {
        //Create object array to store the data
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(10f, "Entry 1"))
        entries.add(PieEntry(30f, "Entry 2"))
        entries.add(PieEntry(50f, "Entry 3"))
        entries.add(PieEntry(5f, "Entry 4"))
        entries.add(PieEntry(5f, "Entry 5"))

        //Create dataset to provide styling and formatting options for the pie chart
        val dataSet = PieDataSet(entries, "Pie Chart")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        dataSet.sliceSpace = 3f //set the space between each pie entry
        dataSet.selectionShift = 5f
        //dataSet.setSelectionShift(0f);
        //dataSet.setSelectionShift(0f);

        dataSet.valueLinePart1OffsetPercentage = 80f // arrow line height size
        dataSet.valueLinePart1Length = 0.4f //value height
        dataSet.valueLinePart2Length = 0.4f //value width
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        //Create PieData Object
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(24f)
        data.setValueTextColor(Color.BLACK)

        binding.pieChart!!.data = data
        binding.pieChart.invalidate()
    }
}