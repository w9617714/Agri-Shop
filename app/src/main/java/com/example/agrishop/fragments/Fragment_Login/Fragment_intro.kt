package com.example.agrishop.fragments.Fragment_Login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.agrishop.AfterLogin
import com.example.agrishop.R
import com.example.agrishop.Viewmodel.IntroductionViewModel
import com.example.agrishop.Viewmodel.IntroductionViewModel.Companion.ACCOUNT_OPTION
import com.example.agrishop.Viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY


import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Fragment_intro : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<IntroductionViewModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View= inflater.inflate(R.layout.fragment_intro, container, false)
        var getStarted=view.findViewById<Button>(R.id.Regsiter)
        val login=view.findViewById<TextView>(R.id.gotologinPage)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect{
                when(it){
                    SHOPPING_ACTIVITY->{
                        val i= Intent(requireContext(), AfterLogin::class.java)
                        startActivity(i)
                    }
                    ACCOUNT_OPTION->{
                        navigate(Fragment_register())
                    }
                    else-> Unit
                }
            }
        }

        auth = Firebase.auth
//        checklogin()
        getStarted.setOnClickListener {
            navigate(Fragment_register())
        }

        login.setOnClickListener {
            navigate(Fragment_login())
        }




        return view
    }

    private fun checklogin() {
        val currentUser = auth.currentUser
        Log.d("users", "checklogin: "+auth.currentUser?.uid.toString())
        if (currentUser !== null) {
           val i= Intent(requireContext(), AfterLogin::class.java)
            startActivity(i)
            return
        }
    }


    private fun navigate(fragmentRegister: Fragment) {


        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()


        transaction.replace(R.id.fragmentContainerView, fragmentRegister)


        transaction.commit()
    }


}