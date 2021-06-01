package com.example.onlineshop.recyclerview

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.model.ProductModel
import com.example.onlineshop.screen.DetailedProductActivity
import com.example.onlineshop.utils.Constants
import kotlinx.android.synthetic.main.product_item.view.*

class ProductsAdapter(val items: List<ProductModel>): RecyclerView.Adapter<ProductsAdapter.ProductItemHolder>(){

    class ProductItemHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        return ProductItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        var item = items[position]

        holder.itemView.setOnClickListener{
            var intent = Intent(it.context, DetailedProductActivity::class.java)
            intent.putExtra(Constants.EXTRA_DATA, item)
            it.context.startActivity(intent)
        }

        Glide.with(holder.itemView.product_image).load(Constants.HOST_IMAGE + item.image).into(holder.itemView.product_image)
        holder.itemView.product_title.text = item.name
        holder.itemView.product_price.text = item.price
    }
}