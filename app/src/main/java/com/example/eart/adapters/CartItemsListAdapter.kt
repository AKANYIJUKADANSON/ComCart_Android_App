package com.example.eart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.CartItem
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.ui.activities.CartListActivity
import com.example.eart.ui.activities.OrderDetailsActivity
import kotlinx.android.synthetic.main.cart_item_custom.view.*

class CartItemsListAdapter (
    private val context: Context,
    private var cartItemsList: ArrayList<CartItem>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cartLayoutFormat = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_custom, parent, false)
        return MyViewHolder(cartLayoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCartItem = cartItemsList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentCartItem.product_image,
                holder.itemView.recyc_cart_image
            )
            holder.itemView.recyc_cart_item_title.text = currentCartItem.product_title
            holder.itemView.recyc_cart_price.text = "$ ${currentCartItem.product_price}"
            holder.itemView.item_quantity.text = currentCartItem.cart_quantity

            /**
             * if the cart quantity is 0 then that mean that also we are out of stock of that product
             * as it is set previously
             * First check if the cart quantity is 0 then we make addition or subtraction impossible
             * by hiding the buttons for that and set the cart quantity to a string informing that
             * ts out of stock
             */
            if(currentCartItem.cart_quantity.toInt() == 0){
                holder.itemView.quantity_remove_btn.visibility = View.GONE
                holder.itemView.quantity_add_btn.visibility = View.GONE

                /**
                 * Since there is an option to also update items because we are using this adapter to
                 * serve two activities
                 */

                // Setting the text
                holder.itemView.item_quantity.text = "Out Of Stock"
                // Setting the color
                holder.itemView.item_quantity.setTextColor(
                    ContextCompat.getColor(
                        context, R.color.errorColor
                    )
                )

            }
            // If there is any item, make the - & + btns vissible
            else{

                /**
                 * This adapter will be used by other activities for recycler views
                 * so deciding on what to display in each context is considered
                 */
                when(context){
                    is OrderDetailsActivity->{
                        holder.itemView.quantity_remove_btn.visibility = View.GONE
                        holder.itemView.quantity_add_btn.visibility = View.GONE
                        holder.itemView.cart_delete_btn.visibility = View.GONE
                    }

                    is CartListActivity -> {
                        holder.itemView.quantity_remove_btn.visibility = View.VISIBLE
                        holder.itemView.quantity_add_btn.visibility = View.VISIBLE
                        // Make the color of the quantity to black again
                        holder.itemView.item_quantity.setTextColor(
                            ContextCompat.getColor(
                                context, R.color.black
                            )
                        )
                    }
                }



            }


            holder.itemView.cart_delete_btn.setOnClickListener{
                when(context){
                    is CartListActivity->{
                        context.progressDialog("please wait..")
                    }
                }
                FirestoreClass().deletingCartItem(context, currentCartItem.cartItemId)
                // The id to use is the one the item obtained as a cart item or when added to cart
            }

            // Reducing the cart item quantity
            holder.itemView.quantity_remove_btn.setOnClickListener {
                // check for the cart quantity
                // if the cart quantty is at zero then it means if we press the minus option...
                // none will remain thus delete the product from cart
                if (currentCartItem.cart_quantity  =="1"){
                    FirestoreClass().deletingCartItem(context, currentCartItem.cartItemId)
                }
                // Else if the cart qty is greater than 0ne then we are free to subtract some
                else{
                    val cartQuantity:Int = currentCartItem.cart_quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()
                    // Setting the hashmap or the exact field/ attribute we are to update
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    // If in the cartListActivity.. show the progress dialog
                    if(context is CartListActivity){
                        context.progressDialog("Please wait...")
                    }
                    FirestoreClass().updateCart(context, currentCartItem.cartItemId, itemHashMap)
                }
            }

            // Increasing the cart item quantity
            holder.itemView.quantity_add_btn.setOnClickListener {
                val cartQuantity:Int = currentCartItem.cart_quantity.toInt()

                // If the number of cart items is less than the stock value then it mean
                // we can still pick or order product from the stock
                if (cartQuantity < currentCartItem.stock_quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()
                    // Setting the hashmap or the exact field/ attribute we are to update
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()


                    // If in the cartListactivity.. show the progress dialog
                    if(context is CartListActivity){
                        context.progressDialog("Please wait...")
                    }
                    FirestoreClass().updateCart(context, currentCartItem.cartItemId, itemHashMap)

                }
                // else we are out of stock and cannot add anything to cart
                else{
                    // Show error snackbar
                    if (context is CartListActivity){
                        context.showErrorSnackBar("Current stock is: ${currentCartItem.stock_quantity}! You cannot add more that stock quantity", true)
                    }
                }


            }

        }

    }

    override fun getItemCount(): Int {
        return cartItemsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}