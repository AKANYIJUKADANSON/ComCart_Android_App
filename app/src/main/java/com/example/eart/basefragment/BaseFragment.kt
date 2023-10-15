package com.example.eart.basefragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.eart.R
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.ui.activities.Login
import com.google.firebase.firestore.FirebaseFirestore

open class BaseFragment : Fragment() {
    lateinit var mProgressDialog: Dialog

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_base, container, false)
//        progdialgTxt = root.findViewById(R.id.progdialgTxt)
        return root
    }


    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun progressDialog(text: String) {
        mProgressDialog = Dialog(requireActivity())

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.progressdialog)
        mProgressDialog.setTitle(text)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }


    // Creating a general alert dialog
    fun showAlertDialogForLogout(
        context:Context,
        title:String,
        message_to_display:String){
        val builder = AlertDialog.Builder(activity)

        // Setting the Title, message and icon for the alert
        builder.setTitle(title)
        builder.setMessage(message_to_display)
        builder.setIcon(R.drawable.ic_warning)

        // Setting the positive action when a yes is selected
        builder.setPositiveButton(resources.getString(R.string.yes)){
                dialogInterface,_->
            // Logout
            val intent = Intent(context, Login::class.java)
            // Removing the flags to avoid getting back to the background activities running
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
//            activity?.finish()

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