package com.example.onlineshop.model

import java.io.Serializable

data class ProductModel(
    var id: Int,
    var name: String,
    var price: String,
    var image: String,
    var countOfCart: Int
): Serializable