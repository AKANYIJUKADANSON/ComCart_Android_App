package com.example.eart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eart.R
import com.example.eart.adapters.AddressListAdapter
import com.example.eart.adapters.ProductsAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Address
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.modules.SwipeToDeleteCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.fragment_products.*

class   AddressList : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setUpActionBar()


        fabAddAddress.setOnClickListener {
            startActivity(Intent(this, AddAddress::class.java))
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
            recycler_view_address_list.visibility = View.VISIBLE
            tv_no_address.visibility = View.GONE

            // change the view layout using layoutmanager and we want to use it in this activity/this
            recycler_view_address_list.layoutManager = LinearLayoutManager(this)
            // sethasfixed size in order to make its size fixed
            recycler_view_address_list.setHasFixedSize(true)

            val addressListAdapter = AddressListAdapter(this, addressList)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            recycler_view_address_list.adapter = addressListAdapter

            // Swiping to delete
            val swipeToDeleteHandler = object : SwipeToDeleteCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    progressDialog("Deleting...")
                    // delete the product from here
                    FirestoreClass().deleteAddress(this@AddressList, addressList[viewHolder.adapterPosition].address_id)
                }

            }

            val deleteItemTouchHelper = ItemTouchHelper(swipeToDeleteHandler)
            deleteItemTouchHelper.attachToRecyclerView(recycler_view_address_list)

        }else{
            recycler_view_address_list.visibility = View.GONE
            tv_no_address.visibility = View.VISIBLE
        }

    }

    fun deleteAddressSuccess(){
        hideProgressDialog()
        Toast.makeText(this, "Address deleted successfully", Toast.LENGTH_LONG).show()
        getAddressList()
    }

}