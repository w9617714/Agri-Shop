package com.example.agrishop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.CartViewModel
import com.example.agrishop.databinding.ActivityAfterLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AfterLogin : AppCompatActivity() {
    private var backPressedTime: Long = 0

    val binding by lazy {
        ActivityAfterLoginBinding.inflate(layoutInflater)
    }


    val viewModel by viewModels<CartViewModel> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNav.setupWithNavController(navController)


        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Rsource.Success->{
                        val count=it.data?.size?:0
                        val bottomNavigation=findViewById<BottomNavigationView>(R.id.bottomNav)
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number=count
                            backgroundColor=resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime > 2000) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            backPressedTime = currentTime
        } else {
            super.onBackPressed()
            // This is optional, you may want to use this depending on your needs.
            finishAffinity()

        }
    }
}