package com.example.cobafirebase.models

import android.os.Parcel
import android.os.Parcelable

data class Kas(
    val userId: String? = null,
    val amount: Double = 0.0,
    val date: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeDouble(amount)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Kas> {
        override fun createFromParcel(parcel: Parcel): Kas {
            return Kas(parcel)
        }

        override fun newArray(size: Int): Array<Kas?> {
            return arrayOfNulls(size)
        }
    }
}
