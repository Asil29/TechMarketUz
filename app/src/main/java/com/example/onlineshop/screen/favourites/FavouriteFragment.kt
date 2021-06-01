package com.example.onlineshop.screen.favourites

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
import com.example.onlineshop.recyclerview.ProductsAdapter
import com.example.onlineshop.utils.PrefUtils
import com.example.onlineshop.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_favourite.*


class FavouriteFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      Gets viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//      Executed when products are received from API
        viewModel.topProductLiveData.observe(requireActivity(), Observer {
            if (it != null){
                fav_product_recyclerView.adapter = ProductsAdapter(it)
            }
        })
//      This is executed when there is an error while and after requesting data from API
        viewModel.errorLiveData.observe(requireActivity(), Observer{
            Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG)
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//      Recycleriew that displays the favorite items of the user
        fav_product_recyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
//      If there is no favorite items then it shows empty view
        if (PrefUtils.getFavouriteList().isEmpty()){
            swipe_refresh_layout.visibility = View.GONE
            fav_empty_view.visibility = View.VISIBLE
        }
//      Executed when the favorite items list is changed
        PrefUtils.favoriteLiveData.observe(requireActivity(), Observer {
            if (!it.isEmpty()){
                fav_empty_view.visibility = View.GONE
                swipe_refresh_layout.visibility = View.VISIBLE
                loadData()
            }else{
                swipe_refresh_layout.visibility = View.GONE
                fav_empty_view.visibility = View.VISIBLE
            }
        })
        loadData()
//      Loads data if user swipers screen from top
        swipe_refresh_layout.setOnRefreshListener{
            loadData()
        }
//      Executed when the value of progressBar is changed
        viewModel.progressBarValue.observe(requireActivity(), Observer {
            swipe_refresh_layout.isRefreshing = it
        })


    }
//  To load the data
    fun loadData(){
        viewModel.getProductsById(PrefUtils.getFavouriteList())
    }
    companion object {

        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }
}