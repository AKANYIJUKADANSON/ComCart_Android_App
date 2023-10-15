package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityForgotpasswdBinding
import com.google.firebase.auth.FirebaseAuth

class Forgotpasswd : BaseActivity() {
    private lateinit var binding: ActivityForgotpasswdBinding

    lateinit var forgtpsswrd: Button
    lateinit var forgtpsswrdEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgotpasswd)

        forgtpsswrd = findViewById(R.id.forgotPasswdBtn)
        forgtpsswrd.setOnClickListener {
            changePassword()
        }

    }

    private fun changePassword():Boolean{

        forgtpsswrdEmail = binding.forgotPasswdEmail

        return when{
            TextUtils.isEmpty(forgtpsswrdEmail.text.toString().trim { it<= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtEmail), true)
                false
            }
            else -> {
                progressDialog("Sending....")
                FirebaseAuth.getInstance().sendPasswordResetEmail(forgtpsswrdEmail.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            progDialog.dismiss()
                            Toast.makeText(
                                this,
                                resources.getString(R.string.emailSentSuccess),
                                Toast.LENGTH_LONG
                            ).show()

                            val intent = Intent(this, Login::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                true
            }
        }

    }
}