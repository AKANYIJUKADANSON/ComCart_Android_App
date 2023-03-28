package com.example.eart.modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val user_id:String = "",
    val product_id:String = "",
    val product_title:String = "",
    val product_price:String = "",
    val product_image:String = "",
    var cart_quantity :String = "",
    var stock_quantity:String = "",
    var cartItemId:String = ""
) :Parcelable
