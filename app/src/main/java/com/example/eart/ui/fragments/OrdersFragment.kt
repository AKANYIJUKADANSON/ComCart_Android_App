package com.example.eart.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.OrdersAdapter
import com.example.eart.basefragment.BaseFragment
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Order
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
        getOrdersFromFirestore()
    }

    private fun getOrdersFromFirestore(){
        // start the progress dialog
        progressDialog("Loading.....")
        FirestoreClass().getOrdersList(this)

    }

    fun ordersListDownloadSuccess(ordersList: ArrayList<Order>){
        //first hide the progress dialog
        hideProgressDialog()
        /**
         * Check if there are any products in the list and .....
         * make the recyclerview Views visible
         * then make the no product added yet to gone
         */
        if(ordersList.size > 0 ){
            orders_recyc_view.visibility = View.VISIBLE
            no_orders_added_yet.visibility = View.GONE

            // change the view layout using layoutManager and we want to use it in this activity/this
            orders_recyc_view.layoutManager = LinearLayoutManager(activity)
            // setHasFixed size in order to make its size fixed
            orders_recyc_view.setHasFixedSize(true)

            val ordersAdapter = OrdersAdapter(requireActivity(), ordersList)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            orders_recyc_view.adapter = ordersAdapter

        }else{
            orders_recyc_view.visibility = View.GONE
            no_orders_added_yet.visibility = View.VISIBLE
        }

    }

}