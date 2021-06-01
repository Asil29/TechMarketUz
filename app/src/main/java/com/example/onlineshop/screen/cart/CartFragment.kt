package com.example.onlineshop.screen.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.R
import com.example.onlineshop.model.ProductModel
import com.example.onlineshop.recyclerview.CartAdapter
import com.example.onlineshop.screen.MakeOrderActivity
import com.example.onlineshop.utils.Constants
import com.example.onlineshop.utils.PrefUtils
import com.example.onlineshop.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import java.io.Serializable

class CartFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    lateinit var cartAdapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      Gets viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//      Executed when the value of progressBar is changed
        viewModel.progressBarValue.observe(requireActivity(), Observer {
            cart_swipe_refresh_layout.isRefreshing = it
        })
//      Executed if there is an error while/after requesting the data from API
        viewModel.errorLiveData.observe(requireActivity(), Observer {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG).show()
        })
//      Executed when product data is received from API
        viewModel.topProductLiveData.observe(requireActivity(), Observer {
            cart_recyclerview.adapter = CartAdapter(it.toMutableList())
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//      RecyclerView that displays the items in the cart
        cart_recyclerview.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
//      Executed when user swipers screen from top, to refresh it
        cart_swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
//      to loadData
        loadData()
//      Executed if there is no item in the cart
        if (PrefUtils.getCartList().isEmpty()) {
            cart_swipe_refresh_layout.visibility = View.GONE
            cart_empty_view.visibility = View.VISIBLE
            make_order_button.visibility = View.GONE
        }
//      Executed if the cart list is changed
        PrefUtils.cartLiveData.observe(requireActivity(), Observer {
            if (it.isEmpty()) {
                cart_swipe_refresh_layout.visibility = View.GONE
                cart_empty_view.visibility = View.VISIBLE
                make_order_button.visibility = View.GONE
            } else {
                cart_empty_view.visibility = View.GONE
                cart_swipe_refresh_layout.visibility = View.VISIBLE
                make_order_button.visibility = View.VISIBLE
                loadData()
            }
        })
//      Executed when user clicks the make order button
        make_order_button.setOnClickListener {
            var intent = Intent(requireActivity(), MakeOrderActivity::class.java)
            intent.putExtra(
                Constants.EXTRA_DATA,
                (viewModel.topProductLiveData.value ?: emptyList<ProductModel>()) as Serializable
            )
            startActivity(intent)
        }
    }
//  Function to load the data
    fun loadData() {
        viewModel.getProductsById(PrefUtils.getCartList().map { it.product_id })
    }

    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }
}