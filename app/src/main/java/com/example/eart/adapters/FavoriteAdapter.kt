package com.example.eart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.modules.*
import kotlinx.android.synthetic.main.custom_favorite_item.view.*

class FavoriteAdapter (
        private val context: Context,
        private val favoriteList: ArrayList<FavoriteClass>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutFormat = LayoutInflater.from(parent.context).inflate(R.layout.custom_favorite_item, parent, false)
        return MyViewHolder(layoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentFavorite = favoriteList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentFavorite.image,
                holder.itemView.favorite_imageview
            )
            holder.itemView.favorite_item_name.text = currentFavorite.productTitle
            holder.itemView.favorite_item_price.text = "${Constants.CURRENCY} ${currentFavorite.productPrice}"

            // Setting the item click foe each item in the recyclerview

//            holder.itemView.setOnClickListener {
//                val intent = Intent(context, ProductDetailsActivity::class.java)
//                intent.putExtra(Constants.PRODUCT_EXTRA_ID, currentFavorite.product_id)
//                intent.putExtra(Constants.PRODUCT_EXTRA_OWNER_ID, currentFavorite.user_id)
//
//                intent.putExtra("productName", currentFavorite.productTitle)
//                context.startActivity(intent)
//            }
        }
    }


    override fun getItemCount(): Int {
        return favoriteList.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}