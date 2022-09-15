package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.modules.AuthUserInfo
import com.example.edutech.firestore.UserInfoFirestore
import com.example.edutech.modules.MyUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Registration : BaseActivity(), View.OnClickListener {

    lateinit var registrationButton: Button
    lateinit var haveAcctLgn: TextView
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //capturing views to use in the onclick function
        registrationButton = findViewById(R.id.regbtn)
        haveAcctLgn = findViewById(R.id.haveActLogin)

        //Setting the onclick function for views to function
        registrationButton.setOnClickListener(this)
        haveAcctLgn.setOnClickListener(this)


    }

    fun validateRegDetails(): Boolean{
        //capturing what the user enters using ids and changing them into text format
        val regFirstName = findViewById<EditText>(R.id.regFirstName)
        val regLastName = findViewById<EditText>(R.id.regLastName)
        val regEmail = findViewById<EditText>(R.id.regEmail)
        val regPasswrd = findViewById<EditText>(R.id.regPassword)
        val regConfrmPaswd = findViewById<EditText>(R.id.regConfrmPassword)
        val agreeTermsCondtns = findViewById<CheckBox>(R.id.agreeTermsCondtns)



        return when{
            TextUtils.isEmpty(regFirstName.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtFirstName), true)
                false
            }
            //errorMessage, we are passing true coz if its true we have to go to the showsnackbar and run the function

            TextUtils.isEmpty(regLastName.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtLastName), true)
                false
            }

            TextUtils.isEmpty(regEmail.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtEmail), true)
                false
            }

            TextUtils.isEmpty(regPasswrd.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtPassword), true)
                false
            }

            regConfrmPaswd.text.toString().length < 8 ->{
                showErrorSnackBar(resources.getString(R.string.errorEtShortPaswd), true)
                false
            }

            TextUtils.isEmpty(regConfrmPaswd.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.errorEtConfrmPassword), true)
                false
            }

            //Comparing the first and last password
            regPasswrd.text.toString().trim { it <= ' ' } != regConfrmPaswd.text.toString().trim{it <= ' '} ->{
                showErrorSnackBar(resources.getString(R.string.errorComparePassword), true)
                false
            }

            !agreeTermsCondtns.isChecked ->{
                showErrorSnackBar(resources.getString(R.string.errorTermsConditions), true)
                false
            }
            // What to do if everything is valid
            else -> {
                progressDialog("Authenticating....")

                // NOTE: all these always have to be captured again to be used in the authentication
                val firstName = findViewById<EditText>(R.id.regFirstName).text.toString().trim{it <= ' '}
                val lastName = findViewById<EditText>(R.id.regLastName).text.toString().trim{it <= ' '}
                val email = findViewById<EditText>(R.id.regEmail).text.toString().trim{it <= ' '}
                val passwrd = findViewById<EditText>(R.id.regPassword).text.toString().trim{it <= ' '}

                //Initializing the firebase authentication
                auth = FirebaseAuth.getInstance()

                // Creating a user with email and password
                auth.createUserWithEmailAndPassword(email, passwrd)
                    .addOnCompleteListener { task ->


                        //The below onComplete is always imported after the error at object
                        //override fun onComplete(p0: Task<AuthResult>) {

                        //edutech starts here
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser!!.uid
                            val x = FirebaseDatabase.getInstance().getReference("Users")
                            x.setValue(
                                AuthUserInfo(
                                    currentUser,firstName, lastName,email
                                )
                            )

                            //-----------------------------------------------------------------------
                            //userDetailsToStore will send data to the class MyUser and
                            // later be picked using the firestoreclass
                            val userDetailsToStore = MyUser(
                                currentUser,
                                firstName,
                                lastName,
                                email
                            )
                            // Storing User details into the firebase storage using the firestore class
                            UserInfoFirestore().storeUserInfo(this@Registration, userDetailsToStore)
                            //------------------------------------------------------------------



                            val intent = Intent(this, MainActivity::class.java)
                            /*
                                Getting rid of the other layers running in the background say if
                                one moves from login to registration we make sure to avoid him from getting back to reg
                                activity at any point
                           */
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            // Transfering the any data to the next activity incase they are to be used
//                            intent.putExtra("firstName", firstName)
//                            intent.putExtra("lastName", lastName)
//                            intent.putExtra("userID", currentUser)
//                            intent.putExtra("userEmail", email)
                            startActivity(intent)

                        }

                        else {
                            //Ending the progress dialog
                            progDialog.dismiss()
                            Toast.makeText(
                                this@Registration,
                                task.exception?.message.toString(),
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
                //Registration button
                R.id.regbtn ->{
                    validateRegDetails()
                }

                //Already have an account txtView
                R.id.haveActLogin ->{
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }

            }
        }
    }
}