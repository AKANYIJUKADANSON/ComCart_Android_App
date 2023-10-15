package com.example.eart.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.eart.DataBindingTriggerClass
import com.example.eart.R
import com.example.eart.baseactivity.BaseActivity
import com.example.eart.databinding.ActivityAddCategoryBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Category

class AddCategory : BaseActivity() {
    lateinit var binding: ActivityAddCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_category)

        // Action bar
        setUpActionBar()

        // Store data
        binding.addCategoryBtn.setOnClickListener {
            if (validateData(binding.categoryName.text.toString(), binding.categoryDescription.text.toString())){
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

        val title = binding.categoryName.text.toString()
        val description = binding.categoryDescription.text.toString()

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