package com.example.mateconnect.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Connection(
    val name: String = "") : Parcelable {
}
