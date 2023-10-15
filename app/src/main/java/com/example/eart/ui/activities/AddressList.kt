package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.adapters.AddressListAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityAddAddressBinding
import com.example.eart.databinding.ActivityAddressListBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Address
import com.example.eart.modules.Constants
import com.example.eart.modules.SwipeToDeleteCallback

class   AddressList : BaseActivity(){
    lateinit var binding: ActivityAddressListBinding
    // Will help to know what address is selected and it will be filled with the intent but one with
    // only intent with extras
    // The variable willl help determine wther we are selecting or want to edit the address what to do
    private var mSelectAddress:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_list)
        setUpActionBar()

        // Adding the address icon
        binding.fabAddAddress.setOnClickListener {
            // If we add an address, then we expect some results whc can be stored in the address code(constant)
            startActivity(Intent(this, AddAddress::class.java))
        }

        /**
         * Here is that if the intent has extras then we set the mselectedaddress variable to
         * whatever is passed
         * otherwise it will be set to false(default value parameter) below
         */
        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        /**
         * If mSelectedAddress is true meaning if it has the intent with extra_select_address
         * change the addressList Activity tv_title(activity title) to SELECT ADDRESS so that a user can select
         * but not edit or delete the address
         */
        if (mSelectAddress){
            binding.tvTitleAddressList.text = resources.getString(R.string.title_select_address)
        }
    }


    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.toolbar_address_list)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }


    override fun onResume() {
        getAddressList()
        super.onResume()
    }

    fun getAddressList(){
        progressDialog("Loading...")
        FirestoreClass().downloadAddressListFromFirestore(this)
    }

    // Create the success download of products function
    //We shall pass the product list from firestore into this function
    fun AddressListDownloadSuccess(addressList:ArrayList<Address>){
        //first hide the progress dialog
        hideProgressDialog()
        /**
         * Check if there are any products in the list and .....
         * make the recyclerview Views visible
         * then make the no product added yet to gone
         */
        if(addressList.size > 0 ){
            binding.recyclerViewAddressList.visibility = View.VISIBLE
            binding.tvNoAddress.visibility = View.GONE

            // change the view layout using layoutmanager and we want to use it in this activity/this
            binding.recyclerViewAddressList.layoutManager = LinearLayoutManager(this)
            // sethasfixed size in order to make its size fixed
            binding.recyclerViewAddressList.setHasFixedSize(true)

            val addressListAdapter = AddressListAdapter(this, addressList, mSelectAddress)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            binding.recyclerViewAddressList.adapter = addressListAdapter

            /**
             * If the mselectedAddress is not having extras with the intent to selectan address then
             * we are to either edit or delete
             */
            if (!mSelectAddress){
                // Swiping to delete
                val swipeToDeleteHandler = object : SwipeToDeleteCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        progressDialog("Deleting...")
                        // delete the product from here
                        FirestoreClass().deleteAddress(this@AddressList, addressList[viewHolder.adapterPosition].address_id)
                    }

                }

                val deleteItemTouchHelper = ItemTouchHelper(swipeToDeleteHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.recyclerViewAddressList)
            }

        }else{
            binding.recyclerViewAddressList.visibility = View.GONE
            binding.tvNoAddress.visibility = View.VISIBLE
        }

    }

    fun deleteAddressSuccess(){
        hideProgressDialog()
        Toast.makeText(this, "Address deleted successfully", Toast.LENGTH_LONG).show()
        getAddressList()
    }

}