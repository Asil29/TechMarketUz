package com.example.onlineshop.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.model.ProductModel
import com.example.onlineshop.utils.Constants
import com.example.onlineshop.utils.PrefUtils
import kotlinx.android.synthetic.main.cart_item_layout.view.*


class CartAdapter(val items: MutableList<ProductModel>): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var item = items[position]

        holder.itemView.item_title.text = item.name
        holder.itemView.item_price.text = item.price
        holder.itemView.item_count.text = item.countOfCart.toString()

        Glide.with(holder.itemView).load(Constants.HOST_IMAGE + item.image).into(holder.itemView.item_image)

        holder.itemView.minus_icon.setOnClickListener {
            minusFunction(item)
        }
        holder.itemView.plus_icon.setOnClickListener {
            plusFunction(item)
        }

        holder.itemView.delete_button.setOnClickListener {
            delete(position, item)
        }

    }

    fun delete(position: Int, item: ProductModel){
        items.removeAt(position)
        PrefUtils.deleteFromCart(item)
        notifyDataSetChanged()
    }

    fun minusFunction(item: ProductModel){
        if(item.countOfCart - 1 < 0){
            return
        }
        item.countOfCart--
        notifyDataSetChanged()
        }

    fun plusFunction(item: ProductModel){
        if (item.countOfCart + 1 > 10){
            return
        }
        item.countOfCart++
        notifyDataSetChanged()
    }

    }


