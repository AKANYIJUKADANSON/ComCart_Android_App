package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.Order
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.ProductDetailsActivity
import kotlinx.android.synthetic.main.dashboard_listitem_custom_layout.view.*

class FavoriteAdapter (
        private val context: Context,
        private val favoriteList: ArrayList<PrdctDtlsClass>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutFormat = LayoutInflater.from(parent.context).inflate(R.layout.products_item_custom, parent, false)
        return ProductsAdapter.MyViewHolder(layoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCategory = favoriteList[position]

        if (holder is DashboardItemListAdapter.MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentCategory.image,
                holder.itemView.recyc_imageview_dashboard
            )
            holder.itemView.tv_dashboard_item_name.text = currentCategory.productTitle
            val currency = context.resources.getString(R.string.currency)
            holder.itemView.tv_dashboard_item_price.text = "${Constants.CURRENCY} ${currentCategory.productPrice}"

            holder.itemView.add_to_favourate_btn.setOnClickListener {
                holder.itemView.add_to_favourate_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite))


            // Setting the item click foe each item in the recyclerview

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.PRODUCT_EXTRA_ID, currentCategory.product_id)
                intent.putExtra(Constants.PRODUCT_EXTRA_OWNER_ID, currentCategory.user_id)

                intent.putExtra("productName", currentCategory.productTitle)
                context.startActivity(intent)
            }
        }
    }
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}