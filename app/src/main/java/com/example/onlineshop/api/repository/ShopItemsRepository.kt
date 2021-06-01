package com.example.onlineshop.api.repository

import androidx.lifecycle.MutableLiveData
import com.example.onlineshop.api.NetworkManager
import com.example.onlineshop.model.BaseResponse
import com.example.onlineshop.model.CategoryModel
import com.example.onlineshop.model.OfferModel
import com.example.onlineshop.model.ProductModel
import com.example.onlineshop.model.request.RequestProductsById
import com.example.onlineshop.utils.PrefUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ShopItemsRepository(){

    private val compositeDisposable = CompositeDisposable()
    fun getOffers(errorLiveData: MutableLiveData<String>, progressBarValue: MutableLiveData<Boolean>, offerLiveData: MutableLiveData<List<OfferModel>>){
        progressBarValue.value = true
        compositeDisposable.add(
            NetworkManager.getApiService().getOffers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<BaseResponse<List<OfferModel>>>(){
                    override fun onComplete() {

                    }

                    override fun onNext(t: BaseResponse<List<OfferModel>>) {
                        progressBarValue.value = false
                        if (t.success){
                            offerLiveData.value = t.data
                        }else {
                            errorLiveData.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        progressBarValue.value = false
                        errorLiveData.value = e.localizedMessage
                    }
                })
        )

    }

    fun getCategories(errorLiveData: MutableLiveData<String>, categoryLiveData: MutableLiveData<List<CategoryModel>>){
        compositeDisposable.add(
            NetworkManager.getApiService().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<BaseResponse<List<CategoryModel>>>(){
                    override fun onComplete() {
                    }

                    override fun onNext(t: BaseResponse<List<CategoryModel>>) {
                        if (t.success){
                            categoryLiveData.value = t.data
                        }else{
                            errorLiveData.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorLiveData.value = e.localizedMessage
                    }
                })
        )
    }

    fun getTopProducts(errorLiveData: MutableLiveData<String>, topProductLiveData: MutableLiveData<List<ProductModel>>){
        compositeDisposable.add(
            NetworkManager.getApiService().getTopProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<BaseResponse<List<ProductModel>>>(){
                    override fun onComplete() {
                    }

                    override fun onNext(t: BaseResponse<List<ProductModel>>) {
                        if (t.success){
                            topProductLiveData.value = t.data
                        }else{
                            errorLiveData.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorLiveData.value = e.localizedMessage
                    }
                })
        )
    }

    fun getProdutsByCategory(id: Int, errorLiveData: MutableLiveData<String>, topProductLiveData: MutableLiveData<List<ProductModel>>){
        compositeDisposable.add(
            NetworkManager.getApiService().getProductsByCategory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<BaseResponse<List<ProductModel>>>(){
                    override fun onComplete() {
                    }

                    override fun onNext(t: BaseResponse<List<ProductModel>>) {
                        if (t.success){
                            topProductLiveData.value = t.data
                        }else{
                            errorLiveData.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorLiveData.value = e.localizedMessage
                    }
                })
        )
    }
    
    fun getProductsById(ids: List<Int>, errorLiveData: MutableLiveData<String>, progressBarValue: MutableLiveData<Boolean>, topProductLiveData: MutableLiveData<List<ProductModel>>){
        progressBarValue.value = true
        compositeDisposable.add(
            NetworkManager.getApiService().getProductsByIds(RequestProductsById(ids))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<BaseResponse<List<ProductModel>>>(){
                    override fun onComplete() {
                    }

                    override fun onNext(t: BaseResponse<List<ProductModel>>) {
                        progressBarValue.value = false
                        if (t.success){
                            t.data.forEach {
                                it.countOfCart = PrefUtils.getCartCount(it)
                            }
                            topProductLiveData.value = t.data
                        }else{
                            errorLiveData.value = t.message
                        }
                    }

                    override fun onError(e: Throwable) {
                        progressBarValue.value = false
                         errorLiveData.value = e.localizedMessage
                    }
                })
        )
    }
}