package com.example.eart.baseactivity

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eart.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private  var doubleBackPressedOnce = false
    lateinit var progDialog: ProgressDialog

    // creating a popup that will appear in case one of the fields is empty or not valid #snackBar
    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        //Setting the background if there is an error
        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.errorColor
                )
            )
        }

        //Setting the background color if there is no error massage
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.deepskyblue
                )
            )
        }
        snackBar.show()
    }
    //Function Starting the dialog
//    fun progressDialog(text:String){
//        //The parameter text can be any text say Loading, waiting
//        val progdialgTxt = findViewById<TextView>(R.id.progdialgTxt)
//        //Initializing the dialog
//        mProgressBarDialog = Dialog(this)
//        //Layout containing the progdialog will be inflated
//        mProgressBarDialog.setContentView(R.layout.progressdialog)
////        progdialgTxt.text = text
//        //Setting it not to be cancelable even when the touched on the outside area
//        mProgressBarDialog.setCancelable(false)
//        mProgressBarDialog.setCanceledOnTouchOutside(false)
//        mProgressBarDialog.show()
//    }

    //function that will stop the dialog

//    fun stopDialog(){
//        progDialog.dismiss()
//    }

    //The progress dialog
    fun progressDialog(text:String){
        progDialog = ProgressDialog(this)
        progDialog.progress = 0
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progDialog.setTitle(text)
        progDialog.show()
    }

    fun perctageProgDialog(text:String){
        progDialog = ProgressDialog(this)
        progDialog.progress = 0
        progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progDialog.setTitle(text)
        progDialog.show()
    }


    fun doubleBackToExit(){

        // If the back is not pressed twice the keep it as default
        if(doubleBackPressedOnce){
            super.onBackPressed()
            return
        }

        //The else part
        this.doubleBackPressedOnce = true
        Toast.makeText(this, resources.getString(R.string.onBackPress),
            Toast.LENGTH_SHORT).show()

            // This will enable if the back is clicked two seconds later, then the app will not be exited
        Handler().postDelayed({doubleBackPressedOnce =false}, 2000)
    }




}