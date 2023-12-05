package com.example.agrishop.fragments.fragment_Shopping

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.agrishop.BuildConfig
import com.example.agrishop.MainActivity
import com.example.agrishop.R
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.ProfileViewModel
import com.example.agrishop.databinding.FragmentHomeBinding
import com.example.agrishop.databinding.FragmentProfileBinding
import com.example.agrishop.fragments.Fragment_Login.Fragment_intro
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
private val TAG="Profile_fragment"
@AndroidEntryPoint
class ProfileFragment:Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.constraintProfile.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.linearAllOrders.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }

        binding.linearLogOut.setOnClickListener {
            viewModel.logout()
            val intent= Intent(requireActivity(),MainActivity::class.java)
            intent.putExtra("destinationFragment", "Fragment_intro()")
            startActivity(intent)
            requireActivity().finish()
        }

        binding.tvVersion.text="Version ${BuildConfig.VERSION_CODE}"


        lifecycleScope.launchWhenStarted {
            viewModel.users.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.progressbarSettings.visibility=View.VISIBLE
                    }
                    is Rsource.Error -> {
                        binding.progressbarSettings.visibility=View.GONE

                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Rsource.Success -> {
                        binding.progressbarSettings.visibility=View.GONE
                        Log.d(TAG, it.data.toString())
                        binding.tvUserName.text=it.data!!.firstName+" "+it.data.lastName

                    }
                    else ->Unit
                }
            }
        }



    }


    override fun onResume() {
        super.onResume()

    }
}