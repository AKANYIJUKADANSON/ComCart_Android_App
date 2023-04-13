package com.example.eart.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.Order
import com.example.eart.modules.SoldProducts
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_sold_product_details.*
import kotlinx.android.synthetic.main.cart_item_custom.view.*
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product_details)

        // setting up the action bar
        setUpActionBar()

        var mySoldProductDetails = SoldProducts()
        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)){
            mySoldProductDetails = intent.getParcelableExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }

        // Fill the fields with order details
        setUpUIDetails(this, mySoldProductDetails)
    }

    // Action bar
    private fun setUpActionBar() {
        val soldProductDetailsToolbar = findViewById<Toolbar>(R.id.toolbar_sold_products_activity)
        setSupportActionBar(soldProductDetailsToolbar)

        val actionbar = supportActionBar
        if(actionbar != null){
            with(actionbar) {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_back_arrow)
            }
        }

        soldProductDetailsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    // setting Up sold product UI Details
    private fun setUpUIDetails(context:Context, soldProduct:SoldProducts){
        tv_sold_product_id.text = soldProduct.sold_product_id

        // Date format
        val dateFormat = "dd MMM yyyy HH:mm"
        val dateFormater = SimpleDateFormat(dateFormat, Locale.getDefault())
        tv_sold_product_date.text = soldProduct.order_date.toString()
        val calender: Calendar = Calendar.getInstance()
        calender.timeInMillis = soldProduct.order_date
        val formatedDate = dateFormater.format(calender.time)

        // Assigning date to the date field
        tv_sold_product_date.text = formatedDate

        // Product image
        GlideLoader(context).loadProductPicture(soldProduct.image,
            details_sold_product_image
        )

        sold_product_title.text = soldProduct.title
        sold_product_price.text = soldProduct.price

        val soldProductAddress = soldProduct.address
        tv_sold_product_address_type.text = soldProductAddress.address_type
        tv_sold_product_address_region.text = soldProductAddress.region
        tv_sold_product_address_name.text = soldProductAddress.addressName
        tv_sold_product_address_zipcode.text = soldProductAddress.zipCode
        tv_sold_product_mobile_number.text = soldProductAddress.phoneNumber

        tv_sold_product_sub_total.text = soldProduct.sub_total
        tv_sold_product_shipping_charge.text = soldProduct.delivery_charge
        tv_sold_product_total_amount.text = soldProduct.total_amount
    }
}