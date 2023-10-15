package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.databinding.AddressListCustomBinding
import com.example.eart.modules.Address
import com.example.eart.modules.Constants
import com.example.eart.ui.activities.CheckoutActivity

class AddressListAdapter(
    private val context: Context,
    private val addressList: ArrayList<Address>,
    // selectAddress param will help the adapter know if we are selecting or making changes
    private val selectAddress:Boolean
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val customizedList = AddressListCustomBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(customizedList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentAddress = addressList[position]
        if (holder is MyViewHolder){
            holder.customAddress.rvAddressName.text = currentAddress.addressName
            holder.customAddress.rvAddressZipcode.text = currentAddress.zipCode
            holder.customAddress.rvAddressPhone.text = currentAddress.phoneNumber

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

    class MyViewHolder(val customAddress:AddressListCustomBinding) : RecyclerView.ViewHolder(customAddress.root)
}