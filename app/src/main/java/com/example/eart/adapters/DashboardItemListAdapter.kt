package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.databinding.DashboardListitemCustomLayoutBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.ProductDetailsActivity

class DashboardItemListAdapter(
    private val context: Context,
    private val dashboardList: ArrayList<PrdctDtlsClass>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            //        inflating the layout format so as to use it
            val layoutFormat = DashboardListitemCustomLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MyViewHolder(layoutFormat)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val currentProduct = dashboardList[position]

            if (holder is MyViewHolder) {
                GlideLoader(context).loadProductPicture( currentProduct.image,
                    holder.dashboardItem.recycImageviewDashboard
                )
                holder.dashboardItem.tvDashboardItemName.text = currentProduct.productTitle
                val currency = context.resources.getString(R.string.currency)
                holder.dashboardItem.tvDashboardItemPrice.text = "${Constants.CURRENCY} ${currentProduct.productPrice}"

                holder.dashboardItem.addToFavourateBtn.setOnClickListener {
                    holder.dashboardItem.addToFavourateBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite))

                    val productToAddToFavorite = PrdctDtlsClass(
                        currentProduct.user_id,
                        currentProduct.username,
                        currentProduct.productTitle,
                        currentProduct.productPrice,
                        currentProduct.prodctDescrptn,
                        currentProduct.stock_quantity,
                        currentProduct.image,
                        currentProduct.product_id
                    )

                    FirestoreClass().addingProductToFavorite(productToAddToFavorite)
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


        private class MyViewHolder(val dashboardItem: DashboardListitemCustomLayoutBinding): RecyclerView.ViewHolder(dashboardItem.root)
    }