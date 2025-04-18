package com.example.inventory.adapters

import android.os.Parcel
import android.os.Parcelable
import com.example.inventory.data.CategorySettings
import com.example.inventory.data.Item
import com.example.inventory.data.SubCategoryLists

data class CategorySingleData(val items: CategorySettings): Parcelable{
    constructor(parcel: Parcel) : this(
        CategorySettings(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.createTypedArrayList(SubCategoryLists.CREATOR) ?: emptyList()
        )
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(items.expenseId)
        parcel.writeInt(items.imageResourceId)
        parcel.writeString(items.title)
        parcel.writeTypedList(items.mList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategorySingleData> {
        override fun createFromParcel(parcel: Parcel): CategorySingleData {
            return CategorySingleData(parcel)
        }

        override fun newArray(size: Int): Array<CategorySingleData?> {
            return arrayOfNulls(size)
        }
    }
}
