package com.example.eart.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityAddproductBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.modules.PrdctDtlsClass
import java.io.IOException

class Addproduct : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityAddproductBinding

    private var mselectImageFileUri:Uri? = null
    private var mImageUrl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addproduct)
        setUpActionBar()


        binding.loadProdct.setOnClickListener(this)
        binding.addProdctBtn.setOnClickListener(this)
    }

    private fun setUpActionBar(){
        val myToolBar = binding.productsToolbar
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    /* The onclick function below appears as a member that is implemented
        after inheriting from the Vie.OnclickListener
    */
    override fun onClick(v: View?) {
        // We first check if the view is not null
        if (v != null) {
            // Then determine what will happen for each view using its id
            when (v.id) {
                // Camera icon that acts as the button to trigger the uploading image process
                R.id.loadProdct -> {
                    //Checking if there is access to the camera and external storage
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        //I already have an account
                        //Instead of showing the already have permission we can choose an image
                        Constants.imageChooser(this)

                        /**
                         * After choosing the image then the onActivityResult() function uploads the file/image to the app
                         * to be viewed
                         */
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                                // Manifest.permission.CAMERA
                            ), Constants.READ_EXTERNAL_STORAGE_CODE
                        /* READ_EXTERNAL_STORAGE is the request code wic can be any value and can be used
                            to compare say if the reqcode= to what u intend then do ABC...
                            */
                        )
                    }
                }

                /**
                 *  After the picture is set and the product details also entered then we validate and upload he image first
                 *  then after the details uploaded to
                */
                R.id.addProdctBtn ->{
                    if (validateProductDetails()){
                        uploadProductImageToCloud()
                    }
                }
            }
        }
    }

    // Asking for permission if we dont have
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_CODE ){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //if we are granted the permission the we just go ahead to choose the image
                //Also hea the we already have the permissions so we choose the image
                Constants.imageChooser(this)
            }else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }


    //The function to receive the results
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Checking whether the result image is ok
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data !=null){
            //Compare if the request code passed from the onActivityResult is the same as one used to picked the image
                //Checking if the data is not equal to null coz it can happen otherwise the app can crash

                    val imgLoader = binding.loadProdct
            //Changing the icon for loadimage to edit
            imgLoader.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))

                    try {
                        /**
                         * if the request and result codes are okay then we have the data ready
                         * and ts from this dat that we get the image uri from as below
                        */
                        //selected uri for the image from storage media and this uri is one we
                        // set as a parameter on setting the image
                        //Picking the image

                        mselectImageFileUri = data.data!! // LOCATION OF THE FILE
                        val productImgToUpload = binding.imageViewProdct
                        /*
                                Using Glide loader: its fast and can smartly define the type of file that one is uploading
                        *  Syntax for glide
                                Glide.with(the activity wea to oad the file).load( the Uri or location of the image)
                                .into( the exact view   wea to insert the file within the selected activity)
                        * */
                        //setting the image from the URI or use Glideloader
                        Glide.with(this).load(mselectImageFileUri).into(productImgToUpload)
                        // Using setImageUri option below
//                        profileImg.setImageURI(mselectImageFileUri!!)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this, "set image failure", Toast.LENGTH_LONG).show()
                    }

        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Result cancelled", "Image selected cancelled")
        }
    }

    fun validateProductDetails(): Boolean{

        return when{
            mselectImageFileUri == null ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctImg), true)
                false
            }
            TextUtils.isEmpty(binding.prodctTitle.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctTitle), true)
                false
            }

            TextUtils.isEmpty(binding.productPrice.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctPrice), true)
                false
            }

            TextUtils.isEmpty(binding.prodctDescription.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctDescrptn), true)
                false
            }

            TextUtils.isEmpty(binding.prodctQuantity.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctQtty), true)
                false
            }

            else -> {
                //Return true indicating that all data is validated
                true
            }
        }
    }


    private fun uploadProductImageToCloud(){

        progressDialog("Uploadig....")
        //first check if the image uri is not null
        if (mselectImageFileUri !=null){
//            Log.e("File location: ", mselectImageFileUri.toString())

            FirestoreClass().uploadImageToCloud(this, mselectImageFileUri, Constants.PRODUCT_IMAGE)

        }

    }



    private fun uploadProdctDetails(){

        val username = this.getSharedPreferences(Constants.MYAPP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME, "")!!
        val userid = FirestoreClass().getCurrentUserID()

        //Storing the data into the PrdctDtlsClass to be picked by the Firestore class file

        val prodct = PrdctDtlsClass(
            userid,
            username,
            binding.prodctTitle.text.toString().trim { it <= ' '},
            binding.productPrice.text.toString().trim { it <= ' '},
            binding.prodctDescription.text.toString().trim { it <= ' '},
            binding.prodctQuantity.text.toString().trim { it <= ' '},
            mImageUrl
        )

        // Store details
        FirestoreClass().storeProductInfo(this,prodct)

    }


    /*
        This function below will be called when the image is uploaded successfully
        after which the details about the image/product will also be uploaded
        the imgUrl passed below as a param is th one that has been pst to the function in firestoreclass
    */
    fun imageUploadSuccess(ImageUrl:String){
        mImageUrl = ImageUrl

        //Log.e("Download Image Url: ", mImageUrl)

        uploadProdctDetails()
    }



    /*
        This function below will be called when the prdct details are uploaded successfully
     */
    fun productUploadSuccess(){
       //Hiding the progress dialog and finishing the process
       progDialog.dismiss()
       Toast.makeText(this, "Product was uploaded successfully", Toast.LENGTH_LONG)
           .show()

       // finishing allws the user to stay out of the addproduct activity
       finish()
    }

}