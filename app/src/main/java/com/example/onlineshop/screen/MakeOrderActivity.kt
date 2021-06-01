package com.example.onlineshop.screen

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.onlineshop.MapsActivity
import com.example.onlineshop.R
import com.example.onlineshop.model.AddressModel
import com.example.onlineshop.model.ProductModel
import com.example.onlineshop.utils.Constants
import kotlinx.android.synthetic.main.activity_make_order.*
import kotlinx.android.synthetic.main.activity_make_order.back_button
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*


class MakeOrderActivity : AppCompatActivity() {
    //  Variables
    var addressModel: AddressModel? = null
    val currency = "so'm"
    lateinit var items: List<ProductModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_order)

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
//      Gets the serializible data sent by previous activity
        items = intent.getSerializableExtra(Constants.EXTRA_DATA) as List<ProductModel>
//      Calculates the overall sum then displays it
        var totalAmount =
            items.sumByDouble { it.countOfCart.toDouble() * it.price.replace(" ", "").toDouble() }
        final_price_textView.setText(formatDoubleValue(totalAmount) + currency)
//      This is for user to choose the address via Google Map
        edit_address.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
//      This is for final stages of order
        final_make_order_button.setOnClickListener {
            if (!edit_address.text.isEmpty()) {
                intent = Intent(this, OrderProcessingActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                startAnimation()
            }

        }
//      When this button is pressed, it finishs the activity
        back_button.setOnClickListener {
            finish()
        }
    }

    //  Unregisters the EventBus before destoroying the acitivty, if it is registered
    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    //  This is for when user chooses the address, then it gets the address from MapsActivity
    @Subscribe
    fun catchAddress(addressModel: AddressModel) {
        this.addressModel = addressModel
        edit_address.setText(
            getCompleteAddressString(
                addressModel.latitude,
                addressModel.longtitude
            )
        )
    }

    //  This formats the overall sum when displaying it
    fun formatDoubleValue(totalAmount: Double): String {
        var formattedNumber = "%.0f\n".format(totalAmount)
        return formattedNumber
    }

    //  Extracts the address from longitude and latitude
    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var stringAddress = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                strReturnedAddress.append(returnedAddress.getAddressLine(0))
                stringAddress = strReturnedAddress.toString()
            } else {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringAddress
    }

    //  Started when user doesn't choose the address
    fun startAnimation() {
        var animShake: Animation = AnimationUtils.loadAnimation(this, R.anim.shake_animation)
        textInput_layout.startAnimation(animShake)
    }
}