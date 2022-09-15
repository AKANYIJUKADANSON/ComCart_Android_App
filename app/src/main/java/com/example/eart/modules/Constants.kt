package com.example.eart.modules

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {

    // Collections in firestore
    const val PRODUCTS:String = "products"
    const val USERS:String = "users"

    const val PICK_IMAGE_CODE = 1
    const val READ_EXTERNAL_STORAGE_CODE = 2
    const val PRODUCT_IMAGE = "ProductImage"

    fun imageChooser(activity: Activity){
        //we are passing the activity parameter coz we are to use it in any other activities
        val imagePick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(imagePick, PICK_IMAGE_CODE)
    }
}