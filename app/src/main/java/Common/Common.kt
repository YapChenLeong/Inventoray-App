package Common

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.inventory.data.Item
import com.example.inventory.data.TransactionListData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

object Common {
    val VIEWTYPE_GROUP = 0
    val VIEWTYPE_ITEM = 1
    val VIEWTPYE_NO_DATA = -1


    fun sortDate(list:MutableList<Item>): MutableList<Item> {
        list.sortWith(Comparator {
                item1,item2 -> item1!!.date!!.compareTo(item2!!.date!!)
        })
        return list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterDate(currentDate: Calendar, list: List<Item>, localDate: LocalDate?): List<Any>{
        val listOfItems : MutableList<Any> = mutableListOf()
//        var a = currentDate[Calendar.YEAR]
//        var b = currentDate[Calendar.MONTH]
//        var c= currentDate[Calendar.DAY_OF_MONTH]
//        var d= currentDate[Calendar.DAY_OF_YEAR]
//        val dateFormat = "dd/MM/yyyy (EEE)"
//        val sdf = SimpleDateFormat(dateFormat, Locale.UK)
//        var currentDat = sdf.format(currentDate.time)
        var item = Calendar.getInstance()

        localDate?.let {
            var localMonth = it.monthValue
            var localYear = it.year

            for( listOfData in list ) {
                item.time = listOfData.date
                var itemMonth = item[Calendar.MONTH] + 1
                var itemYear = item[Calendar.YEAR]

                if (localYear == itemYear && localMonth == itemMonth) {
                    listOfItems.add(listOfData)
                }
            }
        }


        return listOfItems
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterTransactionDate(currentDate: Calendar, list: List<TransactionListData>, localDate: LocalDate?): List<TransactionListData>{
        val listOfItems : MutableList<TransactionListData> = mutableListOf()
//        var a = currentDate[Calendar.YEAR]
//        var b = currentDate[Calendar.MONTH]
//        var c= currentDate[Calendar.DAY_OF_MONTH]
//        var d= currentDate[Calendar.DAY_OF_YEAR]
//        val dateFormat = "dd/MM/yyyy (EEE)"
//        val sdf = SimpleDateFormat(dateFormat, Locale.UK)
//        var currentDat = sdf.format(currentDate.time)
        var item = Calendar.getInstance()

        localDate?.let {
            var localMonth = it.monthValue
            var localYear = it.year

            for( listOfData in list ) {
                item.time = listOfData.dtUpdate
                var itemMonth = item[Calendar.MONTH] + 1
                var itemYear = item[Calendar.YEAR]

                if (localYear == itemYear && localMonth == itemMonth) {
                    listOfItems.add(listOfData)
                }
            }
        }
        return listOfItems
    }

    fun combineDates(list:MutableList<Item>):MutableList<Item>{
        var i: Int
        var id: Int
        var newItemList: MutableList<Item> = ArrayList()
        var firstDate: Date? = list[0].date

        id = -1
        newItemList.add(Item(id, "null","null",0.00,0, firstDate, VIEWTYPE_GROUP))

        i = 0
        while(i<list.size-1){
            val dateFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.UK)

            //China,Malaysia, US
            var date1 = list[i].date//get the first character
            var dateConverted1 = sdf.format(date1)

            //Malaysia,US, US
            var date2 = list[i+1].date
            var dateConverted2 = sdf.format(date2)

            if(dateConverted1 == dateConverted2){
                list[i].viewType = VIEWTYPE_ITEM
                newItemList.add(list[i])
            }else{
                list[i].viewType = VIEWTYPE_ITEM
                newItemList.add(list[i])
                id -= 1
                newItemList.add(Item(id, "null","null",0.00,0, date2, VIEWTYPE_GROUP))
            }
            i++
        }

        list[i].viewType = VIEWTYPE_ITEM
        newItemList.add(list[i])
        return newItemList
    }

    fun sortCountry(list:MutableList<Item>):MutableList<Item>{
        list.sortWith(Comparator {
                item1,item2 -> item1!!.country!!.compareTo(item2!!.country!!)
        })
        return list
    }

    fun combineItems(list:MutableList<Item>):MutableList<Item>{
        var i: Int
        var id: Int
        var newItemList: MutableList<Item> = ArrayList()
        var firstCountry = list[0].country

        id = -1
        newItemList.add(Item(id, firstCountry,"null",0.00,0, null, VIEWTYPE_GROUP))

        i = 0
        while(i<list.size-1){
            //China,Malaysia, US
            var country1 = list[i].country//get the first character
            //Malaysia,US, US
            var country2 = list[i+1].country

            if(country1 == country2){
                list[i].viewType = VIEWTYPE_ITEM
                newItemList.add(list[i])
            }else{
                list[i].viewType = VIEWTYPE_ITEM
                newItemList.add(list[i])
                id -= 1
                newItemList.add(Item(id, country2,"null",0.00,0, null, VIEWTYPE_GROUP))
            }
            i++
        }

        list[i].viewType = VIEWTYPE_ITEM
        newItemList.add(list[i])
        return newItemList
    }

    fun generateCurrentDateTime(): Date {
        var defaultCalendar = Calendar.getInstance()

        return defaultCalendar.time
    }


}