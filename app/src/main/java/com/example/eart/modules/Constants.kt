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

    const val CURRENCY:String = "UGX"

    // Collections in firestore
    const val PRODUCTS:String = "products"
    const val USERS:String = "users"
    const val USER_ID:String = "user_id"
    const val FAVORITE:String = "favorite"

    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val MYAPP_PREFERENCES:String = "yAppPrefsM"
    const val PICK_IMAGE_CODE = 1
    const val READ_EXTERNAL_STORAGE_CODE = 2
    const val USER_PROFILE_IMAGE:String = "user_profile_image"
    const val PRODUCT_IMAGE:String = "Product_image"
    // PRODUCT_EXTRA_ID will store the documentId of the product
    const val PRODUCT_EXTRA_ID:String = "product_extra_id"
    const val PRODUCT_EXTRA_OWNER_ID:String = ""
    const val DEFAULT_ITEM_QUANTITY:String = "1"
    const val CART_ITEMS:String = "cart_items"
    const val PRODUCT_ID:String = "product_id"
    const val CART_QUANTITY:String = "cart_quantity"
    const val STOCK_QUANTITY:String = "stock_quantity"

    // ADDRESS ACTIVITY
    const val ADDRESS:String = "Address"
    // Address radio btn values
    const val HOME:String = "Home"
    const val OFFICE:String = "Office"
    const val OTHER:String = "Other"

    // If address selecting or editing/deleting
    const val EXTRA_SELECT_ADDRESS:String = "extra_select_address"
    // The actual address that is selected
    const val EXTRA_SELECTED_ADDRESS:String = "extra_selected_address"

    // Orders
    const val ORDERS:String = "Orders"
    const val EXTRA_MY_ORDER_DETAILS:String = "extra_my_order_details"


    // Sold Products
    const val SOLD_PRODUCTS:String = "sold_products"
    const val SOLD_PRODUCT_EXTRA_ID:String = "sold_product_id"
    const val EXTRA_SOLD_PRODUCT_DETAILS:String = "extra_sold_product_details"
    const val CATEGORIES:String = "categories"


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
    fun getFileExtension(activity: Activity, url: Uri?): String? {
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
            .getExtensionFromMimeType(activity.contentResolver.getType(url!!))

        // Uri is the link to the image
    }

}