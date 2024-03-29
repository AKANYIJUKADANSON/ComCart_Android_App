package com.example.eart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.ProductsAdapter
import com.example.eart.adapters.SoldProductsAdapter
import com.example.eart.basefragment.BaseFragment
import com.example.eart.databinding.FragmentSoldProductsBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.SoldProducts
import com.example.eart.ui.activities.CartListActivity
import com.example.eart.ui.activities.Settings


class SoldProductsFragment : BaseFragment() {
    private var _binding:FragmentSoldProductsBinding? = null
    private val binding get() = _binding!!

    // OnCreate() function will help to add the menu in the fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSoldProductsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sold_products_menu, menu)
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
            binding.rvSoldProducts.visibility = View.VISIBLE
            binding.tvNoSoldProducts.visibility = View.GONE

            // change the view layout using layoutmanager and we want to use it in this activity/this
            binding.rvSoldProducts.layoutManager = LinearLayoutManager(activity)
            // sethasfixed size in order to make its size fixed
            binding.rvSoldProducts.setHasFixedSize(true)

            val soldProductsAdapter = SoldProductsAdapter(requireActivity(), soldProductList)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            binding.rvSoldProducts.adapter = soldProductsAdapter

        }else{
            binding.rvSoldProducts.visibility = View.GONE
            binding.tvNoSoldProducts.visibility = View.VISIBLE
        }
    }
}