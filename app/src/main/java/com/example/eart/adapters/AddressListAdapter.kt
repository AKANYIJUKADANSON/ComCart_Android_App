package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Address
import com.example.eart.modules.CartItem
import com.example.eart.modules.Constants
import com.example.eart.ui.activities.AddressList
import com.example.eart.ui.activities.CheckoutActivity
import com.example.eart.ui.activities.ProductDetailsActivity
import kotlinx.android.synthetic.main.address_list_custom.view.*
import kotlinx.android.synthetic.main.products_item_custom.view.*
import java.util.zip.Inflater

class AddressListAdapter(
    private val context: Context,
    private val addressList: ArrayList<Address>,
    // selectAddress param will help the adapter know if we are selecting or making changes
    private val selectAddress:Boolean
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

            // if selectAddress is true then display the address selected
            if (selectAddress){
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, CheckoutActivity::class.java)
                    // Send the selected address to the next activity wc is the (checkout)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, currentAddress)
                    context.startActivity(intent)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}