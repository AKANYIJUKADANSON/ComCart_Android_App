package com.example.eart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.databinding.CheckoutProductCustomBinding
import com.example.eart.modules.CartItem
import com.example.eart.modules.GlideLoader

class CheckoutItemsListAdapter (
    private val context: Context,
    private val cartItemsList: ArrayList<CartItem>,
//    private val activity: CartListActivity
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cartLayoutFormat = CheckoutProductCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(cartLayoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCartItem = cartItemsList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentCartItem.product_image,
                holder.checkOutProduct.recycCheckoutImage
            )
            holder.checkOutProduct.recycCheckoutItemTitle.text = currentCartItem.product_title
            val currency = context.resources.getString(R.string.currency)
            holder.checkOutProduct.recycCheckoutPrice.text = "${currency} ${currentCartItem.product_price}"
        }

    }

    override fun getItemCount(): Int {
        return cartItemsList.size
    }

    class MyViewHolder(val checkOutProduct:CheckoutProductCustomBinding) : RecyclerView.ViewHolder(checkOutProduct.root)
}