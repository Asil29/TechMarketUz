package com.example.onlineshop.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onlineshop.R
import kotlinx.android.synthetic.main.activity_order_processing.*

class OrderProcessingActivity : AppCompatActivity() {
    //  This is the simple activity that just shows the two animations for order process
//  NOTE: this is just dummy activity and it does not make real order since the app itself is not legit market app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_processing)

        animationViewProcess.postDelayed({
            animationViewProcess.setAnimation(R.raw.success_anim)
        }, 3000)

        process_back_button.setOnClickListener {
            finish()
        }
    }
}