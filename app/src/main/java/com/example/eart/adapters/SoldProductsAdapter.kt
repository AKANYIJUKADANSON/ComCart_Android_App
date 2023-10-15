package com.example.eart.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.databinding.ProductsItemCustomBinding
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.modules.SoldProducts
import com.example.eart.ui.activities.SoldProductDetailsActivity

class SoldProductsAdapter(
    private val context: Context,
    private val soldProductList: ArrayList<SoldProducts>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // inflating the layout format so as to use it
        val layoutFormat = ProductsItemCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(layoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentSoldProduct = soldProductList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentSoldProduct.image,
                holder.customSoldProduct.recycProductImage
            )
            holder.customSoldProduct.recycProductTitle.text = currentSoldProduct.title
            val currency = context.resources.getString(R.string.currency)
            holder.customSoldProduct.recycProductPrice.text = "${currency} ${currentSoldProduct.price}"
            holder.customSoldProduct.recyclerViewStockQuantity.text = currentSoldProduct.sold_quantity

            holder.customSoldProduct.productsDeleteBtn.visibility = View.GONE

            // Setting the item click for each item in the recyclerview

            holder.itemView.setOnClickListener {
                val intent = Intent(context, SoldProductDetailsActivity::class.java)
                /**
                 * The product_extra_id will help us to be sent to the next activity thru the intent
                 * so that it can be used and assigned to the product_id
                 * so in real sense, the document id(the id representing each product uniquely coz
                 * a product is considered as a document
                 * And thus we use the opportunity that we have the currentProduct and get its document id
                 * to send it to the productDetails activity
                 */
                intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS, currentSoldProduct)
                intent.putExtra(Constants.SOLD_PRODUCT_EXTRA_ID, currentSoldProduct.sold_product_id)
                intent.putExtra(Constants.PRODUCT_EXTRA_OWNER_ID, currentSoldProduct.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return soldProductList.size
    }

    class MyViewHolder(val customSoldProduct: ProductsItemCustomBinding): RecyclerView.ViewHolder(customSoldProduct.root)

}