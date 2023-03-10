package com.example.mystore.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystore.R
import com.example.mystore.data.models.Product
import com.google.android.material.button.MaterialButton

class CartAdapter(val context: Context,val cartViewModel: CartViewModel ): RecyclerView.Adapter<CartViewHolder>() {
    private val listOfSelectedProducts: List<Product> =cartViewModel.getProducts()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_cart,parent,false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product: Product = listOfSelectedProducts[position]

        //show name
        holder.seller.text = product.seller

        //show price
        holder.price.text = "$${product.price}"

        //show image
        Glide.with(context)
            .load(product.image)
            .into(holder.image)

        //show quantity
        val quantity = cartViewModel.getQuantity(product)
        holder.quantity.text = quantity.toString()

        //Remove item from cart
        holder.deleteBtn.setOnClickListener {
            cartViewModel.removeFromCart(product)
            Toast.makeText(context, "${product.seller} Deleted Successfully", Toast.LENGTH_LONG)
                .show()
        }

        //increase quantity
        holder.increaseQty.setOnClickListener {
            cartViewModel.increaseQuantity(product)
        }
        //decrease quantity
        holder.decreaseQty.setOnClickListener {
            cartViewModel.decreaseQuantity(product)
        }

        disableButtonIfQtyis1(quantity,holder.decreaseQty)
    }

    private fun disableButtonIfQtyis1(quantity: Int,button: MaterialButton) {
        button.isEnabled = quantity > 1
    }

    override fun getItemCount(): Int = listOfSelectedProducts.size

}

class CartViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    val image: ImageView = itemView.findViewById(R.id.image)
    val seller: TextView = itemView.findViewById(R.id.seller)
    val price: TextView = itemView.findViewById(R.id.price)
    val quantity: TextView = itemView.findViewById(R.id.quantity)
    val decreaseQty: MaterialButton = itemView.findViewById(R.id.decrease_quantity)
    val increaseQty: MaterialButton = itemView.findViewById(R.id.increase_quantity)
    val deleteBtn: MaterialButton = itemView.findViewById(R.id.delete)
}