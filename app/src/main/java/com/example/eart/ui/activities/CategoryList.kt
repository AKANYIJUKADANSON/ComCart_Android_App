package com.example.eart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.eart.R
import com.example.eart.adapters.CategoryAdapter
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Category
import kotlinx.android.synthetic.main.activity_category_list.*

class CategoryList : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        // Tool bar
        setUpActionBar()

        fabAddCategory.setOnClickListener {
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
            recycler_view_category_list.visibility = View.VISIBLE
            tv_no_category.visibility = View.GONE

            val categoryListAdapter = CategoryAdapter(this, categoryList)
            recycler_view_category_list.layoutManager = LinearLayoutManager(this)
            recycler_view_category_list.setHasFixedSize(true)

            recycler_view_category_list.adapter = categoryListAdapter

        }else{
            recycler_view_category_list.visibility = View.GONE
            tv_no_category.visibility = View.VISIBLE
        }
    }

    fun successDeleteCategory() {
        hideProgressDialog()
        Toast.makeText(this, "Category deleted successfully", Toast.LENGTH_LONG).show()

        getCategoriesFromFireStore()
    }
}