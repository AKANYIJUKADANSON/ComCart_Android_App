package com.example.eart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Category
import kotlinx.android.synthetic.main.activity_add_category.*

class AddCategory : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        // Action bar
        setUpActionBar()

        // Store data
        add_category_btn.setOnClickListener {
            if (validateData(category_name.text.toString(), category_description.text.toString())){
                addCategory()
            }
        }
    }

    private fun setUpActionBar(){
        val myToolBar = findViewById<Toolbar>(R.id.categoryToolbar)
        setSupportActionBar(myToolBar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }

        myToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateData(title:String, description:String):Boolean{
        return when{
            TextUtils.isEmpty(title.trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.error_category_title), true)
                false
            }

            TextUtils.isEmpty(description.trim { it <= ' ' })->{
                showErrorSnackBar(resources.getString(R.string.error_category_description), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun addCategory(){

        progressDialog("Adding.......")

        val title = category_name.text.toString()
        val description = category_description.text.toString()

        val category = Category(
            FirestoreClass().getCurrentUserID(),
            title, description,
            System.currentTimeMillis().toString()
        )

        FirestoreClass().addCategoryToFirestoreCart(this, category)
    }


    fun addCategoryToFirestoreSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this, "Category added successfully",
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(this, CategoryList::class.java)
        startActivity(intent)
        finish()
    }

}