package com.example.agrishop.fragments.fragment_Shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrishop.Data.Address
import com.example.agrishop.Data.CartProduct
import com.example.agrishop.Data.Order.Order
import com.example.agrishop.Data.Order.OrderStatus
import com.example.agrishop.R
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.BillingViewModel
import com.example.agrishop.Viewmodel.OrderViewModel
import com.example.agrishop.adapter.BillingRecyclerView
import com.example.agrishop.adapter.addressAdapter
import com.example.agrishop.databinding.FragmentAddressBinding
import com.example.agrishop.databinding.FragmentBillingBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment:Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { addressAdapter() }
    private val billingAdapter by lazy { BillingRecyclerView() }
    private val viewModel by viewModels<BillingViewModel>()

    private val args by navArgs<BillingFragmentArgs>()
    private var products= emptyList<CartProduct>()
    private var totalprice=0f

    private var selectedAddress:Address?=null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBillingBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageAddAddress.setOnClickListener{
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        setupBillingProductRv()
        setupAddressRv()

        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when(it){
                    is Rsource.Error ->{
                        binding.progressbarAddress.visibility=View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Rsource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility=View.GONE
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when(it){
                    is Rsource.Error ->{
                        binding.buttonPlaceOrder.startAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Loading -> {
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Rsource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                      findNavController().navigateUp()
                        Snackbar.make(requireView(),"Your Order was Placed",Snackbar.LENGTH_LONG).show()

                    }
                    else -> Unit
                }
            }
        }


        products=args.product.toList()
        totalprice=args.TotalPrice
        billingAdapter.differ.submitList(products)
        binding.tvTotalPrice.text=totalprice.toString()
        addressAdapter.OnClick={
            selectedAddress=it
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if(selectedAddress==null) {

                Toast.makeText(requireContext(), "Please Select an address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            showorderConfirmationDialog()
        }

    }

    private fun showorderConfirmationDialog() {
        val alert= AlertDialog.Builder(requireContext()).apply {
            setTitle("Order Items")
            setMessage(("Do you want to order these item from cart"))
            setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()

            }
            setPositiveButton("Yes"){ dialog,_->
                val order=Order(
                    orderStatus = OrderStatus.Ordered.status,
                    totalPrice = totalprice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alert.create()
        alert.show()
    }

    private fun setupAddressRv() {
       binding.rvAddress.apply {
           layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
           adapter=addressAdapter
       }
    }

    private fun setupBillingProductRv() {
        binding.rvProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter=billingAdapter
        }
    }
}