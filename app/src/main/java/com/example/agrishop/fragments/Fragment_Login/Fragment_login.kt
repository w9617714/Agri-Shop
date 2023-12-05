package com.example.agrishop.fragments.Fragment_Login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope

import com.example.agrishop.AfterLogin
import com.example.agrishop.R
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.IntroductionViewModel
import com.example.agrishop.Viewmodel.LoginViewModel
import com.example.agrishop.databinding.FragmentLoginPgeBinding
import com.example.agrishop.dialog.setupBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment_login:Fragment() {
private    lateinit var binding: FragmentLoginPgeBinding
    private val viewModel: LoginViewModel by viewModels()
    private val viewModel2 by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginPgeBinding.inflate(inflater)
        return  binding.root
    }

    @SuppressLint("FragmentBackPressedCallback")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigate(Fragment_intro())
            }
        })

        binding.apply {
            login.setOnClickListener {
                if (emailforlogin.text.isEmpty() || passwordforlogin.text.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please Fill email and Password",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                val email = emailforlogin.text.toString().trim()
                val pass = passwordforlogin.text.toString()
                viewModel.login(email, pass)
            }
        }


        binding.ForgotPass.setOnClickListener {
            setupBottomSheetDialog { email->
                viewModel.resetPassword(email)
            }
        }
lifecycleScope.launchWhenStarted {
    viewModel.resetpass.collect {
        when (it) {
            is Rsource.Loading -> {

            }
            is Rsource.Success -> {
                Snackbar.make(requireView(),"Reset Link sent to the given mail",Snackbar.LENGTH_LONG).show()
            }
            is Rsource.Error -> {
                Snackbar.make(requireView(),"Error: ${it.message.toString()}",Snackbar.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }
}

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Rsource.Loading -> {
                        binding.login.startAnimation()
                    }
                    is Rsource.Success -> {

                            viewModel2.startButtonClick()
                        Intent(requireActivity(), AfterLogin::class.java).also {
                            binding.login.revertAnimation()

                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(it)
                        }
                        requireActivity().finish()
                    }
                    is Rsource.Error -> {
                        binding.login.revertAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> Unit
                }
            }
        }
    }




    private fun navigate(fragmentRegister: Fragment) {


        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()


        transaction.replace(R.id.fragmentContainerView, fragmentRegister)


        transaction.commit()
    }
}