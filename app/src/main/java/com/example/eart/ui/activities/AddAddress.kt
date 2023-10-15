package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityAddAddressBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Address
import com.example.eart.modules.Constants

class AddAddress : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityAddAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address)
        setUpActionBar()
        binding.saveAddressBtn .setOnClickListener(this)

        // Making the field to add other type visibility visible and gone
        // if the other radio btn is selected

        binding.radioGroupType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_other){
                binding.addressOther.visibility = View.VISIBLE
            }else{
                binding.addressOther.visibility = View.GONE
            }
        }
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.toolbar_add_address_edit)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.save_address_btn ->{
                    saveAddressToFirestore()
                }
            }
        }
    }


//    Validating the user entries

    fun validateUserData(): Boolean {
        return when {
            TextUtils.isEmpty(binding.addressRegion.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_region), true)
                false
            }

            TextUtils.isEmpty(binding.addressName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_address_name), true)
                false
            }

            TextUtils.isEmpty(binding.addressPhoneNumber.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.error_phone), true)
                false
            }

            binding.rbOther.isChecked && TextUtils.isEmpty(binding.addressZipcode.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.error_zipcode), true)
                false
            }
            else ->{
                true
            }
        }
    }


    // Adding the address
    fun saveAddressToFirestore(){
        if (validateUserData()){
            progressDialog("Saving....")
            // Getting the value from the radio buttons
            // We use constants to create these values
            val address_type:String = when{
                binding.rbHome.isChecked ->{
                    Constants.HOME
                }
                binding.rbOffice.isChecked ->{
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressDetailsToStore = Address(
                FirestoreClass().getCurrentUserID(),
                binding.addressRegion.text.toString(),
                binding.addressName.text.toString(),
                binding.addressPhoneNumber.text.toString(),
                binding.addressZipcode.text.toString(),
                address_type
            )
            FirestoreClass().addAddressToFirestoreList(this, addressDetailsToStore)
        }
    }

    fun saveAddressToFirestoreSuccess() {
        hideProgressDialog()
        Toast.makeText(this, "Address was saved", Toast.LENGTH_LONG).show()

        val intent = Intent(this, AddressList::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()

    }

}