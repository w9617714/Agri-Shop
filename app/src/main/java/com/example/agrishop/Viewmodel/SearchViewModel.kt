package com.example.agrishop.Viewmodel

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrishop.Data.Category
import com.example.agrishop.Data.Product
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
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firebase: FirebaseDatabase,

):ViewModel() {

    private val _searchProduct= MutableStateFlow<Rsource<List<Product>>>(Rsource.Unspecified())
    val searchProduct=_searchProduct.asStateFlow()




    fun fetchsearchProduct(name:String){
        val dataList= arrayListOf<Product>()
        viewModelScope.launch {
            _searchProduct.emit(Rsource.Loading())
        }
        firebase.getReference("products").orderByChild("product_name").equalTo(name)
            .addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for (data in snapshot.children){
                            val specialData=data.getValue(Product::class.java)


                            if(specialData!!.product_name.toLowerCase() == name)  {
                                dataList.add(specialData!!)
                            }
                        }

                        viewModelScope.launch {

                            _searchProduct.emit(Rsource.Success(dataList))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch {
                        _searchProduct.emit(Rsource.Error(error.message.toString()))
                    }
                }
            })
    }
}