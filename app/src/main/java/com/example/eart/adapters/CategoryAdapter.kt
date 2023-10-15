package com.example.eart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.databinding.CategoryCustomBinding
import com.example.eart.modules.Category
import com.example.eart.ui.activities.CategoryList

class CategoryAdapter(
    private val activity: CategoryList,
    private var categoryList:ArrayList<Category>
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val categoryLayoutFormat = CategoryCustomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(categoryLayoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCategory = categoryList[position]

        if (holder is MyViewHolder) {
            holder.customCategory.tvCategoryName.text = currentCategory.category_title
            holder.customCategory.tvCategoryDescription.text = currentCategory.category_description

            holder.customCategory.categoryDeleteBtn.setOnClickListener{

                // Show the alert to delete dialog
                activity.showAlertDialogToDelete(
                    activity,
                    "Deleting Category",
                    "Are you sure to delete the category",
                    currentCategory.category_id
                )

            }

        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }



    class MyViewHolder(val customCategory:CategoryCustomBinding) : RecyclerView.ViewHolder(customCategory.root)
}