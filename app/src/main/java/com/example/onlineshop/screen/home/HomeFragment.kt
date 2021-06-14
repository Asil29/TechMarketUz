package com.example.onlineshop.screen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.R
import com.example.onlineshop.model.CategoryModel
import com.example.onlineshop.recyclerview.CategoryAdapter
import com.example.onlineshop.recyclerview.CategoryOnClick
import com.example.onlineshop.recyclerview.GridSpacingItemDecoration
import com.example.onlineshop.recyclerview.ProductsAdapter
import com.example.onlineshop.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    //  var offers: List<OfferModel> = emptyList()
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//  Gets the viewModel
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//      When users swipes from top, it refreshs the page
        swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
//      This is for building category views
        category_recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        //      This is for building product views
        product_recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 30, false))
        product_recyclerView.layoutManager =
            GridLayoutManager(requireActivity(), 2)
//      This is executed when there is an error while and after requesting data from API
        mainViewModel.errorLiveData.observe(requireActivity(), Observer {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_LONG)
        })
//      This is for CarouselView that displays fresh offers such as discounts, events

//        mainViewModel.offerLiveData.observe(requireActivity(), Observer {
//            carouselView.setImageListener { position, imageView ->
//                Glide.with(imageView).load(Constants.HOST_IMAGE + it[position].image)
//                    .into(imageView)
//            }
//            carouselView.pageCount = it.count()
//        })

//      Executed when category data is received from API
        mainViewModel.categoryLiveData.observe(requireActivity(), Observer {
            category_recyclerView.adapter =
                CategoryAdapter(it ?: emptyList(), object : CategoryOnClick {
                    override fun onClickItem(item: CategoryModel) {
                        mainViewModel.getProductsByCategory(item.id)
                    }
                })
        })
//      Executed when products are received from API
        mainViewModel.topProductLiveData.observe(requireActivity(), Observer {
            if (it.isEmpty()) {
                empty_view.visibility = View.VISIBLE
                product_recyclerView.visibility = View.GONE
            } else {
                empty_view.visibility = View.GONE
                product_recyclerView.visibility = View.VISIBLE
                product_recyclerView.adapter = ProductsAdapter(it)
            }
        })
//      Executed if the value of the progressBar is changed such as TRUE or FALSE
        mainViewModel.progressBarValue.observe(requireActivity(), Observer {
            swipeRefreshLayout.isRefreshing = it
        })

        loadData()

    }

    //  Function that loads the data
    fun loadData() {
        mainViewModel.getOffers()
        mainViewModel.getCategories()
        mainViewModel.getTopProducts()
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}