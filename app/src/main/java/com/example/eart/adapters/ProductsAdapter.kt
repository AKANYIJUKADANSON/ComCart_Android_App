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
import com.example.eart.modules.PrdctDtlsClass
import com.example.eart.ui.activities.ProductDetailsActivity
import com.example.eart.ui.fragments.ProductsFragment

class ProductsAdapter(
    private val context: Context,
    private val productList: ArrayList<PrdctDtlsClass>,
    private val fragment:ProductsFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // inflating the layout format so as to use it
        val layoutFormat = ProductsItemCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(layoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentProduct = productList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentProduct.image,
                holder.customProduct.recycProductImage
            )
            holder.customProduct.recycProductTitle.text = currentProduct.productTitle
            val currency = context.resources.getString(R.string.currency)
            holder.customProduct.recycProductPrice.text = "${currency} ${currentProduct.productPrice}"
            holder.customProduct.recyclerViewStockQuantity.text = currentProduct.stock_quantity

            holder.customProduct.productsDeleteBtn.setOnClickListener {
                // Passing the product id to the delete fn
                fragment.deleteProduct(currentProduct.product_id)
            }

            // Setting the item click for each item in the recyclerview

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                /**
                 * The product_extra_id will help us to be sent to the next activity thru the intent
                 * so that it can be used and assigned to the product_id
                 * so in real sense, the document id(the id representing each product uniquely coz
                 * a product is considered as a document
                 * And thus we use the opportunity that we have the currentProduct and get its document id
                 * to send it to the productDetails activity
                 */
                intent.putExtra(Constants.PRODUCT_EXTRA_ID, currentProduct.product_id)
                intent.putExtra(Constants.PRODUCT_EXTRA_OWNER_ID, currentProduct.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    class MyViewHolder(val customProduct: ProductsItemCustomBinding): RecyclerView.ViewHolder(customProduct.root)
}