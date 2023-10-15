package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eart.R
import com.example.eart.adapters.CategoryAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityCategoryListBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Category

class CategoryList : BaseActivity() {
    private lateinit var binding: ActivityCategoryListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_list)

        // Tool bar
        setUpActionBar()

        binding.fabAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategory::class.java)
            startActivity(intent)
        }
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.categoryListToolbar)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()

        getCategoriesFromFireStore()
    }

    private fun getCategoriesFromFireStore(){
        progressDialog("loading...")

        FirestoreClass().downloadCategoriesFromFirestore(this@CategoryList)
    }


    fun downloadCategoriesFromFirestoreSuccess(categoryList: ArrayList<Category>) {
        hideProgressDialog()

        Log.e("Categories", "Cats ${categoryList}")

        if (categoryList.size > 0){
            binding.recyclerViewCategoryList.visibility = View.VISIBLE
            binding.tvNoCategory.visibility = View.GONE

            val categoryListAdapter = CategoryAdapter(this, categoryList)
            binding.recyclerViewCategoryList.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewCategoryList.setHasFixedSize(true)

            binding.recyclerViewCategoryList.adapter = categoryListAdapter

        }else{
            binding.recyclerViewCategoryList.visibility = View.GONE
            binding.tvNoCategory.visibility = View.VISIBLE
        }
    }

    fun successDeleteCategory() {
        hideProgressDialog()
        Toast.makeText(this, "Category deleted successfully", Toast.LENGTH_LONG).show()

        getCategoriesFromFireStore()
    }
}