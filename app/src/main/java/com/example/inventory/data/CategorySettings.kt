package com.example.inventory.data

import android.os.Parcel
import android.os.Parcelable
import com.example.inventory.adapters.CategorySingleData

data class CategorySettings(
    val expenseId: String,
    val imageResourceId: Int,
    val title: String,
    val mList: List<SubCategoryLists>
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(SubCategoryLists.CREATOR) ?: emptyList()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(expenseId)
        parcel.writeInt(imageResourceId)
        parcel.writeString(title)
        parcel.writeTypedList(mList)
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
