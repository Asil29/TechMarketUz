package com.example.onlineshop.screen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.model.ProductModel
import com.example.onlineshop.utils.Constants
import com.example.onlineshop.utils.PrefUtils
import kotlinx.android.synthetic.main.activity_detailed_product.*
import kotlinx.android.synthetic.main.category_item.view.*

class DetailedProductActivity : AppCompatActivity() {
    lateinit var item: ProductModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_product)
//      Gets serializible data which was sent by previous activity
        item = intent.getSerializableExtra(Constants.EXTRA_DATA) as ProductModel
//      When back button is pressed, it finishs the current acitivity
        back_button.setOnClickListener {
            var iconAnimator = ObjectAnimator.ofFloat(
                back_button,
                View.ROTATION,
                -360f, 0f
            )
            iconAnimator.duration = 200
            iconAnimator.start()
            back_button.postDelayed({
                finish()},
                200
            )
        }
//      When this button pressed, it changes the state of the fav button
        favourite_button.setOnClickListener {
            PrefUtils.setFavourite(item)

            if (PrefUtils.isFavourite(item)) {
//                var iconAnimator = ObjectAnimator.ofArgb(
//                    favourite_button,
//                    "ImageResource",
//                    R.drawable.ic_star
//                )
//                iconAnimator.duration = 150
//                iconAnimator.start()
//                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.01f)
//                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.01f)
//                val sizeAnimator = ObjectAnimator.ofPropertyValuesHolder(
//                    favourite_button, scaleX, scaleY)
//                sizeAnimator.repeatMode = ObjectAnimator.REVERSE
//                sizeAnimator.start()
                favourite_button.setImageResource(R.drawable.ic_star)
            } else {
//                var iconAnimator = ObjectAnimator.ofArgb(
//                    favourite_button,
//                    "ImageResource",
//                    R.drawable.ic_free_star
//                )
//                iconAnimator.duration = 50
//                iconAnimator.start()
//                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f)
//                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f)
//                val sizeAnimator = ObjectAnimator.ofPropertyValuesHolder(
//                    favourite_button, scaleX, scaleY)
//                sizeAnimator.repeatMode = ObjectAnimator.REVERSE
//                sizeAnimator.start()
                favourite_button.setImageResource(R.drawable.ic_free_star)
            }

        }
//      This is needed to make whether the item is on the list or not, if yes then it does not add the item again
        if (PrefUtils.getCartCount(item) > 0) {
            add_to_car_button.setText(R.string.added_to_cart)
            add_to_car_button.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_blue))
            add_to_car_button.setOnClickListener {
                startAnimation()
                Toast.makeText(this, R.string.item_is_added, Toast.LENGTH_SHORT).show()
            }
        }

//      Detailed infos of the item
        detailed_title.text = item.name
        textview_title.text = item.name
        price_textView.text = item.price

//      This is code is executed when the user visits the activtiy for the first time,
//      and shows whether item is in favorite list or not
        if (PrefUtils.isFavourite(item)) {
            favourite_button.setImageResource(R.drawable.ic_star)
        } else {
            favourite_button.setImageResource(R.drawable.ic_free_star)
        }
//      Downloads the image of product
        Glide.with(product_image_detail).load(Constants.HOST_IMAGE + item.image)
            .into(product_image_detail)
//      Adds to the cart list
        add_to_car_button.setOnClickListener {
            if (PrefUtils.getCartCount(item) == 0) {
                item.countOfCart = 1
                PrefUtils.addToCart(item)
                Toast.makeText(this, R.string.product_added, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, R.string.item_is_added, Toast.LENGTH_SHORT).show()
                startAnimation()
            }
        }
    }

    //   Specific animation executed if the item is already in the cart list when the add to cart button is pressed
    fun startAnimation() {
        var animShake: Animation = AnimationUtils.loadAnimation(this, R.anim.shake_animation)
        add_to_car_button.startAnimation(animShake)
    }
}