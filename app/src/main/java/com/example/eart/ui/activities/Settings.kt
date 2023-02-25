package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setUpActionBar()
        address_layout.setOnClickListener(this)
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
                R.id.address_layout ->{
                    val intent = Intent(this, AddressList::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

}