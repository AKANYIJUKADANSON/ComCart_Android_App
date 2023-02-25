package com.example.eart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.AddressListAdapter
import com.example.eart.adapters.ProductsAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Address
import com.example.eart.modules.PrdctDtlsClass
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.fragment_products.*

class AddressList : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setUpActionBar()

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater): Boolean {
//        super.onCreateOptionsMenu(menu)
//        inflater.inflate(R.menu.address_list, menu)
//
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_add_address ->{
                startActivity(Intent(this, AddAddress::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
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
        progressDialog("Loading...")
        FirestoreClass().downloadAddressListFromFirestore(this)
        super.onResume()
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

        }else{
            recycler_view_address_list.visibility = View.GONE
            tv_no_address.visibility = View.VISIBLE
        }

    }

}