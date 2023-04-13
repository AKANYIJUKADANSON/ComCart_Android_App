package com.example.eart.modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class MyUser(
    val user_id:String = "",
    val firstName:String = "",
    val lastName:String = "",
    val emailID:String = "",
    val image:String = "",
): Parcelable