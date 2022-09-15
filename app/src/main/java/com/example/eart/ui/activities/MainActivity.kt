package com.example.eart.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eart.R
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val x = FirebaseStorage.getInstance()
    }
}