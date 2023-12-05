package com.example.agrishop.fragments.fragment_Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.agrishop.Data.Address
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.AddressViewModel
import com.example.agrishop.databinding.FragmentAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment:Fragment() {
    private lateinit var binding:FragmentAddressBinding
    val viewModel by viewModels<AddressViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.progressbarAddress.visibility=View.VISIBLE

                    }
                    is Rsource.Error -> {
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        findNavController().navigateUp()
                        binding.apply {
                            edAddressTitle.text.clear()  
                            edCity.text.clear()
                            edPhone.text.clear()  
                            edState.text.clear()  
                            edStreet.text.clear()
                            edFullName.text.clear()

                        }
                        Toast.makeText(requireContext(), "Data successFully added", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }


        binding.apply {
            buttonSave.setOnClickListener{
                val addressTitile=edAddressTitle.text.toString()
                val name=edFullName.text.toString()
                val street=edStreet.text.toString()
                val phone=edPhone.text.toString()
                val city=edCity.text.toString()
                val state=edState.text.toString()
                val address= Address(addressTitile,city,name,state,street,phone)
                viewModel.addAddress(address)
            }
        }
    }
}