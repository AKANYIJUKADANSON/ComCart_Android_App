package com.example.eart.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.MyUser
import kotlinx.android.synthetic.main.activity_edit_user_data.*

class EditUserData : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_data)

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
            userDetails.image, edit_user_image
        )

        tv_edit_user_id.text = userDetails.user_id
        // tv_user_phone_number.text = userDetails.phone_number

    }
}