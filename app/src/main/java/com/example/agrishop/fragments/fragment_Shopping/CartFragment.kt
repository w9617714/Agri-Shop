package com.example.agrishop.fragments.fragment_Shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrishop.Firebase.FirebaseCommon
import com.example.agrishop.R
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Util.VerticalItemDecoration
import com.example.agrishop.Viewmodel.CartViewModel
import com.example.agrishop.adapter.CartProductAdapter
import com.example.agrishop.databinding.FragmentCartBinding
import kotlinx.coroutines.flow.collectLatest

class CartFragment:Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCArtRV()
        var totalPrice=0f

        lifecycleScope.launchWhenStarted {
            viewModel.productPrice.collectLatest {
                it?.let {
                    totalPrice=it
                    binding.tvTotalPrice.text=it.toString()
                }
            }
        }




        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.progressbarCart.visibility=View.VISIBLE
                    }
                    is Rsource.Error -> {
                        binding.progressbarCart.visibility=View.GONE
                        showEmptyCart()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {
                        binding.progressbarCart.visibility=View.GONE
                        if(it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherView()
                        } else {
                            hideEmptyCart()
                            showOtherView()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }
                    else ->Unit
                }
            }
        }


        cartAdapter.onPlusClick={
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.INCREASE)
        }
        cartAdapter.onMinusClick={
            viewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.DECREASE)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alert=AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage(("Do you want to delete this item from cart"))
                    setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()

                    }
                    setPositiveButton("Yes"){ dialog,_->

                        viewModel.deleteCart(it)
                        dialog.dismiss()
                    }
                }
                alert.create()
                alert.show()
            }
        }


        binding.buttonCheckout.setOnClickListener {
            val action =CartFragmentDirections.actionCartFragmentToBillingFragment(totalPrice,cartAdapter.differ.currentList.toTypedArray())
            findNavController().navigate(action)
        }

    }

    private fun showOtherView() {
        binding.apply {
            rvCart.visibility=View.VISIBLE
            totalBoxContainer.visibility=View.VISIBLE
            buttonCheckout.visibility=View.VISIBLE
        }
    }

    private fun hideOtherView() {
        binding.apply {
            rvCart.visibility=View.GONE
            totalBoxContainer.visibility=View.GONE
            buttonCheckout.visibility=View.GONE
        }
    }

    private fun hideEmptyCart() {
binding.apply {
    layoutCarEmpty.visibility=View.GONE
}
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCarEmpty.visibility=View.VISIBLE
        }
    }

    private fun setUpCArtRV() {
        binding.rvCart.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter=cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}