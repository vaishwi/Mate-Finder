package com.example.mateconnect.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userId: String="",
    var name: String="",
    var isGroup: Boolean=false,
    var email: String="",
    var password: String="",
    var sex:String="",
    var address: String="",
    var university: String="",
    var hometown: String="",
    var currentAddress:String="",
    var previousWorkLocation:String="",
    var previousWorkOrganisation:String="",
    var roomPreference:String="",
    var lookingFor:String="",
    var foodPreference:String="",
    var pinnedConnections: ArrayList<String> = ArrayList<String>()
) : Parcelable {
}
