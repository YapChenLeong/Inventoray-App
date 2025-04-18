package com.example.inventory.adapters

import Common.CustomColors
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.data.TransactionListData
import java.text.DecimalFormat
import java.util.*

class CountryAdapter (private val onItemClicked: (List<TransactionListData>) -> Unit) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    private var totalItemPrice: Double = 0.0
    private lateinit var allColors: List<Int>
    private var listOfCountries: List<Pair<String, List<TransactionListData>>> = emptyList()
    private var totalItemCount: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cateroy_country_list_item, parent, false)
        return CountryViewHolder(view)
    }
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val (country, items) = listOfCountries[position]
        val color = allColors[position]

        /** Percentage of each group country with random color*/
        // Generate a random color with transparency
        val shapeDrawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.percentage_background)
        shapeDrawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        // Apply the shape drawable as the background of the percentage view
        holder.percentageTextView.background = shapeDrawable
        // Apply the percentage text
        holder.percentageTextView.text = "${calculatePercentage(items, totalItemPrice)}"

        /** Country Name & Total counts items of each group country*/
        var totalCount = "${items.size}"
        holder.countryTextView.text = "$country ($totalCount)"
        /** Total sum price items of each country*/
        holder.priceTextView.text = "${calculateTotalPrice(items, "RM")}"

    }

    override fun getItemCount(): Int {
        return listOfCountries.size
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val percentageTextView: TextView = itemView.findViewById(R.id.item_percentage)
        val countryTextView: TextView = itemView.findViewById(R.id.item_country)
        val priceTextView: TextView = itemView.findViewById(R.id.item_price)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val (_, items) = listOfCountries[position]
                    if (items.isNotEmpty()) {
                        onItemClicked.invoke(items)
                    }
                }
            }
        }
    }

    fun setData(countries: List<Pair<String, List<TransactionListData>>>, items: List<TransactionListData>) {

        val sortedGroups = countries.sortedByDescending { it.second.size }
        val sortedGroups2 = countries.toList().sortedByDescending { (_, groupItemList) ->
            groupItemList.sumByDouble { it.amountValue.toDouble() }
        }
        this.listOfCountries = sortedGroups2
        this.totalItemCount = countries.flatMap { it.second }.size
        this.allColors = CustomColors.pieChartColors
        this.totalItemPrice = items.sumByDouble { it.amountValue.toDouble() }
        notifyDataSetChanged()
    }

    fun setEmptyData(newList: List<Pair<String, List<TransactionListData>>>) {
        listOfCountries = newList
        notifyDataSetChanged() // fixed!
    }

    private fun calculatePercentage(items: List<TransactionListData>, totalItemPrice: Double): String {
        //Example total transaction is 5 items
        // 3/5 * 100 = 60%
        val totalPrice = items.sumByDouble { it.amountValue.toDouble() }
        val percentage = (totalPrice / totalItemPrice) * 100
        return if (percentage >= 1) {
            "%.0f".format(percentage) + "%"
        }else{
            "%.2f".format(percentage) + "%"
        }
    }

    private fun calculateTotalPrice(items: List<TransactionListData>, currencySymbol: String): String {
        val totalPrice = items.sumByDouble { it.amountValue.toDouble() }
        val decimalFormat = DecimalFormat("#.00")
        val formattedPrice = decimalFormat.format(totalPrice)

        return "$currencySymbol$formattedPrice"
    }

    private fun getRandomColor(): Int {
        val random = Random()
        val hue = random.nextInt(360)
        val saturation = 1.0f
        val value = 1.0f
        return Color.HSVToColor(floatArrayOf(hue.toFloat(), saturation, value))
    }
}
