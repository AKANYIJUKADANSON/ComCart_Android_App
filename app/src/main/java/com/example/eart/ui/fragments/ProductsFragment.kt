package com.example.eart.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.ProductsAdapter
import com.example.eart.basefragment.BaseFragment
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.Addproduct
import com.example.eart.ui.activities.Login
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.action_addItem ->{
                startActivity(Intent(activity, Addproduct::class.java))
                return true
            }

            R.id.action_logout ->{
                showAlertDialogForLogout(
                    requireActivity(),
                    "Logging out",
                    "Are you sure, you want to logout?")
//                startActivity(Intent(activity, Login::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    // On resume means that everytym we get trigger the onresume option or getting back to the products fragments
    // then we can call the functions for getting products again
    override fun onResume() {
        super.onResume()
        getProductsfromFirestore()
    }


    // Function to get the product list from firestore and it will be called on resume
    fun getProductsfromFirestore(){
        // start the progress dialog
        progressDialog("Loading.....")
        FirestoreClass().getProductList(this)

    }


    // Create the success download of products function
    //We shall pass the product list from firestore into this function
    fun productListDownloadSuccess(prductList:ArrayList<PrdctDtlsClass>){
        //first hide the progress dialog
        hideProgressDialog()
        /**
         * Check if there are any products in the list and .....
         * make the recyclerview Views visible
         * then make the no product added yet to gone
         */
        if(prductList.size > 0 ){
            products_recyc_view.visibility = View.VISIBLE
            no_prdcts_added_yet.visibility = View.GONE

            // change the view layout using layoutmanager and we want to use it in this activity/this
            products_recyc_view.layoutManager = LinearLayoutManager(activity)
            // sethasfixed size in order to make its size fixed
            products_recyc_view.setHasFixedSize(true)

            val productsAdapter = ProductsAdapter(requireActivity(), prductList, this)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            products_recyc_view.adapter = productsAdapter

        }else{
            products_recyc_view.visibility = View.GONE
            no_prdcts_added_yet.visibility = View.VISIBLE
        }

    }


    fun deleteProduct(productID:String){
        showAlertDialogForDeletingProduct(productID)
//        Toast.makeText(requireActivity(), "Deleting product with id $productID", Toast.LENGTH_LONG).show()
    }

    fun deleteProductSuccess(){
        hideProgressDialog()
        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.delete_product_success),
            Toast.LENGTH_LONG
        ).show()

        // After deleting, call back or reload the products
        getProductsfromFirestore()
    }

    /**
     * This function below will display the alert for deleting the product
     */

    private fun showAlertDialogForDeletingProduct(productID: String){
        val builder = AlertDialog.Builder(requireActivity())

        // Setting the Title, message and icon for the alert
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_alert_msg))
        builder.setIcon(R.drawable.ic_warning)

        // Setting the positive action when a yes is selected
        builder.setPositiveButton(resources.getString(R.string.yes)){
            dialogInterface,_->

            progressDialog("Processing")
            FirestoreClass().deleteProductFromStore(this, productID)

        }

        // Setting the negative action when a yes is selected
        builder.setNegativeButton(resources.getString(R.string.No)){
                dialogInterface,_->

            dialogInterface.dismiss()
        }

        // Using the alert dialog structure above to show the alert
        // Creating the dialof
        val alert_dialog:AlertDialog = builder.create()
        alert_dialog.setCancelable(false)
        alert_dialog.show()
    }



}