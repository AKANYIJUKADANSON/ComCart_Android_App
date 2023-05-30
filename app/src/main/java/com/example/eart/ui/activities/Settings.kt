package com.example.eart.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.MyUser
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setUpActionBar()

        account_settings.setOnClickListener(this)
        password_settings.setOnClickListener(this)
        address_settings.setOnClickListener(this)

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



    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.account_settings -> {
                    val intent = Intent(this, Account::class.java)
                    startActivity(intent)
                }

                R.id.address_settings ->{
                    val intent = Intent(this, AddressList::class.java)
                    startActivity(intent)
                }

                R.id.password_settings -> {
                    val intent = Intent(this, Forgotpasswd::class.java)
                    startActivity(intent)
                }
            }
        }
    }



}