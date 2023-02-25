package com.example.eart.modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val user_id: String = "",
    val region: String = "",
    val addressName:String = "",
    val phoneNumber:String = "",
    val zipCode:String = "",
    val address_type: String = "",
    var address_id:String = ""
):Parcelable
