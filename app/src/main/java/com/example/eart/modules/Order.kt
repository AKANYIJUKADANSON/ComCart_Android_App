package com.example.eart.modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val user_id:String = "",
    val items:ArrayList<CartItem> = ArrayList(),
    val address:Address = Address(),
    val order_title:String = "",
    val order_image:String = "",
    val sub_total:String = "",
    val delivery_charge :String = "",
    val total_amount:String = "",
    val order_date:Long = 0L,
    var order_id:String = ""

) : Parcelable
