package com.example.eart.modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PrdctDtlsClass(
    val user_id:String = "",
    val username:String = "",
    val productTitle:String = " ",
    val productPrice:String = "",
    val prodctDescrptn:String = "",
    var stock_quantity:String = "",
    val image:String = "",
    var product_id:String = "",
) : Parcelable