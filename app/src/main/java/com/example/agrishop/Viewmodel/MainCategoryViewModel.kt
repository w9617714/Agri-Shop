package com.example.agrishop.Viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrishop.Data.Product
import com.example.agrishop.Data.SpecialProducts
import com.example.agrishop.R
import com.example.agrishop.Util.Rsource
import com.example.agrishop.fragments.fragment_Shopping.Product_Detail_fragment
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firebase: FirebaseDatabase
) : ViewModel() {
    private val _specialProducts = MutableStateFlow<Rsource<List<SpecialProducts>>>(Rsource.Unspecified())
    val specialProducts: StateFlow<Rsource<List<SpecialProducts>>> = _specialProducts

    private val _bestProducts = MutableStateFlow<Rsource<List<Product>>>(Rsource.Unspecified())
    val bestProducts: StateFlow<Rsource<List<Product>>> = _bestProducts

    private val PaginInfo=PagingInfo()

    init {
        fetchspecialProductData()
        BestProduct()
    }


    fun fetchspecialProductData() {
    viewModelScope.launch {
        _specialProducts.emit(Rsource.Loading())
    }
            val dataList= arrayListOf<SpecialProducts>()
            firebase.getReference("products").orderByChild("what").equalTo("Special product").addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for (data in snapshot.children){
                                val specialData=data.getValue(SpecialProducts::class.java)
                                dataList.add(specialData!!)
                        }

                        viewModelScope.launch {

                            _specialProducts.emit(Rsource.Success(dataList))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch {
                        _specialProducts.emit(Rsource.Error(error.message.toString()))
                    }
                }
            })


    }
    fun BestProduct(){
        viewModelScope.launch {
            _bestProducts.emit(Rsource.Loading())
        }
        val dataList= arrayListOf<Product>()
        firebase.getReference("products").limitToFirst((PaginInfo.Page *5).toInt()).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (data in snapshot.children){
                        val specialData=data.getValue(Product::class.java)
                        dataList.add(specialData!!)
                    }
                    PaginInfo.Page++

                    viewModelScope.launch {

                        _bestProducts.emit(Rsource.Success(dataList))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _bestProducts.emit(Rsource.Error(error.message.toString()))
                }
            }
        })

    }




}


internal data class PagingInfo(
     var Page:Long=1
)
