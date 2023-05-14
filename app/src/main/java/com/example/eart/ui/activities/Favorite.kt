package com.example.eart.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.FavoriteAdapter
import com.example.eart.adapters.ProductsAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.PrdctDtlsClass
import kotlinx.android.synthetic.main.activity_favorite.*

class Favorite : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        downloadFavoriteProducts()

    }

    fun downloadFavoriteProducts(){
        progressDialog("Loading")
        FirestoreClass().downloadFavoriteProducts(this)
    }


    fun downloadFavoriteProductsSuccess(favorite: ArrayList<PrdctDtlsClass>){
        hideProgressDialog()

        if (favorite.size > 0){
            favorite_recyclerView.visibility = View.VISIBLE
            no_favorite_added_yet.visibility = View.GONE

            val adapter = FavoriteAdapter(this, favorite)

            favorite_recyclerView.setHasFixedSize(true)
            favorite_recyclerView.layoutManager = LinearLayoutManager(this)

            favorite_recyclerView.adapter = adapter
        }
        else{
            favorite_recyclerView.visibility = View.GONE
            no_favorite_added_yet.visibility = View.VISIBLE
        }
    }
}