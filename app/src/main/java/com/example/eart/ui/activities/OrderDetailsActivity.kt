package com.example.eart.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.CartItemsListAdapter
import com.example.eart.modules.Constants
import com.example.eart.modules.Order
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_order_details.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class OrderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        setUpActionBar()



        var myOrderDetails = Order()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)){
            myOrderDetails = intent.getParcelableExtra(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }

        // Fill the fields with order details
        setUpUIDetails(myOrderDetails)
    }

    private fun setUpActionBar() {
        val productDetailsToolbar = findViewById<Toolbar>(R.id.toolbar_order_details_activity)
        setSupportActionBar(productDetailsToolbar)

        val actionbar = supportActionBar
        if(actionbar != null){
            with(actionbar) {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_back_arrow)
            }
        }

        productDetailsToolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun setUpUIDetails(myOrder:Order){
        tv_order_id.text = myOrder.order_title

        // Date format
        val dateFormat = "dd MMM yyyy HH:mm"
        val dateFormater = SimpleDateFormat(dateFormat, Locale.getDefault())
        tv_order_date.text = myOrder.order_date.toString()
        val calender:Calendar = Calendar.getInstance()
        calender.timeInMillis = myOrder.order_date
        val formatedDate = dateFormater.format(calender.time)

        // Assigning date to the date field
        tv_order_date.text = formatedDate
        /**
         * Try to change the time to hour in order to play around with the status
         * for example if the difference in time when the order was made is < 1 hr
         * then make the status pending with some color and if like greater than two then
         * status in progress with some other color
         */

        val diffInMillisecondsTime:Long = System.currentTimeMillis() - myOrder.order_date
        // Changing the difference into hours
        val diffInTimeInHours = TimeUnit.MILLISECONDS.toHours(diffInMillisecondsTime)

        when {
            diffInTimeInHours < 1 ->{
                // make status pending
                tv_order_status.text = resources.getString(R.string.status_pending)
                tv_order_status.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.status_pending_Color
                    )
                )
            }

            diffInTimeInHours < 2 ->{
                // make status in progress
                tv_order_status.text = resources.getString(R.string.status_progress)
                tv_order_status.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailsActivity, R.color.status_in_progress_Color
                    )
                )
            }

            else -> {
                // make status delivered
                tv_order_status.text = resources.getString(R.string.status_delivered)
                tv_order_status.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailsActivity, R.color.status_delivered_Color
                    )
                )
            }
        }

        // Setting the recyclerview
        rv_order_items_list.layoutManager = LinearLayoutManager(this)
        rv_order_items_list.setHasFixedSize(true)

        val adapter = CartItemsListAdapter(this, myOrder.items)
        rv_order_items_list.adapter = adapter

        val orderAddress= myOrder.address
        tv_order_address_type.text = orderAddress.address_type
        tv_order_address_name.text = orderAddress.addressName
        tv_order_address_region.text = orderAddress.region
        tv_order_address_zipcode.text = orderAddress.zipCode
        tv_order_mobile_number.text = orderAddress.phoneNumber

        tv_order_sub_total.text = myOrder.sub_total
        tv_order_shipping_charge.text = myOrder.delivery_charge
        tv_order_total_amount.text = myOrder.total_amount

    }

}

