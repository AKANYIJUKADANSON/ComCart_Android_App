package com.example.eart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.Category
import com.example.eart.ui.activities.CartListActivity
import com.example.eart.ui.activities.CategoryList
import kotlinx.android.synthetic.main.cart_item_custom.view.*
import kotlinx.android.synthetic.main.category_custom.view.*

class CategoryAdapter(
    private val activity: CategoryList,
    private var categoryList:ArrayList<Category>
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val categoryLayoutFormat = LayoutInflater.from(parent.context).inflate(R.layout.category_custom, parent, false)
        return MyViewHolder(categoryLayoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCategory = categoryList[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_category_name.text = currentCategory.category_title
            holder.itemView.tv_category_description.text = currentCategory.category_description

            holder.itemView.category_delete_btn.setOnClickListener{

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



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}