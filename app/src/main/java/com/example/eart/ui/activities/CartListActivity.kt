package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.CartItemsListAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.CartItem
import com.example.eart.modules.Constants
import com.example.eart.modules.PrdctDtlsClass
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.fragment_products.*

class CartListActivity :BaseActivity() {

    // This variable will be initialized in the productdownload success
    private lateinit var mProductsList:ArrayList<PrdctDtlsClass>
    private lateinit var mCartListItems:ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setUpActionBar()

        btn_checkout.setOnClickListener {
            // Send an intent with extras to the AddressList Activity wea to pic the address from
            val intent = Intent(this@CartListActivity, AddressList::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }

    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.toolbar_cart_list_activity)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        // getCartItemsList() This will be the first to be called
        // We have to first get rid of the getCartItemList() because we want to first get the productItemsList()
        // This will help to know how much of each element exists in stock
        // The getCartItemsList() will be called when the productsList is downloaded successfully

        getProductsList()
    }

    private fun getProductsList(){
        progressDialog("Loading")
        FirestoreClass().getAllProductsList(this)
    }

    private fun getCartItemsList(){
        // progressDialog("Loading")
        FirestoreClass().downloadCartItemList(this)
    }

    fun successProductsListFromFirestore(productList:ArrayList<PrdctDtlsClass>){
        hideProgressDialog()
        // Initialising the mproductList with the productlist array that is passed from
        // firestore into this function
        mProductsList = productList

        // Since the products are obtained successfully then we can now download
        // and display cart items in the recyclerview
        getCartItemsList()
    }

    fun successDeleteCartItem(){
        hideProgressDialog()
        Toast.makeText(this, "Item deleted Successfully", Toast.LENGTH_LONG).show()
        getCartItemsList()
    }

    fun cartItemListDownloadSucces(cartItemList:ArrayList<CartItem>){
        //first hide the progress dialog
        hideProgressDialog()
        /** Check if there are any products in the list, make the recyclerview Views visible
         * then make the no product added yet to gone
         */

        /**
         * Check the product List items downloaded and loop through them to get the product id
         * and compare it with the cart item id
         * So it will go through every single product item and also through every single cart item
         */

        for(product in mProductsList){
            for (cartitem in cartItemList){
                /**
                 * check if the product id is same as the cart item id, then it will assign the product stock quantity
                 * to the cart item stock quantity
                 */
                if (product.product_id == cartitem.product_id){
                    /**
                     * This line below will help to know directly how much is in stock of that
                     * product we still have
                     */

                    cartitem.stock_quantity = product.stock_quantity

                    /**
                     * if out of stock then cant order anything thus also in cart we cant have any
                     * product ordered coz wea out of stock
                     * If only the stock quantity is 0, means there are no items to add to cart thus we shall
                     * assign the cartItem quantity to be 0 too
                     */
                    if (product.stock_quantity.toInt() == 0){
                        cartitem.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        // Just made it global that we can use it later with the data that will be passed to it
        mCartListItems = cartItemList


        // And thus we shall check the mCartListItems instead of the cartItemsList otherwise they are of same contents

        // Log.e("Downloaded cart items:", cartItemList.toString())
        if(mCartListItems.size > 0 ){
            rv_cart_items_list.visibility = View.VISIBLE
            linear_layout_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE


            // change the view layout using layoutmanager and we want to use it in this activity/this
            rv_cart_items_list.layoutManager = LinearLayoutManager(this)
            // sethasfixed size in order to make its size fixed
            rv_cart_items_list.setHasFixedSize(true)

            val cartItemsListAdapter = CartItemsListAdapter(this, mCartListItems)
            // The cartItemsListAdapter above will be assigned as the adapter of the recyclerview
            rv_cart_items_list.adapter = cartItemsListAdapter

            // Dealing with the subtotal or calculating price
            var subtotal:Double = 0.0
            /**
             * First loop through the items in the cart and get the price for each
             * multiply with the quantity for each too
             * and add them to get a subtotal of all the items in cart combined
             */
            for(item in mCartListItems){
                // Also check for the available quantity to know if its possible to increase or reduce
                // and If quantity is > than Zero the that's when we can carry on with the calculations of prices
                val availableQuantity = item.stock_quantity
                if(availableQuantity.toInt() > 0){
                    // Price for each item
                    val price = item.product_price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    // Getting subtotal of each item and add to that of the other
                    subtotal += (price * quantity)
                }
            }

            // Assign the total of the subtotal to the subtotal field in the layout
            tv_sub_total.text = "$ ${subtotal}"

            // Fr now the shipping cost can be fixed
            // TODO - Shipping charge
            /**
             * can do the logic to determine hipping charge
             * based on weight or size or data from the developer or manufacturer
             */
            tv_shipping_charge.text = "$ 10"

            // Shipping fee will be 1% of the total price of all products
            var deliveryFee:Double = 0.0
            deliveryFee = ((1.0/100.0)*(subtotal))

            tv_shipping_charge.text = "$ ${deliveryFee}"

            /**
             * check if the subtotal is greater than zero as in real sense there are items in cart
             * and then make the checkout layout visible
             */
            if (cartItemList.size > 0){
                linear_layout_checkout.visibility = View.VISIBLE

                // find the overall total after adding the shipping
                val total = subtotal + 10  // TODO - change the logic here due to shipping fee logic
                tv_total_amount.text = "$ ${total}"
            }else{
                linear_layout_checkout.visibility = View.GONE
            }

        }else{
            rv_cart_items_list.visibility = View.GONE
            linear_layout_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }


    }

    fun cartUpdateSuccess(){
        // Hide prog dialog
        hideProgressDialog()
        getCartItemsList()

    }


}