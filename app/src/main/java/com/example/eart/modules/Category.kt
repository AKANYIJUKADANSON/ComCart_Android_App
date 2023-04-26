package com.example.eart.modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val user_id:String = "",
    val category_title:String = "",
    val category_description:String = "",
    val date_created:String = "",
    var category_id:String = ""
): Parcelable
