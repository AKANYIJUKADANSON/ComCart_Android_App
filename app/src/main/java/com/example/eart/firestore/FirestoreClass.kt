package com.example.eart.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eart.modules.*
import com.example.eart.ui.activities.*
import com.example.eart.ui.activities.Favorite
import com.example.eart.ui.fragments.DashboardFragment
import com.example.eart.ui.fragments.OrdersFragment
import com.example.eart.ui.fragments.ProductsFragment
import com.example.eart.ui.fragments.SoldProductsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FirestoreClass {

    private var mFirestore = FirebaseFirestore.getInstance()
    private var mFireStorageReference = FirebaseStorage.getInstance()

    //Creating a function using data from the registration activity and MyUser data class
    fun registerUser(activity:Registration, UserInfo: MyUser){

        //Creating a new collection that will act as a folder under which all user details will be stored
        mFirestore.collection(Constants.USERS)
            // Document is the user id given after registration
            .document(UserInfo.user_id)
            // Here we are using the fields created inMyUser class with help of UserInfo
            // Merge helps to change or add data in any field
            .set(UserInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering.",
                    e
                )
            }
    }

    /**
     * A function to get the user id of current logged user.
     */
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                /**
                 * We get the document wic is the current  user id
                 *  We convert the it result containing the results from firebase to an object
                 */

                // The type of object we want to create is of MyUser like we stored on registration
                val userDetails = document.toObject(MyUser::class.java)!!

                /**
                 * Since we have the user object
                 * we can use it to get the shared preferences which helps to store limited amount of data like strings and codes
                 *
                 */

                val sharedPreferences = activity.getSharedPreferences(Constants.MYAPP_PREFERENCES, Context.MODE_PRIVATE)
                /** Mode private make the shared prefs to be used o seen in this app only but  not the outside apps
                 * to use the shared prefs... we have to use the editor that will enable us to insert a string to store in it
                 * as below
                 */
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // ts in this editor that we are going to store the logged in username
                // Uses the params of p0 wc is the key and p1 which is the value to store
                // in this case key will be the logged_in_username and value will be the exact value
                // for example the first name and last name or email from the objet userDetails above
                editor.putString(Constants.LOGGED_IN_USERNAME, "${userDetails.firstName} ${userDetails.lastName}")

                // Then we have to apply the editor
                editor.apply()

                /**
                 * Shared prefs can also help to reduce costs esp when making requests to firebase db
                 * since the user details can be stored physically and be used anytime without first making a request to database again
                 * this reduces costs coz each req u make is a cost and the more the requests the more the jam on the app too
                 */

                when(activity){
                    is Login ->{
                        activity.loginSuccess(userDetails)
                    }

                    is Account ->{
                        activity.getUserDetailsSuccess(userDetails)
                    }

                    is EditUserData -> {
                        activity.editUserDetailsSuccess(userDetails)
                    }
                }
            }
    }


    fun storeProductInfo(activity: Addproduct, ProductInfo: PrdctDtlsClass){

        //Creating a new collection that will act as a folder under which all products details will be stored
        mFirestore.collection(Constants.PRODUCTS)
            // Document is the user id given after registration
            .document()
            // Here we are using the fields created inMyUser class with help of UserInfo
            // Merge helps to change or add data in any field if it never existed
            .set(ProductInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                //Hiding the progress dialog
                activity.progDialog.dismiss()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading product details", e
                )
                Toast.makeText(activity, " Product storage Failure",
                    Toast.LENGTH_SHORT).show()
            }
    }
    /**
     * Uploading the image to the firebase
     * */
    // To use this function we shall just provide the activity and Uri(image location on media) as the parameters
    fun uploadImageToCloud(activity: Activity, imageUri: Uri?, imageType:String){

        /**
         * This function can be used to load different kinds of pictures thus we have to specify from each activity
         * The imageType param is just any name that can easily help to identify where the image belongs for example
         * a product image can be named as " productImage followed by time in milliseconds(uniquely label the image)
         * then a dot(.), then the extension" (productImage.jpg and also for a user image could be (userImage.jpg)
         * NOTE: What will be stored in the details of the product or any other object where this image belongs is the
         * image url/download url as a string.
         */

        /**
         * passing in a child wc will be the name of the image Say naming the file
         * The user_profile_image constant will be the name of the image on the firestore storage
         * The time will act as the modifier to differentiate images
         *
         */
        val storageReference : StorageReference = mFireStorageReference.reference.child(

            imageType + System.currentTimeMillis() + "." + Constants
                .getFileExtension(activity,imageUri)
        )

        //taskSnapShot is what we will receive
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener { TaskSnapshot ->
                TaskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { imageUrl ->
                        when(activity){
                            // if ts the reg activity then upload the user profile img
                            is Registration ->{
                                activity.imageUploadSuccess(imageUrl.toString())
                            }

                            // if ts the addProduct activity then upload the product img
                            is Addproduct ->{
                                activity.imageUploadSuccess(imageUrl.toString())
                            }
                        }

                    }

        }.addOnFailureListener{
            // We shall dismiss/hide the progress dialog in the activity
            when(activity){
                is Registration->{
                    activity.hideProgressDialog()
                }

                is Addproduct->{
                    activity.hideProgressDialog()
                }

                // then also log the error
            }
                Log.e(
                    activity.javaClass.simpleName,
                    it.message,
                    it
                )
        }

    }

    fun getProductList(fragment:Fragment){
        /**
         * we shall get the products from the products collection
         * then we shall get products from a specific user using his id thus use the whereEqual fn
         * wc takes params: key and Value,..... Key is the user id and value is the exact user id got from getCurrentUserId() fn
         * from the current logged in user
         *
         *  Then we have to get the product list wc is an array list of type ProductClass (because they have same properties)
         * and later we have to loop through the data we get coz they are many products from the collection
         *
         */
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

//                Log.e("ProductList: ", document.documents.toString())

                //Creating an array that we shall put or add the products
                val productList : ArrayList<PrdctDtlsClass> = ArrayList()
//                we have to loop through the data got coz the document that   we are getting contains many products

                for (i in document.documents){
                    // This loop will contain the products that will fit in the criteria of the document obtained
                    // Then we now change each doc to an object

                    val product = i.toObject(PrdctDtlsClass::class.java)

                    // creating a new product id out of the id of the products
                    /**
                     * this id below will assign the product_id property on firebase database the document id which
                     * is located in the column of documents(ids representing each product)
                     * i.id is the id representing each product in the document column
                     */
                    product!!.product_id = i.id

                    // then now we add this product to the product list

                    productList.add(product)
                }

                /** Since we shall deal with different fragments
                 * then we have to define how each will receive its data
                 * using the when
                 */

                when(fragment){
                    is ProductsFragment ->{
                        fragment.productListDownloadSuccess(productList)

                    }
                }
            }
    }

    fun getDashboardItemList(fragment: DashboardFragment){
        // This time getting all the products, so we shall get everything in the products collection
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->

                //Creating an array that we shall put or add the products
                val productList : ArrayList<PrdctDtlsClass> = ArrayList()
//                we have to loop through the data got coz the document that we are getting contains many products

                for (i in document.documents){
                    // This loop will contain the products that will fit in the criteria of the document obtained
                    // Then we now change each doc to an object

                    val product = i.toObject(PrdctDtlsClass::class.java)

                    // creating a new product id out of the id of the products
                    product!!.product_id = i.id

                    // then now we add this product to the product list
                    productList.add(product)
                }

                // Passing the productList made to the dashboardListDownloadSuccess() function in the dashboard frag
                fragment.dashboardListDownloadSuccess(productList)

            }

            .addOnFailureListener { error ->
                // Hide the progress dialog
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting products list", error)
            }
    }

    // Deleting a product
    fun deleteProductFromStore(fragment:ProductsFragment, productID:String){
        mFirestore.collection(Constants.PRODUCTS)
                // Set the document to delete tht is using the ID
            .document(productID)
            .delete()
            .addOnSuccessListener {
                fragment.deleteProductSuccess()
            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting product", it
                )
            }
    }

    fun getExtraProductDetailsFromFirestore(activity: ProductDetailsActivity, productId:String){
        // Have to specify the exact collection to get data from and the exact product needed using the id
        mFirestore.collection(Constants.PRODUCTS)
            // Specifying the product using id
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                // Change the document retrieved into an object
                val productDetails = document.toObject(PrdctDtlsClass::class.java)
                if (productDetails != null){
                    // Passing the product object created into the function below
                    activity.productDetailsDownloadSuccess(productDetails)
                }

            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while retrieving data", e)
            }
    }

    // Function that will add the selected item to cloud in the cart table database
    // It will require the activity param and the object containing the details about  a product
    fun addItemToFirestoreCart(activity: ProductDetailsActivity, itemToAddToCart:CartItem){
        mFirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(itemToAddToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }.addOnFailureListener { e ->
                //Hiding the progress dialog
                activity.progDialog.dismiss()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding product to cart", e
                )
            }
    }

    /**
     *  check if item exists in the cart by checking the collection of cart items on cloud
     *  using the product id
    */

    fun checkIfItemExistInCart(activity: ProductDetailsActivity, productId: String) {

        mFirestore.collection(Constants.CART_ITEMS)
                // this whereto of user id will allow another user to also add the product to their cart
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->
//                Log.e("Product  Id in cart:", productId)
//                Log.e("Current user", getCurrentUserID())
//
//                Log.e("Whole doc", document.documents.toString())

                // If the document size is greater than 1 it means the product is already added to the cart.
                if (document.documents.size > 0) {
                    activity.productExistsInCart()
                } else {
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list.",
                    e
                )
            }
    }

    fun downloadCartItemList(activity: Activity){
        mFirestore.collection(Constants.CART_ITEMS)
                // We have to only get the items of that specific user logged in only
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("All item cats for user " + getCurrentUserID(),  document.documents.toString())
                // Get the list of cart items
                //Creating an array wea we shall put or add the products
                val cartItemsList : ArrayList<CartItem> = ArrayList()
//                we have to loop through the data got coz the document that we are getting contains many products

                for (i in document.documents){
                    // This loop will contain the products that will fit in the criteria of the document obtained

                    // Then we now change each doc to an object
                    val singleItem = i.toObject(CartItem::class.java)!!

                    // creating a new product id out of the id of the products
                    /**
                     * Assigning the documentId of the document(cartItem) to the cartItemId
                     * meaning that the documentId for each cartItem will be the value for the
                     * cartItemId variable
                     */
                    singleItem.cartItemId = i.id

                    // then now we add this product to the product list
                    cartItemsList.add(singleItem)
                }
                when(activity){
                    is CartListActivity->{
                         activity.cartItemListDownloadSucces(cartItemsList)
                    }

                    is CheckoutActivity->{
                        activity.successCartListFromFirestore(cartItemsList)
                    }
                }
            }.addOnFailureListener { e->
                when(activity){
                    is CartListActivity ->{
                        activity.hideProgressDialog()
                    }

                    is CheckoutActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e("Error msg","Error while fetching all products", e)
            }
    }

    // Getting the list of all products in the cartList activity for use
    fun getAllProductsList(activity: Activity){
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->

                Log.e("CartProductsList: ", document.documents.toString())
                //Creating an array that we shall put or add the products
                val productList : ArrayList<PrdctDtlsClass> = ArrayList()
//                we have to loop through the data got coz the document that   we are getting contains many products

                for (i in document.documents){
                    // This loop will contain the products that will fit in the criteria of the document obtained
                    // Then we now change each doc to an object

                    val product = i.toObject(PrdctDtlsClass::class.java)

                    // creating a new product id out of the id of the products
                    /**
                     * this id below will assign the product_id  property in the cart items on firebase database the document id which
                     * is located in the column of documents(ids representing each product)
                     */
                    product!!.product_id = i.id

                    // then now we add this product to the product list

                    productList.add(product)
                }

                when(activity){
                    is CartListActivity -> {
                        activity.successProductsListFromFirestore(productList)
                    }

                    is CheckoutActivity -> {
                        activity.successProductsListFromFirestore(productList)
                    }
                }



            }.addOnFailureListener { e->
                when(activity){
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                        Log.e("Error msg","Error while fetching all products", e)
                    }

                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                        Log.e("Error msg","Error while fetching all products", e)
                    }
                }

            }
    }

    fun updateCart(context: Context, cart_id:String, itemHashMap:HashMap<String, Any>){
        mFirestore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            /**
             * HashMap will help to pass in the whole object but will help to edit a
             * specific attribute of this object passed in this case the stock_quantity
             */
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is CartListActivity-> {
                        context.cartUpdateSuccess()
                    }
                }
            }.addOnFailureListener { e->
                when(context){
                    is CartListActivity-> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName, "Cart update failure! Something went wrong", e)
            }
    }

    // Deleting cart item
    fun deletingCartItem(context: Context, productID:String){
        mFirestore.collection(Constants.CART_ITEMS)
            // Set the document to delete tht is using the ID
            .document(productID) // Checking the item id that was passed to this function
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CartListActivity ->{
                        context.successDeleteCartItem()
                    }
                }
            }
            .addOnFailureListener {
                when(context){
                    is CartListActivity ->{
                        context.hideProgressDialog()
                        Log.e(
                            context.javaClass.simpleName,
                            "Error while deleting product", it
                        )
                    }
                }

            }
    }

    fun addAddressToFirestoreList(activity:AddAddress, addressModule: Address) {
        mFirestore.collection(Constants.ADDRESS)
            .document()
            .set(addressModule, SetOptions.merge())
            .addOnSuccessListener {
                activity.saveAddressToFirestoreSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while saving the address",
                    e
                )
            }
    }

    fun downloadAddressListFromFirestore(activity: AddressList){
        mFirestore.collection(Constants.ADDRESS).get()
            .addOnSuccessListener { address ->

                //Creating an array that we shall put or add the addresses
                val addressList : ArrayList<Address> = ArrayList()
//                we have to loop through the data got coz the document that   we are getting contains many addresses

                for (i in address.documents){
                    // This loop will contain the addresses that will fit in the criteria of the document obtained
                    // Then we now change each doc to an object

                    val addressObj = i.toObject(Address::class.java)

                    // creating a new address id out of the id of the products
                    /**
                     * this id below will assign the product_id  property in the cart items on firebase database
                     * the document id which is located in the column of documents(ids representing each product)
                     */
                    addressObj!!.address_id = i.id

                    // then now we add this address to the address list
                    addressList.add(addressObj)
                }

                activity.AddressListDownloadSuccess(addressList)
            }


             .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while saving the address",
                    e
                )
            }
    }

    fun deleteAddress(activity: AddressList, addressId:String){
        mFirestore.collection(Constants.ADDRESS)
            .document(addressId).delete()
            .addOnSuccessListener {
                activity.deleteAddressSuccess()
            }.addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting the address",
                    e
                )
            }
    }

    // Save the order made
    fun addOrderToFirestoreCart(activity: CheckoutActivity, orderPlaced:Order){
        mFirestore.collection(Constants.ORDERS)
            .document()
            .set(orderPlaced, SetOptions.merge())
            .addOnSuccessListener {
                activity.successAddOrderToFirestore()
            }.addOnFailureListener { e ->
                //Hiding the progress dialog
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while placing the order", e
                )
            }
    }

    // Orders
    fun getOrdersList(fragment: OrdersFragment){
        mFirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { orders ->

                val ordersList:ArrayList<Order> = ArrayList()

                for (i in orders.documents){
                    // Creating the object of each order
                    val orderObj = i.toObject(Order::class.java)

                    // Assigning the document id to be the order id
                    orderObj!!.order_id = i.id

                    // Adding all the objects looped to the ordersList
                    ordersList.add(orderObj)
                }

                // Passing the ordersList to the ordersFragment
                fragment.ordersListDownloadSuccess(ordersList)
            }
    }

    // Updating the cart activity after making an order
    fun updateAllDetails(activity: CheckoutActivity, cartItemArrayList:ArrayList<CartItem>, order:Order){
        /**
         * firestore batch helps to update or perform multiple activities at a tym
         * in this case we to update the cart quantity and stock quantity
         * First we shall loop through the cart items in order to update the product hashmap with
         * stock_quantity and then get rid of the cart coz the product is bought or no longer exists
         *
         * ~~~~~~~~~~~~~~~~~~~~~~WHAT EACH DOES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         * ~~~~~~~~HashMap: Will design what action will be done
         * ~~~~~~~~DocReference: Will locate the exact item(document) that will be updated
         * ~~~~~~~~Bach: will run the updating process
         */
        val writeBatch = mFirestore.batch()

        // Loop that will cater for the locating and then updating the document
        // *******************UPDATING THE CART AND PRODUCTS COLLECTIONS*************

        for (cartItem in cartItemArrayList){

            val productHashmap = HashMap<String, Any>()

            productHashmap[Constants.STOCK_QUANTITY] = (
                    cartItem.stock_quantity.toInt() - cartItem.cart_quantity.toInt()).toString()

            /**
             * ~~~~~~~~~~~Locating the document that we will work on~~~~~~~~~~~~~~~~~~
             * Creating the document reference in the collection in this case the products collection
             *  and the document we need to work on is of id cartItem product_id.
             *  NOTE: That the product_id field is both in the products collection and the cart collection thus we can easily update it
             */

            val documentReference = mFirestore.collection(Constants.PRODUCTS).document(cartItem.product_id)

            // Running or writing the batch
            writeBatch.update(documentReference, productHashmap)

        }


        for (cartItem in cartItemArrayList){
            val soldProducts = SoldProducts(
                cartItem.product_owner_id,
                cartItem.product_title,
                cartItem.product_price,
                cartItem.cart_quantity,
                cartItem.product_image,
                order.order_title,
                order.order_date,
                order.sub_total,
                order.delivery_charge,
                order.total_amount,
                order.address
            )

            /**
             * ~~~~~~~~~~~Locating the document that we will work on~~~~~~~~~~~~~~~~~~
             * Creating the document reference in the collection in this case the products collection
             *  and the document we need to work on is of id cartItem product_id.
             *  NOTE: That the product_id field is both in the products collection and the cart collection thus we can easily update it
             */

            val documentReference = mFirestore.collection(Constants.SOLD_PRODUCTS).document(cartItem.product_id)

            // Running or writing the batch
            writeBatch.set(documentReference, soldProducts)

        }


        // *********************DELETING THE ITEMS FROM THE CART COLLECTION**********
        /**
         * After updating then we have to try and make sure that we delete the items from cart
         */
        for (cartItem in cartItemArrayList) {
            val documentReference = mFirestore.collection(Constants.CART_ITEMS).document(cartItem.cartItemId)
            // run the batch
            writeBatch.delete(documentReference)
        }

        // Once done with doing all the changes using the batch then we commit it
        // commit option will write all the changes/writes in the writeBatch as a single unit
        writeBatch.commit()
            .addOnSuccessListener {
                activity.successUpdateAllDetails()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while updating the items after placing order")
            }
    }

    fun downloadSoldProductsFromFirestore(fragment: SoldProductsFragment) {
        mFirestore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { soldProducts ->
                val soldProductList:ArrayList<SoldProducts> = ArrayList()

                for (i in soldProducts){
                    val productObj = i.toObject(SoldProducts::class.java)

                    // creating a new address id out of the id of the products
                    /**
                     * this id below will assign the product_id  property in the cart items on firebase database
                     * the document id which is located in the column of documents(ids representing each product)
                     * wc wea created automatically when a document is created too and since the sold_product_id field
                     * is empty then we can assign this doc id to it
                     */
                    productObj.sold_product_id = i.id

                    soldProductList.add(productObj)

                }

                fragment.getSoldProductsFromFirestoreSuccess(soldProductList)
            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while downloading sold items")
            }
    }

    fun addCategoryToFirestoreCart(activity: AddCategory, category: Category){
        mFirestore.collection(Constants.CATEGORIES)
            .document()
            .set(category)
            .addOnSuccessListener {
                activity.addCategoryToFirestoreSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while adding category")
            }
    }

    fun downloadCategoriesFromFirestore(activity: CategoryList) {
        mFirestore.collection(Constants.CATEGORIES)
            .get()
            .addOnSuccessListener { categories ->

                val categoryList:ArrayList<Category> = ArrayList()

                for (i in categories){
                    val categoryObj = i.toObject(Category::class.java)

                    // creating a new address id out of the id of the products
                    /**
                     * this id below will assign the product_id  property in the cart items on firebase database
                     * the document id which is located in the column of documents(ids representing each product)
                     * wc wea created automatically when a document is created too and since the sold_product_id field
                     * is empty then we can assign this doc id to it
                     */
                    categoryObj.category_id = i.id

                    categoryList.add(categoryObj)

                }

                activity.downloadCategoriesFromFirestoreSuccess(categoryList)
            }
    }

    fun deletingCategory(context: Context, category_id:String){
        mFirestore.collection(Constants.CATEGORIES)
            // Set the document to delete tht is using the ID
            .document(category_id) // Checking the item id that was passed to this function
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CategoryList ->{
                        context.successDeleteCategory()
                    }
                }

//                Log.e("DELETE", "ProductToDeleteId: ${category_id}")
            }
            .addOnFailureListener {
                when(context){
                    is CategoryList ->{
                        context.hideProgressDialog()
                        Log.e(
                            context.javaClass.simpleName,
                            "Error while deleting category", it
                        )
                    }
                }

            }
    }

    fun addingProductToFavorite(product:PrdctDtlsClass){
        mFirestore.collection(Constants.FAVORITE)
            .document()
            .set(product)
            .addOnSuccessListener {
                Log.e(
                    "SUCCESS", "Success with favorite"
                )
            }
            .addOnFailureListener {e->
                Log.e(
                    javaClass.simpleName, "Error while adding to favorite", e
                )
            }
    }

    fun downloadFavoriteProducts(activity: Favorite){
        mFirestore.collection(Constants.FAVORITE)
//            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener {favoriteProducts ->
                val favoriteList:ArrayList<FavoriteClass> = ArrayList()

                for (i in favoriteProducts){
                    val favObj = i.toObject(FavoriteClass::class.java)

                    favObj.product_id = i.id

                    favoriteList.add(favObj)
                }

                activity.downloadFavoriteProductsSuccess(favoriteList)
            }
    }


}









