package com.example.inventory.data

import android.os.Parcel
import android.os.Parcelable
import com.example.inventory.adapters.ItemListData

class SubCategoryListData(val items: List<SubCategoryLists>) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(SubCategoryLists.CREATOR) ?: emptyList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubCategoryListData> {
        override fun createFromParcel(parcel: Parcel): SubCategoryListData {
            return SubCategoryListData(parcel)
        }

        override fun newArray(size: Int): Array<SubCategoryListData?> {
            return arrayOfNulls(size)
        }
    }
}