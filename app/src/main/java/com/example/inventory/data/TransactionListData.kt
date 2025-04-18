package com.example.inventory.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "T_TRANSACTION_DATA")
data class TransactionListData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "IMG_RESOURCE_ID")
    @DrawableRes val imgResourceID: Int,
    @ColumnInfo(name = "TYPE")
    val type: String, // to indicate which transaction type
    @ColumnInfo(name = "ITEM_CATEGORY")
    val itemCategory: String,
    @ColumnInfo(name = "DSCP_01")
    val description01: String,
    @ColumnInfo(name = "DSCP_02")
    val description02: String?, // make it nullable
    @ColumnInfo(name = "DSCP_03")
    val description03: String?, // make it nullable
    @ColumnInfo(name = "DT_CREATE")
    val dtCreate: Date?,
    @ColumnInfo(name = "DT_UPDATE")
    val dtUpdate: Date?,
    @ColumnInfo(name = "AMOUNT_VALUE")
    val amountValue: String,
    @ColumnInfo(name = "viewType")
    var viewType: Int = -1
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readSerializable() as? Date,
        parcel.readSerializable() as? Date,
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(type)
        parcel.writeString(itemCategory)
        parcel.writeString(description01)
        parcel.writeString(description02)
        parcel.writeString(description03)
        parcel.writeSerializable(dtCreate)
        parcel.writeSerializable(dtUpdate)
        parcel.writeString(amountValue)
        parcel.writeInt(viewType)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionListData> {
        override fun createFromParcel(parcel: Parcel): TransactionListData {
            return TransactionListData(parcel)
        }
        override fun newArray(size: Int): Array<TransactionListData?> {
            return arrayOfNulls(size)
        }

        fun generateSysGuid(): String {
            // Implement logic to generate SYS_GUID()
            // For example, using java.util.UUID.randomUUID().toString() or any other method
            return java.util.UUID.randomUUID().toString()
        }
    }
}
