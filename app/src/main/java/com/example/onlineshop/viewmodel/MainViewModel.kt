package com.example.onlineshop.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlineshop.api.Api
import com.example.onlineshop.api.NetworkManager
import com.example.onlineshop.api.repository.ShopItemsRepository
import com.example.onlineshop.model.BaseResponse
import com.example.onlineshop.model.CategoryModel
import com.example.onlineshop.model.OfferModel
import com.example.onlineshop.model.ProductModel
import com.example.onlineshop.recyclerview.CategoryAdapter
import com.example.onlineshop.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel: ViewModel(){

    var shopRepository = ShopItemsRepository()

    val errorLiveData = MutableLiveData<String>()
    val offerLiveData = MutableLiveData<List<OfferModel>>()
    val categoryLiveData = MutableLiveData<List<CategoryModel>>()
    val topProductLiveData = MutableLiveData<List<ProductModel>>()
    var progressBarValue = MutableLiveData<Boolean>()

    fun getOffers(){
        shopRepository.getOffers(errorLiveData,progressBarValue, offerLiveData)
    }

    fun getCategories(){
        shopRepository.getCategories(errorLiveData, categoryLiveData)
    }

    fun getTopProducts(){
        shopRepository.getTopProducts(errorLiveData, topProductLiveData)
    }

    fun getProductsByCategory(id: Int){
        shopRepository.getProdutsByCategory(id, errorLiveData, topProductLiveData)
    }

    fun getProductsById(ids: List<Int>){
        shopRepository.getProductsById(ids, errorLiveData, progressBarValue, topProductLiveData)
    }
}