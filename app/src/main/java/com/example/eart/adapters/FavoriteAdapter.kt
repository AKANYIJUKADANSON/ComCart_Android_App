package com.example.eart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.databinding.CustomFavoriteItemBinding
import com.example.eart.modules.*

class FavoriteAdapter (
        private val context: Context,
        private val favoriteList: ArrayList<FavoriteClass>
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutFormat = CustomFavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(layoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentFavorite = favoriteList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentFavorite.image,
                holder.customFavoriteItem.favoriteImageview
            )
            holder.customFavoriteItem.favoriteItemTitle.text = currentFavorite.productTitle
            holder.customFavoriteItem.favoriteItemPrice.text = "${Constants.CURRENCY} ${currentFavorite.productPrice}"

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



    class MyViewHolder(val customFavoriteItem: CustomFavoriteItemBinding) : RecyclerView.ViewHolder(customFavoriteItem.root)

}