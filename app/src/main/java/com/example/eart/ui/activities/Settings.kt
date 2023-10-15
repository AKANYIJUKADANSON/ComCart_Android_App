package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivitySettingsBinding

class Settings : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        setUpActionBar()

        binding.accountSettings.setOnClickListener(this)
        binding.passwordSettings.setOnClickListener(this)
        binding.addressSettings.setOnClickListener(this)

    }

    private fun setUpActionBar(){
        val myToolBar = binding.toolbarSettings
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