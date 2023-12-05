package com.example.agrishop.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrishop.Data.Category
import com.example.agrishop.Data.Product
import com.example.agrishop.Data.SpecialProducts
import com.example.agrishop.Util.Rsource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryViewModel constructor(
    private val firebase:FirebaseDatabase ,
    private val category: Category
):ViewModel() {
    private val _bestProduct=MutableStateFlow<Rsource<List<Product>>>(Rsource.Unspecified())
    val bestProducts=_bestProduct.asStateFlow()


    init {
        fetchbestProduct()
    }

    fun fetchbestProduct(){
        val dataList= arrayListOf<Product>()
        viewModelScope.launch {
            _bestProduct.emit(Rsource.Loading())
        }
        firebase.getReference("products").orderByChild("category").equalTo(category.Category)
            .addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for (data in snapshot.children){
                            val specialData=data.getValue(Product::class.java)
                            dataList.add(specialData!!)
                        }

                        viewModelScope.launch {

                            _bestProduct.emit(Rsource.Success(dataList))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch {
                        _bestProduct.emit(Rsource.Error(error.message.toString()))
                    }
                }
            })
    }
}