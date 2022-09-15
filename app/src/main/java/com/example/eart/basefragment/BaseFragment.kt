package com.example.eart.basefragment

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.eart.R

class BaseFragment : Fragment() {
    lateinit var mProgressDialog: ProgressDialog
    lateinit var progdialgTxt:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_base, container, false)
        progdialgTxt = root.findViewById(R.id.progdialgTxt)
        return root
    }

    fun progressDialog(text:String){
        //The progress dialog
            mProgressDialog = ProgressDialog(let { it.requireActivity() })
            progdialgTxt.text = text
            mProgressDialog.progress = 0
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            mProgressDialog.setTitle(text)
            mProgressDialog.show()
        }

    fun dismissDialog(){
        mProgressDialog.dismiss()
    }

}