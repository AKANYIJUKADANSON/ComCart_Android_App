package com.example.eart.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.modules.MyUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.IOException

//
class Registration : BaseActivity(), View.OnClickListener {

    private var mselectImageFileUri: Uri? = null
    lateinit var registrationButton: Button
    lateinit var haveAcctLgn: TextView
    lateinit var loadUserImg:ImageView
    lateinit var auth: FirebaseAuth
    private var mImageUrl:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //capturing views to use in the onclick function
        registrationButton = findViewById(R.id.regbtn)
        haveAcctLgn = findViewById(R.id.haveActLogin)
        loadUserImg = findViewById(R.id.loadUserImg)

        //Setting the onclick function for views to function
        registrationButton.setOnClickListener(this)
        haveAcctLgn.setOnClickListener(this)
        loadUserImg.setOnClickListener(this)



    }

    fun validateRegDetails(): Boolean{
        //capturing what the user enters using ids and changing them into text format
        val regFirstName = findViewById<EditText>(R.id.address_name)
        val regLastName = findViewById<EditText>(R.id.address_phone_number)
        val regEmail = findViewById<EditText>(R.id.address_zipcode)
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
                true
            }


        }

    }

    /*
This function below will be called when the image is uploaded successfully in the firestore class
after which the details about the image/product will also be uploaded
*/
    fun imageUploadSuccess(ImageUrl:String){
        mImageUrl = ImageUrl
        Toast.makeText(this, "Image uploaded and Uri = $ImageUrl", Toast.LENGTH_LONG).show()
        uploadUserDetails()
    }


    //Creating the onclickListener for all the clickable items
    override fun onClick(view: View?){
        if (view!= null){

            when(view.id){
                //Registration button
                R.id.regbtn ->{
                    if(validateRegDetails()){
                        FirestoreClass().uploadImageToCloud(this, mselectImageFileUri, Constants.USER_PROFILE_IMAGE)
                    }
                }

                // --------------------------PICKING THE IMAGE FROM STORAGE MEDIA--------------
                R.id.loadUserImg->{
                    //Checking if there is access to the camera and external storage
                    if (ContextCompat.checkSelfPermission(
                            this, android.Manifest.permission.READ_EXTERNAL_STORAGE )
                        == PackageManager.PERMISSION_GRANTED ) {
                        //I already have permission to
                        //Instead of showing the already have permission we can choose an image
                        Constants.imageChooser(this)

                        // After choosing the image then the onActivityResult() function uploads the file/image to the app
                        // to be viewed
                    } else {

                        // If there are no permissions then we can ask for some as below
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                            ), Constants.READ_EXTERNAL_STORAGE_CODE
                            /**
                             * READ_EXTERNAL_STORAGE_CODE will be compared to in the onRequestPermisiionResults function
                             *  READ_EXTERNAL_STORAGE code is the request code wic can be any value and can be used
                                to compare say if the reqcode= to what u intend then do ABC...
                                */
                        )
                    }
                }

                //Already have an account txtView
                R.id.haveActLogin ->{
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }

            }
        }
    }


    /**
     *  This function will give an answer on the granted permission results
     *  if the request code is same as the one used to ask for the permission
     *  if we have the permission then we can choose the image
    */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
        ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_CODE ){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                /**
                 * grantResults.isEmpty() -> we have a result from the user
                 * grantResults[0] -> position of yes or no
                 * PackageManager.PERMISSION_GRANTED-> granted access
                 */

                //Since we are granted the permission so we choose the image to upload using the uploadImage function
                Constants.imageChooser(this)
            }else{
                Toast.makeText(this, R.string.errorPermission, Toast.LENGTH_LONG).show()
            }
        }
    }


    /**
     * After choosing the image/file then we have to receive the results using the onActivityResult() function
     * and then display it in the app in this cas using Glide loader function
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Checking whether the result image is ok
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data !=null){
            //Checking if the request code passed from the onActivityResult is the one we used to picked the image
            //Checking if the data is not equal to null coz it can happen otherwise the app can crash

            val imgLoader = findViewById<ImageView>(R.id.loadUserImg)
            //Changing the icon for loading image to edit image after the image is selected and on the app
            imgLoader.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))

            try {
                //selected uri for the image from storage media and this uri is one we
                // set as a parameter on setting the image
                //Picking the image
                mselectImageFileUri = data.data!! // LOCATION OF THE FILE
                val userprofileImg = findViewById<ImageView>(R.id.userprofileImg)
                /*
                        Using Glide loader: its fast and can smartly define the type of file that one is uploading
                *  Syntax for glide
                        Glide.with(the activity wea to oad the file).load( the Uri or location of the image)
                        .into( the exact view wea to insert the file within the selected activity)
                */
                //setting the image from the URI or use Glideloader
                Glide.with(this).load(mselectImageFileUri).into(userprofileImg)
                // Using setImageUri option below
//                        profileImg.setImageURI(mselectImageFileUri!!)
            }catch (e: IOException){
                e.printStackTrace()
                Toast.makeText(this, "Image selection failure", Toast.LENGTH_LONG).show()
            }

        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Result cancelled", "Image selected cancelled")
        }
    }


    /**
     * Uploading the image to the firebase
     * */
    fun uploadUserDetails(){
        progressDialog("Authenticating....")

        // NOTE: all these always have to be captured again to be used in the authentication
        val firstName = findViewById<EditText>(R.id.address_name).text.toString().trim{it <= ' '}
        val lastName = findViewById<EditText>(R.id.address_phone_number).text.toString().trim{it <= ' '}
        val email = findViewById<EditText>(R.id.address_zipcode).text.toString().trim{it <= ' '}
        val passwrd = findViewById<EditText>(R.id.regPassword).text.toString().trim{it <= ' '}

        //Initializing the firebase authentication
        auth = FirebaseAuth.getInstance()

        // Creating a user with email and password
        auth.createUserWithEmailAndPassword(email, passwrd)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful){
                        val firebaseuser: FirebaseUser? = task.result!!.user

                        //-----------------------------------------------------------------------
                        //userDetailsToStore will send data to the class MyUser and
                        // later be picked using the firestoreclass
                        val userDetailsToStore = MyUser(
                            firebaseuser!!.uid,
                            firstName,
                            lastName,
                            email
                        )
                        // Storing User details into the firebase storage using the firestore class
                        //UserInfoFirestore().storeUserInfo()

                        FirestoreClass().registerUser(this@Registration, userDetailsToStore)
                        //------------------------------------------------------------------
                    }
                }

            )


    }

    /**
     * A function to notify the success result of Firestore entry when the user is registered successfully.
     */
    fun userRegistrationSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@Registration, "Registered successfully, LOGIN TO YOUR ACCOUNT",
            Toast.LENGTH_LONG
        ).show()






        val intent = Intent(this, Login::class.java)
        /*
            Getting rid of the other layers running in the background say if
            one moves from login to registration we make sure to avoid him from getting back to reg
            activity at any point
       */
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Transfering the any data to the next activity incase they are to be used
        //  intent.putExtra("firstName", firstName)
        //  intent.putExtra("lastName", lastName)
        //  intent.putExtra("userID", currentUser)
        //  intent.putExtra("userEmail", email)
        startActivity(intent)








        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
//        FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
//        finish()
    }


}