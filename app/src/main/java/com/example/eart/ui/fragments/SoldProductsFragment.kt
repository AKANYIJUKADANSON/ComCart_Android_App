package com.example.eart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.ProductsAdapter
import com.example.eart.adapters.SoldProductsAdapter
import com.example.eart.basefragment.BaseFragment
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.SoldProducts
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.fragment_sold_products.*


class SoldProductsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    override fun onResume() {
        super.onResume()

        getSoldProductsFromFirestore()
    }

    private fun getSoldProductsFromFirestore(){
        progressDialog("Loading")
        FirestoreClass().downloadSoldProductsFromFirestore(this)
    }

    fun getSoldProductsFromFirestoreSuccess(soldProductList:ArrayList<SoldProducts>) {
        //first hide the progress dialog
        hideProgressDialog()
        /**
         * Check if there are any products in the list and .....
         * make the recyclerview Views visible
         * then make the no product added yet to gone
         */
        if(soldProductList.size > 0 ){
            rv_sold_products.visibility = View.VISIBLE
            tv_no_sold_products.visibility = View.GONE

            // change the view layout using layoutmanager and we want to use it in this activity/this
            rv_sold_products.layoutManager = LinearLayoutManager(activity)
            // sethasfixed size in order to make its size fixed
            rv_sold_products.setHasFixedSize(true)

            val soldProductsAdapter = SoldProductsAdapter(requireActivity(), soldProductList)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            rv_sold_products.adapter = soldProductsAdapter

        }else{
            rv_sold_products.visibility = View.GONE
            tv_no_sold_products.visibility = View.VISIBLE
        }
    }

}