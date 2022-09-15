package com.example.edutech.firestore

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.modules.Constants
import com.example.eart.ui.activities.Registration
import com.example.edutech.modules.MyUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserInfoFirestore: BaseActivity() {

    private val mFirestore = FirebaseFirestore.getInstance()

    //Creating a function using data from the registration activity and MyUser data class
    fun storeUserInfo(activity:Activity, UserInfo:MyUser){

        //Creating a new collection that will act as a folder under which all user details will be stored
        mFirestore.collection("UserInfo")
                // Document is the user id given after registration
            .document(UserInfo.id)
                // Here we are using the fields created inMyUser class with help of UserInfo
                // Merge helps to change or add data in any field
            .set(UserInfo, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity,"User Data Saved success.",
                    Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, " Data storage Failure",
                    Toast.LENGTH_SHORT).show()
            }
    }


    fun storeProductInfo(activity:Activity, UserInfo:MyUser){

        //Creating a new collection that will act as a folder under which all user details will be stored
        mFirestore.collection("UserInfo")
            // Document is the user id given after registration
            .document(UserInfo.id)
            // Here we are using the fields created inMyUser class with help of UserInfo
            // Merge helps to change or add data in any field
            .set(UserInfo, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity,"User Data Saved success.",
                    Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, " Data storage Failure",
                    Toast.LENGTH_SHORT).show()
            }
    }

}