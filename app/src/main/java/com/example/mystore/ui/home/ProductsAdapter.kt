package com.example.mystore.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystore.R
import com.example.mystore.data.models.Product
import com.example.mystore.ui.productDetails.ProductDetailsFragment

class ProductsAdapter(
    val context: Context,
    val listOfProducts:List<Product>,
    val fragmentManager: FragmentManager
    ): RecyclerView.Adapter<ProductsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemView:View = LayoutInflater.from(context).inflate(R.layout.layout_products,parent, false)
        return ProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = listOfProducts[position]
        //show product name
        holder.seller.text = product.seller
        //show product price
        holder.price.text = "$${product.price}"
        //show Image
        val imageUrl = product.image
        Glide.with(context)
            .load(imageUrl)
            .into(holder.productImage)

        //set onclick listener to the item
        holder.itemView.setOnClickListener {
            ProductDetailsFragment(product).show(fragmentManager,"tag")
        }
    }

    override fun getItemCount(): Int = listOfProducts.size
}
class ProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val seller: TextView = itemView.findViewById(R.id.seller)
    val price: TextView = itemView.findViewById(R.id.price)
    val productImage: ImageView = itemView.findViewById(R.id.product_image)

}