package com.example.agrishop.fragments.Settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.OrderRetrivalViewModel
import com.example.agrishop.Viewmodel.OrderViewModel
import com.example.agrishop.adapter.OrderAdapter
import com.example.agrishop.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
private val TAG="OrderFragment"
@AndroidEntryPoint
class OrdersFragment:Fragment() {
    private lateinit var binding:FragmentOrdersBinding
    val viewModel by viewModels<OrderRetrivalViewModel>()
    private lateinit var orderAdapter:OrderAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
orderAdapter=OrderAdapter()

        setupOrderRv()

        lifecycleScope.launchWhenStarted {
            viewModel.allorder.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.progressbarAllOrders.visibility=View.VISIBLE
                    }
                    is Rsource.Error -> {
                        binding.progressbarAllOrders.visibility=View.GONE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {
                        binding.progressbarAllOrders.visibility=View.GONE
                        Log.d(TAG, it.data.toString())
                        orderAdapter.differ.submitList(it.data)
                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility=View.VISIBLE
                        }
                    }
                    else ->Unit
                }
            }
        }


        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupOrderRv() {
        binding.rvAllOrders.apply {

            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter=orderAdapter
        }
    }
}