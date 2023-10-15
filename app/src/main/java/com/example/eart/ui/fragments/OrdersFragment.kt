package com.example.eart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.OrdersAdapter
import com.example.eart.basefragment.BaseFragment
import com.example.eart.databinding.FragmentOrdersBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Order
import com.example.eart.ui.activities.CartListActivity
import com.example.eart.ui.activities.Settings

class OrdersFragment : BaseFragment() {

    // Binding
    private var _binding:FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.orders_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        when(id){

            R.id.action_cart_list ->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }

            R.id.action_settings ->{
                startActivity(Intent(activity, Settings::class.java))
                return true
            }

            R.id.action_logout ->{
                showAlertDialogForLogout(
                    requireActivity(),
                    "Logging out",
                    "Are you sure, you want to logout?")
                return true
            }

        }

        return super.onOptionsItemSelected(item)
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
            binding.ordersRecycView.visibility = View.VISIBLE
            binding.noOrdersAddedYet.visibility = View.GONE

            // change the view layout using layoutManager and we want to use it in this activity/this
            binding.ordersRecycView.layoutManager = LinearLayoutManager(activity)
            // setHasFixed size in order to make its size fixed
            binding.ordersRecycView.setHasFixedSize(true)

            val ordersAdapter = OrdersAdapter(requireActivity(), ordersList)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            binding.ordersRecycView.adapter = ordersAdapter

        }else{
            binding.ordersRecycView.visibility = View.GONE
            binding.noOrdersAddedYet.visibility = View.VISIBLE
        }

    }

}