package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class Login : BaseActivity(), View.OnClickListener {
    lateinit var auth: FirebaseAuth
    lateinit var forgotPasswd: TextView
    lateinit var haveNoAccnt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Calling the views by id
        val lgnbtn = findViewById<Button>(R.id.loginbtn)
        forgotPasswd = findViewById(R.id.forgotPasswd)
        haveNoAccnt = findViewById(R.id.haveNoActReg)

        //Setting the onclick function to work when the views are selected
        lgnbtn.setOnClickListener(this)
        forgotPasswd.setOnClickListener(this)
        haveNoAccnt.setOnClickListener(this)

        // With no action and status bars
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun validateLoginDetails(): Boolean {
        val loginEmailID = findViewById<EditText>(R.id.loginEmail)
        val loginPassword = findViewById<EditText>(R.id.loginPassword)

        return when{
            TextUtils.isEmpty(loginEmailID.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtEmail), true)
                false
            }

            TextUtils.isEmpty(loginPassword.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtPassword), true)
                false
            }

            else ->{
                // Starting the progress dialog
                progressDialog("Authenticating.....")
                val email = findViewById<EditText>(R.id.loginEmail).text.toString().trim{it <= ' '}
                val password = findViewById<EditText>(R.id.loginPassword).text.toString().trim{it <= ' '}

                auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        progDialog.dismiss()

                        if (it.isSuccessful){
                            Toast.makeText(this, resources.getString(R.string.loginSuccess),
                                Toast.LENGTH_LONG).show()

                            val intent = Intent(this@Login, MainActivity::class.java)

                            // Clearing the previous background tasks
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else {
                            Toast.makeText(
                                this@Login,
                                it.exception?.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }
                return true
            }
        }
    }

    //Creating the onclickListener for all the clickable items
    override fun onClick(view: View?){
        if (view!= null){

            when(view.id){
                //login button
                R.id.loginbtn ->{
                    validateLoginDetails()
                }

                R.id.forgotPasswd ->{
                    startActivity(Intent(this, Forgotpasswd::class.java))
                }

                //Already have an account txtView
                R.id.haveNoActReg ->{
                    startActivity(Intent(this, Registration::class.java))
                    finish()
                }

            }
        }
    }


}