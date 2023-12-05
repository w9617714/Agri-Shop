package com.example.agrishop.fragments.categoryFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrishop.Data.Category
import com.example.agrishop.R
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.CategoryViewModel
import com.example.agrishop.Viewmodel.Factory.BaseCategoryViewModelFactory
import com.example.agrishop.adapter.BestProductAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
private val TAG="Equipment_Fragment"
@AndroidEntryPoint
class EquipmentFragment:BaseCategoryFragment() {

    @Inject
    lateinit var firebase: FirebaseDatabase
    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firebase, Category.Equipments)
    }

    private lateinit var bestProductsAdapter2: BestProductAdapter
    private lateinit var  loader: ProgressBar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bestProductsAdapter2= BestProductAdapter()
        loader=view.findViewById(R.id.progressBar)
        lifecycleScope.launch {

            viewModel.bestProducts.collectLatest {
                Log.d(TAG, it.data.toString())
                when(it){
                    is Rsource.Loading->{
                        loader.visibility=View.VISIBLE
                    }
                    is Rsource.Success->{
                        loader.visibility=View.GONE
                        val recycle=view.findViewById<RecyclerView>(R.id.BaseCategoryRecyclerView)
                        recycle.apply {
                            layoutManager= GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
                            adapter=bestProductsAdapter2
                        }
//                        bestProductsAdapter.differ.submitList(it.data)
                        bestProductsAdapter2.differ.submitList(it.data)
                    }
                    is Rsource.Error->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG)
                    }
                    else ->Unit
                }
            }
        }

    }
}