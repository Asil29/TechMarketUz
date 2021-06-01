package com.example.onlineshop.model

data class BaseResponse<T>(
    var success: Boolean,
    var data: T,
    var message : String,
    var error_code: Int
)
