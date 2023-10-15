package com.example.eart.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eart.R
import com.example.eart.adapters.FavoriteAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.FavoriteClass
import kotlinx.android.synthetic.main.activity_favorite.*

class Favorite : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setUpActionBar()
        downloadFavoriteProducts()

    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.favoriteToolbar)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun downloadFavoriteProducts(){
        progressDialog("Loading")
        FirestoreClass().downloadFavoriteProducts(this)
    }


    fun downloadFavoriteProductsSuccess(favorite: ArrayList<FavoriteClass>){
        hideProgressDialog()

        if (favorite.size > 0){

            Log.e("Favorites", favorite.toString())
            favorite_recyclerView.visibility = View.VISIBLE
            no_favorite_added_yet.visibility = View.GONE

            val adapter = FavoriteAdapter(this, favorite)

            favorite_recyclerView.setHasFixedSize(true)
            favorite_recyclerView.layoutManager = GridLayoutManager(this, 2)

            favorite_recyclerView.adapter = adapter
        }
        else{
            favorite_recyclerView.visibility = View.GONE
            no_favorite_added_yet.visibility = View.VISIBLE
        }
    }
}