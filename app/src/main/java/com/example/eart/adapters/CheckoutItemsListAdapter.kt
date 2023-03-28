package com.example.eart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.modules.CartItem
import com.example.eart.modules.GlideLoader
import kotlinx.android.synthetic.main.cart_item_custom.view.*
import kotlinx.android.synthetic.main.checkout_product_custom.view.*

class CheckoutItemsListAdapter (
    private val context: Context,
    private val cartItemsList: ArrayList<CartItem>,
//    private val activity: CartListActivity
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cartLayoutFormat = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkout_product_custom, parent, false)
        return MyViewHolder(cartLayoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCartItem = cartItemsList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentCartItem.product_image,
                holder.itemView.recyc_checkout_image
            )
            holder.itemView.recyc_checkout_item_title.text = currentCartItem.product_title
            holder.itemView.recyc_checkout_price.text = "$ ${currentCartItem.product_price}"
        }

    }

    override fun getItemCount(): Int {
        return cartItemsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}