package com.example.onlineshop.utils

import androidx.lifecycle.MutableLiveData
import com.example.onlineshop.model.CartModel
import com.example.onlineshop.model.ProductModel
import com.orhanobut.hawk.Hawk

object PrefUtils{
    private const val PREF_FAVOURITES = "favorite_product"
    private const val PREF_CART = "pref_cart"
    var cartLiveData = MutableLiveData<List<CartModel>>()
    var favoriteLiveData = MutableLiveData<List<Int>>()

    fun setFavourite(item: ProductModel){
        val items = Hawk.get(PREF_FAVOURITES, arrayListOf<Int>())

        if (items.filter { it == item.id }.firstOrNull() != null){
            items.remove(item.id)
        } else {
            items.add(item.id)
        }
        Hawk.put(PREF_FAVOURITES, items)
        isChangedFavList(items)
    }

    fun getFavouriteList(): ArrayList<Int>{
        return Hawk.get(PREF_FAVOURITES, arrayListOf())
    }

    fun isFavourite(item: ProductModel): Boolean{
        val items = Hawk.get(PREF_FAVOURITES, arrayListOf<Int>())
        return items.filter{it == item.id}.firstOrNull() != null
    }

    fun addToCart(item: ProductModel){
        val items = Hawk.get<ArrayList<CartModel>>(PREF_CART, arrayListOf<CartModel>())
        val cartItem = items.filter{it.product_id == item.id}.firstOrNull()
        if(cartItem != null){
            if(item.countOfCart > 0){
                cartItem.count = item.countOfCart
            }
          }else{
            var newCart = CartModel(item.id, item.countOfCart)
            items.add(newCart)
        }
        Hawk.put(PREF_CART, items)
        isChangedList(items)
    }

    fun deleteFromCart(item: ProductModel){
        val items = Hawk.get<ArrayList<CartModel>>(PREF_CART, arrayListOf<CartModel>())
        val cartItem = items.filter{it.product_id == item.id}.firstOrNull()
        if (cartItem!=null){
            items.remove(cartItem)
        }
        Hawk.put(PREF_CART, items)
        isChangedList(items)
    }

    fun getCartList(): ArrayList<CartModel>{
        return Hawk.get(PREF_CART, arrayListOf<CartModel>())
    }

    fun getCartCount(item: ProductModel): Int{
        val items = Hawk.get<ArrayList<CartModel>>(PREF_CART, arrayListOf<CartModel>())
        return items.filter{it.product_id == item.id}.firstOrNull()?.count ?: 0
    }

    fun setFCMToken(value: String){
        Hawk.put(Constants.FCM_TOKEN, value)
    }

    fun getFCMToken(): String{
        return Hawk.get(Constants.FCM_TOKEN, "")
    }

    fun isChangedList(items: List<CartModel>){
        cartLiveData.value = items
    }

    fun isChangedFavList(ids: List<Int>){
        favoriteLiveData.value = ids
    }
}