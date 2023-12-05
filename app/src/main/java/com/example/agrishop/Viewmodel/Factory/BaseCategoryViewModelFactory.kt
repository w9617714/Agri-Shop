package com.example.agrishop.Viewmodel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agrishop.Data.Category
import com.example.agrishop.Viewmodel.CategoryViewModel
import com.google.firebase.database.FirebaseDatabase

class BaseCategoryViewModelFactory(
    private val firebase:FirebaseDatabase ,
    private val category: Category
):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firebase,category) as T
    }
}