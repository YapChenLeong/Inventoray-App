package com.example.inventory.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.util.*

data class SubCategoryLists (
    val expenseId: String?,
    val subId: String?,
    val description: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(expenseId)
        parcel?.writeString(subId)
        parcel?.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<SubCategoryLists> {
        override fun createFromParcel(parcel: Parcel): SubCategoryLists {
            return SubCategoryLists(parcel)
        }

        override fun newArray(size: Int): Array<SubCategoryLists?> {
            return arrayOfNulls(size)
        }
    }
}


