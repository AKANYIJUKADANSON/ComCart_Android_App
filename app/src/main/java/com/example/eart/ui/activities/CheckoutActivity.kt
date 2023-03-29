package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.CartItemsListAdapter
import com.example.eart.adapters.CheckoutItemsListAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.*
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {

    // Will help to always check if its null because it can happen also sometimes
    private var mSelectedAddress:Address? = null
    // This variable will act as a global variable to store the product list that will be passed
    // to the success from the download all products in firstoreclass
    private lateinit var mProductsList:ArrayList<PrdctDtlsClass>
    private lateinit var mCartListItems:ArrayList<CartItem>

    private var mDeliveryFee:Double = 0.0
    private var mSubtotal:Double = 0.0
    private var mTotal:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setUpActionBar()


        /**
         * We check if whatever intent is heading to this activity has some intent with the extra wc
         * was stored in the EXTRA_SELECTED_ADDRESS constant
         * if it has then, get it assigned to the mSelectedAddress variable so that we can use it
         */
        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mSelectedAddress = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }

        // Check if mseletedAddress is not empty and if not then, populate the activity with summarized details
        if (mSelectedAddress != null){
            tv_checkout_address_type.text = mSelectedAddress!!.address_type
            tv_checkout_address_name.text = mSelectedAddress!!.addressName
            tv_checkout_address_region.text = mSelectedAddress!!.region
            tv_checkout_address_zipcode.text = mSelectedAddress!!.zipCode
            tv_checkout_mobile_number.text = mSelectedAddress!!.phoneNumber
        }

        // Getting all the products
        getAllProductsList()

        // Placing an order
        place_order_btn.setOnClickListener {
            placeOrder()
        }
    }

    private fun setUpActionBar() {
        val myToolBar = findViewById<Toolbar>(R.id.toolbar_checkout_activity)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * Getting all the products from firestore wc will help us get the details about each product
     * that will be in the cart
     *
     * ~~~~~~~~~~~~~We first load the products list and later the cart list items since we have not more info
     * about the items in cart so we can get it from the products list
     */
    private fun getAllProductsList(){
        progressDialog("Loading...")
        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    fun successProductsListFromFirestore(productList: ArrayList<PrdctDtlsClass>) {
        mProductsList = productList
        getCartItemList()
    }

    // Getting the cartItemList
    private fun getCartItemList(){
        FirestoreClass().downloadCartItemList(this)
    }

    fun successCartListFromFirestore(cartList: ArrayList<CartItem>) {
        hideProgressDialog()
        mCartListItems = cartList

        // Checking if we have the right amount of stock even when still in cart and check out
        for (product in mProductsList){
            for (cartitem in mCartListItems) {
                /**
                 * check if the product id is same as the cart item id, then it will assign the product stock quantity
                 * to the cart item stock quantity
                 */
                if (product.product_id == cartitem.product_id) {
                    /**
                     * This line below will help to see directly how much in stock of that
                     * product in cart we still have
                     */

                    cartitem.stock_quantity = product.stock_quantity

                }
            }
        }


        // setting the recyclerview and this time use the same adapter as that of the cartListAdapter
        // Setting recyclerview

        // change the view layout using layoutmanager and we want to use it in this activity/this
        rv_checkout_items_list.layoutManager = LinearLayoutManager(this)
        // sethasfixed size in order to make its size fixed
        rv_checkout_items_list.setHasFixedSize(true)


        // Setting the adapter (in this case the cartlistadapter)
        // In here there is no need to update the cartItems thus param updateCartItems set to false
        val checkoutAdapter = CheckoutItemsListAdapter(this, mCartListItems)
        rv_checkout_items_list.adapter = checkoutAdapter


        if (mCartListItems.size > 0){
            // change the view layout using layoutmanager and we want to use it in this activity/this
            rv_checkout_items_list.layoutManager = LinearLayoutManager(this)
            // sethasfixed size in order to make its size fixed
            rv_checkout_items_list.setHasFixedSize(true)

            val checkoutCartListAdapter = CheckoutItemsListAdapter(this, mCartListItems)
            // The productsAdapter above will be assigned as the adapter of the recyclerview
            rv_checkout_items_list.adapter = checkoutCartListAdapter



            // Dealing with the subtotal or calculating price

            /**
             * First loop through the items in the cart and get the price for each
             * multiply with the quantity for each too
             * and add them to get a subtotal of all the items in cart combined
             */
            for(item in mCartListItems){
                // Also check for the available quantity to know if its possible to increase or reduce
                // and If quantity is > than Zero the thats when we can carry on with the calculations of prices
                val availableQuantity:Int = item.cart_quantity.toInt()
                if(availableQuantity > 0){
                    // Price for each item
                    val price = item.product_price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    // Getting subtotal of each item and add to that of the other
                    mSubtotal += price * quantity
                }
            }

            // Assign the total of the subtotal to the subtotal field in the layout
            tv_checkout_sub_total.text = resources.getString(R.string.currency) + mSubtotal.toString()

            // Shipping fee will be 1% of the total price of all products

            mDeliveryFee = ((1.0/100.0)*(mSubtotal))

            tv_checkout_shipping_charge.text = "$ ${mDeliveryFee}"

            if (mCartListItems.size > 0){
                mTotal = mSubtotal + mDeliveryFee
                tv_checkout_total_amount.text = "$ ${mTotal}"
            }else{
                checkout_place_order_layout.visibility = View.GONE
            }
        }



    }


    private fun placeOrder(){
        progressDialog("Ordering...")
        if (mSelectedAddress != null){
            val order = Order(
                FirestoreClass().getCurrentUserID(),
                mCartListItems,
                mSelectedAddress!!,
                "Oder ${System.currentTimeMillis()}",
                mCartListItems[0].product_image,
                mSubtotal.toString(),
                mDeliveryFee.toString(),
                mTotal.toString(),
                System.currentTimeMillis()
                )

            FirestoreClass().addOrderToFirestoreCart(this@CheckoutActivity, order)
        }
    }

    fun successAddOrderToFirestore() {
        FirestoreClass().updateAllDetails(this, mCartListItems)
    }

    fun successUpdateAllDetails() {
        hideProgressDialog()
        Toast.makeText(this@CheckoutActivity,
            "Oder placed successfully",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this,Dashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}