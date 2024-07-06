package com.example.cobafirebase.models

import android.os.Parcel
import android.os.Parcelable

data class Contacts(
    val id: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val alamat: String? = null,
    val jabatan: String? = null,
    val imgUri: String? = null,
    val kasList: List<Kas>? = null  // Menambahkan field untuk daftar kas warga
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Kas)  // Menambahkan pembacaan dari parcel untuk kasList
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
        parcel.writeString(email)
        parcel.writeString(alamat)
        parcel.writeString(jabatan)
        parcel.writeString(imgUri)
        parcel.writeTypedList(kasList)  // Menambahkan penulisan ke parcel untuk kasList
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contacts> {
        override fun createFromParcel(parcel: Parcel): Contacts {
            return Contacts(parcel)
        }

        override fun newArray(size: Int): Array<Contacts?> {
            return arrayOfNulls(size)
        }
    }
}
