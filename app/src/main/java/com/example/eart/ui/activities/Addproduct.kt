package com.example.eart.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.ProdctInfoStore
import com.example.eart.modules.Constants
import com.example.eart.modules.PrdctDtlsClass
import com.example.edutech.firestore.UserInfoFirestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException

class Addproduct : BaseActivity(), View.OnClickListener {

    private var mselectImageFileUri:Uri? = null
    private var mImageUrl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addproduct)
        setUpActionBar()

        val loadimg = findViewById<ImageView>(R.id.loadProdct)
        val addPrdctSubmtBtn = findViewById<Button>(R.id.addProdctBtn)
        loadimg.setOnClickListener(this)
        addPrdctSubmtBtn.setOnClickListener(this)
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.productsToolbar)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.loadProdct -> {
                    //Checking if i have access to the camera and external storage
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        //I already have an account
                        //Instead of showing the already have permission we can choose an image
                        Constants.imageChooser(this)
                    } else {

                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                                // Manifest.permission.CAMERA
                            ), Constants.READ_EXTERNAL_STORAGE_CODE
                        )
                    }
                }

                R.id.addProdctBtn ->{
                    if (validateProductDetails()){
                        uploadProductImage()
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
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

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
            //Checking if the request code passed from the onActivityResult is the one we picked our image
                //Checking if the data is not equal to null coz it can happen otherwise the app can crash

                    val imgLoader = findViewById<ImageView>(R.id.loadProdct)
            //Changing the icon for loadimage to edit
            imgLoader.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))

                    try {
                        //selected uri for the image from storage media and this uri is one we
                        // set as a parameter on setting the image
                        //Picking the image
                        mselectImageFileUri = data.data!!
                        val profileImg = findViewById<ImageView>(R.id.image_view_prodct)
                        //setting the image from the URI or use Glideloader
                        profileImg.setImageURI(mselectImageFileUri!!)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this, "set image failure", Toast.LENGTH_LONG).show()
                    }

        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Result cancelled", "Image selected cancelled")
        }
    }

    fun validateProductDetails(): Boolean{
        val productTitle = findViewById<EditText>(R.id.prodctTitle)
        val productPrice = findViewById<EditText>(R.id.productPrice)
        val productDecrptn = findViewById<EditText>(R.id.prodctDescription)
        val productQutty = findViewById<EditText>(R.id.prodctQuantity)

        return when{
            mselectImageFileUri == null ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctImg), true)
                false
            }
            TextUtils.isEmpty(productTitle.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctTitle), true)
                false
            }

            TextUtils.isEmpty(productPrice.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctPrice), true)
                false
            }

            TextUtils.isEmpty(productDecrptn.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctDescrptn), true)
                false
            }

            TextUtils.isEmpty(productQutty.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.errorProdctQtty), true)
                false
            }

            else -> {
                //Return true indicating that all data is verified
                true
            }
        }
    }

    fun uploadProductImage(){

        progressDialog("Uploadig....")
        //first check if the image uri is not null
        if (mselectImageFileUri !=null){

            //Getting the image extensin which will help firebase to know under wc extension the img is stored
            val imgExtension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(mselectImageFileUri!!))

            //get the firebase storage reference
            val strgeRef : StorageReference = FirebaseStorage.getInstance().reference.child(
                //passing in a child wc will be the name of the image
                //Say naming the file
                "Product_Image" + System.currentTimeMillis() + "." + imgExtension )

            strgeRef.putFile(mselectImageFileUri!!).addOnSuccessListener {TaskSnapshot ->
                TaskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { Url ->

                    Log.e("Download Url", Url.toString())
                    imageUploadSuccess(Url.toString())
                   //mImageUrl = "$Url"
                   // Toast.makeText(this, "$Url", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "downloadUrl not generated", Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    fun imageUploadSuccess(ImageUrl:String){
        mImageUrl = ImageUrl
        uploadProdctDetails()
    }

    fun uploadProdctDetails(){
        val productTitle = findViewById<EditText>(R.id.prodctTitle).text.toString()
            .trim { it <= ' '}
        val productPrice = findViewById<EditText>(R.id.productPrice).text.toString()
            .trim { it <= ' '}
        val productDecrptn = findViewById<EditText>(R.id.prodctDescription).text.toString()
            .trim { it <= ' '}
        val productQutty = findViewById<EditText>(R.id.prodctQuantity).text.toString()
            .trim { it <= ' '}

            //Storing the data into the PrdctDtlsClass to be picked by the ProdctInfoStore.kt file
            val prodct = PrdctDtlsClass(
                productTitle, productPrice,
                productDecrptn, productQutty, mImageUrl
            )
            // Store details
            ProdctInfoStore().storeProductInfo(this, prodct)
    }

    fun productUploadSuccess(){
        //Hiding the progress dialog and finishing the process
        progDialog.dismiss()
        Toast.makeText(this, "Product Uploaded Successfully", Toast.LENGTH_LONG)
            .show()
        finish()
    }

}