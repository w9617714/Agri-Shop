package com.example.agrishop.fragments.fragment_Shopping

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrishop.Data.Product
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.MainCategoryViewModel
import com.example.agrishop.Viewmodel.SearchViewModel
import com.example.agrishop.adapter.SearchAdapter
import com.example.agrishop.databinding.FragmentHomeBinding
import com.example.agrishop.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class SearchFragment:Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewmodel by viewModels<SearchViewModel>()
    private val viewmodel2 by viewModels<MainCategoryViewModel>()
    private lateinit var datalist:ArrayList<Product>
    private val SearchAdapter by lazy { SearchAdapter() }
    private val firebase:FirebaseDatabase=FirebaseDatabase.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


datalist=ArrayList()

setupSearchRV()
        getData()

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced with new text
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within `charSequence` changes have occurred
                val currentText = charSequence.toString()
                // Do something with the current text, e.g., log it or update a variable
                Log.d("TextChanged", "Current Text: $currentText")
                searchList(currentText)


            }

            override fun afterTextChanged(editable: Editable?) {
                // This method is called to notify you that the characters within `editable` have been changed
            }
        })





    }

    private fun setupSearchRV() {
        binding.recyclerViewDetails.apply {
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=SearchAdapter
        }
    }


    fun searchList(text:String){
        binding.progressbarCart.visibility=View.VISIBLE
        binding.notfound.visibility=View.GONE
        binding.recyclerViewDetails.visibility=View.VISIBLE
        val searchList=ArrayList<Product>()
        for(dataClass in datalist){
                if(dataClass in datalist){

                if(dataClass.product_name?.toLowerCase()?.contains(text.lowercase())==true){
                    searchList.add(dataClass)
                }
                }


        }
        Log.d("Search Fragment", searchList.toString())

        if(searchList.isEmpty()){
            binding.notfound.visibility=View.VISIBLE
            binding.recyclerViewDetails.visibility=View.GONE
            binding.progressbarCart.visibility=View.GONE
            return
        }

        SearchAdapter.differ.submitList(searchList)
        SearchAdapter.notifyDataSetChanged()
        binding.progressbarCart.visibility=View.GONE
    }


    fun getData(){
        binding.progressbarCart.visibility=View.VISIBLE
    firebase.getReference("products").addValueEventListener(object:ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
           datalist.clear()
            for (item in snapshot.children)  {

            val dataClass=item.getValue(Product::class.java)
            if(dataClass!==null){
                    datalist.add(dataClass)
            }
            }

            Log.d("Search Fragment", datalist.toString())
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
        binding.progressbarCart.visibility=View.GONE
    }
}