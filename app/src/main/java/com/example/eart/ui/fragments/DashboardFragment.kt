package com.example.eart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.DashboardItemListAdapter
import com.example.eart.adapters.ProductsAdapter
import com.example.eart.basefragment.BaseFragment
import com.example.eart.databinding.FragmentDashboardBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.CartListActivity
import com.example.eart.ui.activities.Login
import com.example.eart.ui.activities.Settings
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_products.*

class DashboardFragment : BaseFragment() {

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
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
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
        }
        return super.onOptionsItemSelected(item)
    }



    // When the user selects the dashboard fragment the onResume function is called
    override fun onResume() {
        super.onResume()
        getDashboardItemsfromFirestore()
    }


    // Function to get the Dashboard items list from firestore and it will be called on resume
    fun getDashboardItemsfromFirestore(){
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
//        for (i in dashboardItemsList){
//            Log.i("Items", i.productTitle)
//        }


        if(dashboardItemsList.size > 0 ){
            dashboard_recyc_view.visibility = View.VISIBLE
            no_dashboard_added_yet.visibility = View.GONE

            // change the view layout using layoutmanager and we want to use it in this activity/this
            dashboard_recyc_view.layoutManager = GridLayoutManager(requireActivity(), 2)
            // sethasfixed size in order to make its size fixed
            dashboard_recyc_view.setHasFixedSize(true)

            val dashboardProductsAdapter = DashboardItemListAdapter(requireActivity(), dashboardItemsList)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            dashboard_recyc_view.adapter = dashboardProductsAdapter

        }else{
            dashboard_recyc_view.visibility = View.GONE
            no_dashboard_added_yet.visibility = View.VISIBLE
        }
    }

}