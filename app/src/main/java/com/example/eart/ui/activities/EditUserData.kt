package com.example.eart.ui.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityEditUserDataBinding
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.MyUser

class EditUserData : BaseActivity() {
    private lateinit var binding: ActivityEditUserDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_user_data)

        // Action bar
        setUpActionBar()
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.toolbar_settings)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    fun editUserDetailsSuccess(userDetails: MyUser){
        // Hiding the progress dialog
        hideProgressDialog()

        GlideLoader(this).loadUserPicture(
            userDetails.image, binding.editUserImage
        )

        binding.tvEditUserId.text = userDetails.user_id
        // tv_user_phone_number.text = userDetails.phone_number

    }
}