package com.example.eart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eart.R
import com.example.eart.adapters.DashboardItemListAdapter
import com.example.eart.basefragment.BaseFragment
import com.example.eart.databinding.FragmentDashboardBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.CartListActivity
import com.example.eart.ui.activities.Favorite
import com.example.eart.ui.activities.Settings

class DashboardFragment : BaseFragment() {
    private var _binding : FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // To use the options menu in a fragment we add this below
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    // This will make the menu to be activated
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //We will use a custom menu that we created, so we have to inflate it first
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    //This will activate the items in the options menu on what to display next
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        when(id){
            R.id.action_settings ->{
                startActivity(Intent(activity, Settings::class.java))
                return true
            }

            R.id.action_cart_list ->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }

            R.id.action_logout ->{
                showAlertDialogForLogout(
                    requireActivity(),
                    "Logging out",
                    "Are you sure, you want to logout?")
                return true
            }

            R.id.action_favorite ->{
                startActivity(Intent(activity, Favorite::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    // When the user selects the dashboard fragment the onResume function is called
    override fun onResume() {
        super.onResume()
        getDashboardItemsFromFirestore()
    }


    // Function to get the Dashboard items list from firestore and it will be called on resume
    private fun getDashboardItemsFromFirestore(){
        // start the progress dialog
        progressDialog("Loading.....")
        FirestoreClass().getDashboardItemList(this)
    }


    // Create the success download of products function in dashboard frag
    //We shall pass the product list from firestore into this function
    fun dashboardListDownloadSuccess(dashboardItemsList:ArrayList<PrdctDtlsClass>){
        //first hide the progress dialog
        hideProgressDialog()
        /** Check if there are any products in the list and .....
         * make the recyclerview Views visible
         * then make the no product added yet to gone
         */


        if(dashboardItemsList.size > 0 ){
            binding.dashboardRecycView.visibility = View.VISIBLE
            binding.noDashboardAddedYet.visibility = View.GONE

            // change the view layout using layout manager and we want to use it in this activity/this
            binding.dashboardRecycView.layoutManager = GridLayoutManager(requireActivity(), 2)
            // set has fixed size in order to make its size fixed
            binding.dashboardRecycView.setHasFixedSize(true)

            val dashboardProductsAdapter = DashboardItemListAdapter(requireActivity(),dashboardItemsList)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            binding.dashboardRecycView.adapter = dashboardProductsAdapter

        }else{
            binding.dashboardRecycView.visibility = View.GONE
            binding.noDashboardAddedYet.visibility = View.VISIBLE
        }
    }


    fun addProductToFavoriteSuccess(){
        Toast.makeText(requireActivity(), "Added to favourite successfully", Toast.LENGTH_LONG).show()
        getDashboardItemsFromFirestore()
    }

}