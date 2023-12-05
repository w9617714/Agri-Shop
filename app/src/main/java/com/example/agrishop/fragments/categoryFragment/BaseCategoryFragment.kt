package com.example.agrishop.fragments.categoryFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrishop.R
import com.example.agrishop.adapter.BestProductAdapter
import com.example.agrishop.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment:Fragment(R.layout.fragment_base_category) {
    private lateinit var binding:FragmentBaseCategoryBinding
  protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
  protected val bestProductsAdapter: BestProductAdapter by lazy { BestProductAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root

        setupBestProduct()

    }


open fun onBestProductPagingRequest(){

}

    private fun setupBestProduct() {

        binding.BaseCategoryRecyclerView.apply {
            layoutManager= GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter=bestProductsAdapter
        }
    }


}