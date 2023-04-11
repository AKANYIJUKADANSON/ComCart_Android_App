package com.example.eart.modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoldProducts(
    val user_id:String = "",
    val title:String = "",
    val price:String = "",
    val sold_quantity:String = "",
    val image:String = "",
    var order_id:String = "",
    val order_date:Long = 0L,
    val sub_total:String = "",
    val delivery_charge :String = "",
    val total_amount:String = "",
    val address:Address = Address(),
    var sold_product_id:String = ""
): Parcelable
