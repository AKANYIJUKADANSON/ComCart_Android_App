package com.example.eart.modules

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    /** Using constants helps not to change the meaning of a value
     *  For example the name of the field in the firestore for fname will not change but only the value
     */

    // Collections in firestore
    const val PRODUCTS:String = "products"
    const val USERS:String = "users"
    const val USER_ID:String = "user_id"

    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val MYAPP_PREFERENCES:String = "yAppPrefsM"
    const val PICK_IMAGE_CODE = 1
    const val READ_EXTERNAL_STORAGE_CODE = 2
    const val USER_PROFILE_IMAGE:String = "User_profile_image"
    const val PRODUCT_IMAGE:String = "Product_image"
    const val PRODUCT_EXTRA_ID:String = "product_extra_id"
    const val PRODUCT_EXTRA_OWNER_ID:String = ""
    const val DEFAULT_ITEM_QUANTITY:String = "1"
    const val CART_ITEMS:String = "cart_items"
    const val PRODUCT_ID:String = "product_id"
    const val CART_QUANTITY:String = "cart_quantity"

    // Intent extra constants.
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"

    // ADDRESS ACTIVITY
    const val ADDRESS:String = "Address"
    // Address radio btn values
    const val HOME:String = "Home"
    const val OFFICE:String = "Office"
    const val OTHER:String = "Other"


    fun imageChooser(activity: Activity){
        /**
         * we are passing the activity parameter coz we are to use it in any other activities
         * Reading the image from phone media storage
         */

        val imagePicker = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(imagePicker, PICK_IMAGE_CODE)
    }

    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

        // Uri is the link to the image
    }
}