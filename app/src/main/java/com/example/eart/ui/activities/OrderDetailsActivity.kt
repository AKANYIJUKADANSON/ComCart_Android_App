package com.example.eart.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.CartItemsListAdapter
import com.example.eart.databinding.ActivityOrderDetailsBinding
import com.example.eart.modules.Constants
import com.example.eart.modules.Order
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details)

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
        binding.tvOrderId.text = myOrder.order_title

        // Date format
        val dateFormat = "dd MMM yyyy HH:mm"
        val dateFormater = SimpleDateFormat(dateFormat, Locale.getDefault())
        binding.tvOrderDate.text = myOrder.order_date.toString()
        val calender:Calendar = Calendar.getInstance()
        calender.timeInMillis = myOrder.order_date
        val formatedDate = dateFormater.format(calender.time)

        // Assigning date to the date field
        binding.tvOrderDate.text = formatedDate
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
                binding.tvOrderStatus.text = resources.getString(R.string.status_pending)
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this, R.color.status_pending_Color
                    )
                )
            }

            diffInTimeInHours < 2 ->{
                // make status in progress
                binding.tvOrderStatus.text = resources.getString(R.string.status_progress)
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailsActivity, R.color.status_in_progress_Color
                    )
                )
            }

            else -> {
                // make status delivered
                binding.tvOrderStatus.text = resources.getString(R.string.status_delivered)
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailsActivity, R.color.status_delivered_Color
                    )
                )
            }
        }

        // Setting the recyclerview
        binding.rvOrderItemsList.layoutManager = LinearLayoutManager(this)
        binding.rvOrderItemsList.setHasFixedSize(true)

        val adapter = CartItemsListAdapter(this, myOrder.items)
        binding.rvOrderItemsList.adapter = adapter

        val orderAddress= myOrder.address
        binding.tvOrderAddressType.text = orderAddress.address_type
        binding.tvOrderAddressName.text = orderAddress.addressName
        binding.tvOrderAddressRegion.text = orderAddress.region
        binding.tvOrderAddressZipcode.text = orderAddress.zipCode
        binding.tvOrderMobileNumber.text = orderAddress.phoneNumber

        binding.tvOrderSubTotal.text = myOrder.sub_total
        binding.tvOrderShippingCharge.text = myOrder.delivery_charge
        binding.tvOrderTotalAmount.text = myOrder.total_amount

    }

}

