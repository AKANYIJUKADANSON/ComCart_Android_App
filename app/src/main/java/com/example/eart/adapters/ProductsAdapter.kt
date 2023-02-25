package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.ProductDetailsActivity
import com.example.eart.ui.fragments.ProductsFragment
import kotlinx.android.synthetic.main.products_item_custom.view.*

class ProductsAdapter(
    private val context: Context,
    private val productList: ArrayList<PrdctDtlsClass>,
    private val fragment:ProductsFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //        inflating the layout format so as to use it
        val layoutFormat = LayoutInflater.from(parent.context).inflate(R.layout.products_item_custom, parent, false)
        return MyViewHolder(layoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentProduct = productList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentProduct.image,
                holder.itemView.recyc_productImage
            )
            holder.itemView.recyc_productTitle.text = currentProduct.productTitle
            holder.itemView.recyc_productPrice.text = "$ ${currentProduct.productPrice}"

            holder.itemView.products_delete_btn.setOnClickListener {
                // Passing the product id to the delete fn
                fragment.deleteProduct(currentProduct.product_id)
            }

            // Setting the item click foe each item in the recyclerview

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.PRODUCT_EXTRA_ID, currentProduct.product_id)
                intent.putExtra(Constants.PRODUCT_EXTRA_OWNER_ID, currentProduct.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}