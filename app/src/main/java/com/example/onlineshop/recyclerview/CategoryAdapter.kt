package com.example.onlineshop.recyclerview

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.R
import com.example.onlineshop.model.CategoryModel
import kotlinx.android.synthetic.main.category_item.view.*

interface CategoryOnClick{
    fun onClickItem(item: CategoryModel)
}

class CategoryAdapter(val items: List<CategoryModel>, val categoryCallBack: CategoryOnClick): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var item = items[position]

        holder.itemView.setOnClickListener{
            items.forEach{
                it.checked = false
            }

            item.checked = true

            categoryCallBack.onClickItem(item)
            notifyDataSetChanged()
        }

        holder.itemView.category_title.text = item.title

        if (item.checked) {
//            holder.itemView.categoryCardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAccent))
//            holder.itemView.category_title.setTextColor(Color.WHITE)
            var cardAnimator = ObjectAnimator.ofArgb(
                holder.itemView.categoryCardView,
                "CardBackgroundColor",
                ContextCompat.getColor(holder.itemView.context, R.color.colorAccent)
            )
            cardAnimator.duration = 200
            cardAnimator.start()
            holder.itemView.category_title.setTextColor(Color.WHITE)
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.07f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.07f)
            val sizeAnimator = ObjectAnimator.ofPropertyValuesHolder(
                holder.itemView.categoryCardView, scaleX, scaleY)
            sizeAnimator.start()
        } else {
            holder.itemView.categoryCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.gray
                )
            )
            holder.itemView.category_title.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.dark_gray
                )
            )
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
            val sizeAnimator = ObjectAnimator.ofPropertyValuesHolder(
                holder.itemView.categoryCardView, scaleX, scaleY)
            sizeAnimator.start()
        }
    }


}