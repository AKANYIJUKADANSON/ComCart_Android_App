package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.modules.Address
import com.example.eart.modules.CartItem
import com.example.eart.modules.Constants
import com.example.eart.ui.activities.ProductDetailsActivity
import kotlinx.android.synthetic.main.address_list_custom.view.*
import kotlinx.android.synthetic.main.products_item_custom.view.*
import java.util.zip.Inflater

class AddressListAdapter(
    private val context: Context,
    private val addressList: ArrayList<Address>
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val customizedList = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_list_custom, parent, false )
        return MyViewHolder(customizedList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentAddress = addressList[position]
        if (holder is MyViewHolder){
            holder.itemView.rv_address_name.text = currentAddress.addressName
            holder.itemView.rv_address_zipcode.text = currentAddress.zipCode
            holder.itemView.rv_address_phone.text = currentAddress.phoneNumber

        }

        holder.itemView.address_delete_btn.setOnClickListener {
            // Passing the product id to the delete fn
//            fragment.deleteProduct(currentProduct.product_id)
        }

        // Setting the item click foe each item in the recyclerview

        holder.itemView.setOnClickListener {
//            val intent = Intent(context, AddressDetailst::class.java)
//            intent.putExtra(Constants.PRODUCT_EXTRA_ID, currentProduct.product_id)
//            intent.putExtra(Constants.PRODUCT_EXTRA_OWNER_ID, currentProduct.user_id)
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}