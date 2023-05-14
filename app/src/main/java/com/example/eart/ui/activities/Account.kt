package com.example.eart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.MyUser
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_settings.*

class Account : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setUpActionBar()

        //  Get user data
        getUserDetails()

        address_layout.setOnClickListener(this)

        tv_currency.setText(Constants.CURRENCY)
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.toolbar_account)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    // Get userdetails
    fun getUserDetails(){
        progressDialog("Fetching...")
        FirestoreClass().getUserDetails(this)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.address_layout ->{
                    val intent = Intent(this, AddressList::class.java)
                    startActivity(intent)
                }

                R.id.tv_edit_profile -> {
                    val intent = Intent(this, EditUserData::class.java)
                }
            }
        }
    }

    fun getUserDetailsSuccess(userDetails: MyUser){
        // Hiding the progress dialog
        hideProgressDialog()

        GlideLoader(this).loadUserPicture(
            userDetails.image, user_image
        )

        tv_user_id.text = "ID: ${userDetails.user_id}"
        tv_user_name.text = "Name: ${userDetails.firstName +" "+ userDetails.lastName}"
        tv_user_email.text = "Email: ${userDetails.emailID}"
        // tv_user_phone_number.text = userDetails.phone_number

    }

}