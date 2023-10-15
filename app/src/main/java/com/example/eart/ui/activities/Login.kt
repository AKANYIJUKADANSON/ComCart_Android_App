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
import androidx.databinding.DataBindingUtil
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityLoginBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.MyUser
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Login : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Analytics
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, FirestoreClass().getCurrentUserID())
            param(FirebaseAnalytics.Param.ITEM_NAME, "My logs")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }

        binding.loginbtn.setOnClickListener(this)
        binding.forgotPasswd.setOnClickListener(this)
        binding.haveNoActReg.setOnClickListener(this)

        // With no action and status bars
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun validateLoginDetails(): Boolean {
        return when{
            TextUtils.isEmpty(binding.loginEmail.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtEmail), true)
                false
            }

            TextUtils.isEmpty(binding.loginPassword.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtPassword), true)
                false
            }

            else ->{
                // Starting the progress dialog
                progressDialog("Authenticating.....")
                val email = binding.loginEmail.text.toString().trim{it <= ' '}
                val password = binding.loginPassword.text.toString().trim{it <= ' '}

                auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {

                        if (it.isSuccessful){
                           FirestoreClass().getUserDetails(this)
                        }
                        else {
                            progDialog.dismiss()
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


    fun loginSuccess(user: MyUser){

        progDialog.dismiss()

        Toast.makeText(this, resources.getString(R.string.loginSuccess),
            Toast.LENGTH_LONG).show()

//        val intent = Intent(this@Login, Dashboard::class.java)
        val intent = Intent(this@Login, Dashboard::class.java)

        // Clearing the previous background tasks
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}