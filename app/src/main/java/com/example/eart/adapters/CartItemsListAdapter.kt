package com.example.eart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eart.R
import com.example.eart.databinding.CartItemCustomBinding
import com.example.eart.firestore.FirestoreClass
import com.example.eart.modules.CartItem
import com.example.eart.modules.Constants
import com.example.eart.modules.GlideLoader
import com.example.eart.ui.activities.CartListActivity
import com.example.eart.ui.activities.OrderDetailsActivity

class CartItemsListAdapter (
    private val context: Context,
    private var cartItemsList: ArrayList<CartItem>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cartLayoutFormat = CartItemCustomBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return MyViewHolder(cartLayoutFormat)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentCartItem = cartItemsList[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture( currentCartItem.product_image,
                holder.customItem.recycCartImage
            )
            holder.customItem.recycCartItemTitle.text = currentCartItem.product_title
            holder.customItem.recycCartPrice.text = "${Constants.CURRENCY} ${currentCartItem.product_price}"
            holder.customItem.itemQuantity.text = currentCartItem.cart_quantity

            /**
             * if the cart quantity is 0 then that mean that also we are out of stock of that product
             * as it is set previously
             * First check if the cart quantity is 0 then we make addition or subtraction impossible
             * by hiding the buttons for that and set the cart quantity to a string informing that
             * ts out of stock
             */
            if(currentCartItem.cart_quantity.toInt() == 0){
                holder.customItem.quantityRemoveBtn.visibility = View.GONE
                holder.customItem.quantityAddBtn.visibility = View.GONE

                /**
                 * Since there is an option to also update items because we are using this adapter to
                 * serve two activities
                 */

                // Setting the text
                holder.customItem.itemQuantity.text = "Out Of Stock"
                // Setting the color
                holder.customItem.itemQuantity.setTextColor(
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
                        holder.customItem.quantityRemoveBtn.visibility = View.GONE
                        holder.customItem.quantityAddBtn.visibility = View.GONE
                        holder.customItem.cartDeleteBtn.visibility = View.GONE
                    }

                    is CartListActivity -> {
                        holder.customItem.quantityRemoveBtn.visibility = View.VISIBLE
                        holder.customItem.quantityAddBtn.visibility = View.VISIBLE
                        // Make the color of the quantity to black again
                        holder.customItem.itemQuantity.setTextColor(
                            ContextCompat.getColor(
                                context, R.color.black
                            )
                        )
                    }
                }



            }


            holder.customItem.cartDeleteBtn.setOnClickListener{
                when(context){
                    is CartListActivity->{
                        context.progressDialog()
                    }
                }
                FirestoreClass().deletingCartItem(context, currentCartItem.cartItemId)
                // The id to use is the one the item obtained as a cart item or when added to cart
            }

            // Reducing the cart item quantity
            holder.customItem.quantityRemoveBtn.setOnClickListener {
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
                        context.progressDialog()
                    }
                    FirestoreClass().updateCart(context, currentCartItem.cartItemId, itemHashMap)
                }
            }

            // Increasing the cart item quantity
            holder.customItem.quantityAddBtn.setOnClickListener {
                val cartQuantity:Int = currentCartItem.cart_quantity.toInt()

                // If the number of cart items is less than the stock value then it mean
                // we can still pick or order product from the stock
                if (cartQuantity < currentCartItem.stock_quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()
                    // Setting the hashmap or the exact field/ attribute we are to update
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()


                    // If in the cartListactivity.. show the progress dialog
                    if(context is CartListActivity){
                        context.progressDialog()
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

    class MyViewHolder(val customItem:CartItemCustomBinding) : RecyclerView.ViewHolder(customItem.root)
}