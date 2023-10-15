package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityAccountBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.MyUser

class Account : BaseActivity(), View.OnClickListener {
    lateinit var binding:ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)
        setUpActionBar()

        //  Get user data
        getUserDetails()

        binding.addressLayout.setOnClickListener(this)

        binding.tvCurrency.setText(Constants.CURRENCY)
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
            userDetails.image, binding.userImage
        )

        binding.tvUserId.text = "ID: ${userDetails.user_id}"
        binding.tvUserName.text = "Name: ${userDetails.firstName +" "+ userDetails.lastName}"
        binding.tvUserEmail.text = "Email: ${userDetails.emailID}"
        // tv_user_phone_number.text = userDetails.phone_number

    }

}