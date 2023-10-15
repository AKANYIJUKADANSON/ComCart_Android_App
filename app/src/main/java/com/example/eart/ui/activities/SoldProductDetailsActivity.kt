package com.example.eart.ui.activities

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivitySoldProductDetailsBinding
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.SoldProducts
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivitySoldProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sold_product_details)

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
        val soldProductDetailsToolbar = binding.toolbarSoldProductsActivity
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
        binding.tvSoldProductId.text = soldProduct.sold_product_id

        // Date format
        val dateFormat = "dd MMM yyyy HH:mm"
        val dateFormater = SimpleDateFormat(dateFormat, Locale.getDefault())
        binding.tvSoldProductDate.text = soldProduct.order_date.toString()
        val calender: Calendar = Calendar.getInstance()
        calender.timeInMillis = soldProduct.order_date
        val formatedDate = dateFormater.format(calender.time)

        // Assigning date to the date field
        binding.tvSoldProductDate.text = formatedDate

        // Product image
        GlideLoader(context).loadProductPicture(soldProduct.image,
            binding.detailsSoldProductImage
        )

        binding.soldProductTitle.text = soldProduct.title
        binding.soldProductPrice.text = soldProduct.price

        val soldProductAddress = soldProduct.address
        binding.tvSoldProductAddressType.text = soldProductAddress.address_type
        binding.tvSoldProductAddressRegion.text = soldProductAddress.region
        binding.tvSoldProductAddressName.text = soldProductAddress.addressName
        binding.tvSoldProductAddressZipcode.text = soldProductAddress.zipCode
        binding.tvSoldProductMobileNumber.text = soldProductAddress.phoneNumber

        binding.tvSoldProductSubTotal.text = soldProduct.sub_total
        binding.tvSoldProductShippingCharge.text = soldProduct.delivery_charge
        binding.tvSoldProductTotalAmount.text = soldProduct.total_amount
    }
}