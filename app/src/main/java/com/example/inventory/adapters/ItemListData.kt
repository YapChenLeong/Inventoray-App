package com.example.inventory.adapters

import android.os.Parcel
import android.os.Parcelable
import com.example.inventory.data.Item
import com.example.inventory.data.TransactionListData

data class ItemListData(val items: List<TransactionListData>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(TransactionListData.CREATOR) ?: emptyList())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemListData> {
        override fun createFromParcel(parcel: Parcel): ItemListData {
            return ItemListData(parcel)
        }

        override fun newArray(size: Int): Array<ItemListData?> {
            return arrayOfNulls(size)
        }
    }
}

