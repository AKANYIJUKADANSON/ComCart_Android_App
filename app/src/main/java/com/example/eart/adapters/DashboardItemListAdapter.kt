package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.ProductDetailsActivity
import kotlinx.android.synthetic.main.dashboard_listitem_custom_layout.view.*
import kotlinx.android.synthetic.main.products_item_custom.view.*

class DashboardItemListAdapter(
    private val context: Context,
    private val dashboardList: ArrayList<PrdctDtlsClass>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            //        inflating the layout format so as to use it
            val layoutFormat = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_listitem_custom_layout, parent, false)
            return MyViewHolder(layoutFormat)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val currentProduct = dashboardList[position]

            if (holder is MyViewHolder) {
                GlideLoader(context).loadProductPicture( currentProduct.image,
                    holder.itemView.recyc_imageview_dashboard
                )
                holder.itemView.tv_dashboard_item_name.text = currentProduct.productTitle
                val currency = context.resources.getString(R.string.currency)
                holder.itemView.tv_dashboard_item_price.text = "${currency} ${currentProduct.productPrice}"

                holder.itemView.add_to_favourate_btn.setOnClickListener {
                    holder.itemView.add_to_favourate_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite))
                    Toast.makeText(context, "Added to favourite successfully", Toast.LENGTH_LONG).show()
                }

                // Setting the item click foe each item in the recyclerview

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.PRODUCT_EXTRA_ID, currentProduct.product_id)
                    intent.putExtra(Constants.PRODUCT_EXTRA_OWNER_ID, currentProduct.user_id)

                    intent.putExtra("productName", currentProduct.productTitle)
                    context.startActivity(intent)
                }
            }
        }


        override fun getItemCount(): Int {
            return dashboardList.size
        }


        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    }