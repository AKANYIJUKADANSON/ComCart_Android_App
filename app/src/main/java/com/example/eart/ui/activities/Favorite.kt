package com.example.eart.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eart.R
import com.example.eart.adapters.FavoriteAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityFavoriteBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.FavoriteClass

class Favorite : BaseActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite)

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
            binding.favoriteRecyclerView.visibility = View.VISIBLE
            binding.noFavoriteAddedYet.visibility = View.GONE

            val adapter = FavoriteAdapter(this, favorite)

            binding.favoriteRecyclerView.setHasFixedSize(true)
            binding.favoriteRecyclerView.layoutManager = GridLayoutManager(this, 2)

            binding.favoriteRecyclerView.adapter = adapter
        }
        else{
            binding.favoriteRecyclerView.visibility = View.GONE
            binding.noFavoriteAddedYet.visibility = View.VISIBLE
        }
    }
}