package com.example.eart.ui.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.eart.R
import com.example.eart.modules.Constants
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedpreferences = getSharedPreferences(Constants.MYAPP_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedpreferences.getString(Constants.LOGGED_IN_USERNAME, "")

        val txt:TextView = findViewById(R.id.txtViewMain)
        txt.text = "The loged in user is $username "




    }
}