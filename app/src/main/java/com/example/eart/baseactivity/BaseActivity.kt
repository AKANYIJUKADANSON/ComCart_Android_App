package com.example.eart.baseactivity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eart.R
import com.example.eart.firestore.FirestoreClass
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private  var doubleBackPressedOnce = false

    /**
     * This is a progress dialog instance which we will initialize later on.
     */
    lateinit var progDialog: Dialog

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

     /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun progressDialog() {
        progDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        progDialog.setContentView(R.layout.progressdialog)
        progDialog.setCancelable(false)
        progDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        progDialog.show()
    }


    //function that will stop the dialog

    fun hideProgressDialog(){
        progDialog.dismiss()
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


    fun showAlertDialogToDelete(
        activity: Activity,
        title:String,
        message_to_display:String,
        itemToDeleteId:String
    ){
        val builder = AlertDialog.Builder(this)

        // Setting the Title, message and icon for the alert
        builder.setTitle(title)
        builder.setMessage(message_to_display)
        builder.setIcon(R.drawable.ic_warning)

        // Setting the positive action when a yes is selected
        builder.setPositiveButton(resources.getString(R.string.yes)){
                dialogInterface,_->

            // Delete
            FirestoreClass().deletingCategory(activity, itemToDeleteId)

        }

        // Setting the negative action when a yes is selected
        builder.setNegativeButton(resources.getString(R.string.No)){
                dialogInterface,_->

            dialogInterface.dismiss()
        }

        // Using the alert dialog structure above to show the alert
        // Creating the dialof
        val alert_dialog: AlertDialog = builder.create()
        alert_dialog.setCancelable(false)
        alert_dialog.show()
    }

}