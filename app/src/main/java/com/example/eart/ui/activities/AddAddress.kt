package com.example.eart.ui.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Address
import com.example.eart.modules.Constants
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_settings.*

class AddAddress : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        setUpActionBar()
        save_address_btn.setOnClickListener(this)

        // Making the field to add other type visibility visible and gone
        // if the other radio btn is selected

        radio_group_type.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_other){
                address_other.visibility = View.VISIBLE
            }else{
                address_other.visibility = View.GONE
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
            TextUtils.isEmpty(address_region.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_region), true)
                false
            }

            TextUtils.isEmpty(address_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_address_name), true)
                false
            }

            TextUtils.isEmpty(address_phone_number.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.error_phone), true)
                false
            }

            rb_other.isChecked && TextUtils.isEmpty(address_zipcode.text.toString().trim { it <= ' ' }) ->{
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
                rb_home.isChecked ->{
                    Constants.HOME
                }
                rb_office.isChecked ->{
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressDetailsToStore = Address(
                FirestoreClass().getCurrentUserID(),
                address_region.text.toString(),
                address_name.text.toString(),
                address_phone_number.text.toString(),
                address_zipcode.text.toString(),
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