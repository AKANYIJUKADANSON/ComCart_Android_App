package com.example.eart.firestore

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.Addproduct
import com.example.edutech.modules.MyUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProdctInfoStore {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun storeProductInfo(activity: Addproduct, ProductInfo: PrdctDtlsClass){

        //Creating a new collection that will act as a folder under which all products details will be stored
        mFirestore.collection("Products")
            // Document is the user id given after registration
            .document(ProductInfo.productTitle)
            // Here we are using the fields created inMyUser class with help of UserInfo
            // Merge helps to change or add data in any field
            .set(ProductInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                //Hidding the progress dialog
                activity.progDialog.dismiss()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading product details", e
                )
                Toast.makeText(activity, " Product storage Failure",
                    Toast.LENGTH_SHORT).show()
            }
    }
}