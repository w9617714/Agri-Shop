package com.example.agrishop.fragments.fragment_Shopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.agrishop.Data.CartProduct
import com.example.agrishop.Data.Product
import com.example.agrishop.R
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Viewmodel.DetailsViewModel
import com.example.agrishop.adapter.ViewPager2Adapter
import com.example.agrishop.databinding.ActivityProductDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val prodct_id = "id"
private val product_what = "what"
private val product_des="Des"
private val product_img="img"
private val product_name="Name"
private val product_price="prices"
private val TAG = "Product_details"
private val product_category="category"

@AndroidEntryPoint

class Product_Details : AppCompatActivity() {
    private lateinit var id: String
    private lateinit var what: String
    private lateinit var des:String
    private lateinit var name:String
    private lateinit var img:String
    private lateinit var prices:String
    private lateinit var kate:String
    private val viewModel by viewModels<DetailsViewModel>()
    private var extras: Bundle? = null
    private val viewPager by lazy { ViewPager2Adapter() }
    private lateinit var  binding:ActivityProductDetailsBinding
    private lateinit var imageList:MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Move the initialization here
        extras = intent.extras

        if (extras != null) {
            id = extras?.getString(prodct_id, "") ?: ""
            what = extras?.getString(product_what, "") ?: ""
            des=extras?.getString(product_des, "") ?: ""
            name=extras?.getString(product_name, "") ?: ""
            img=extras?.getString(product_img, "") ?: ""
            prices=extras?.getString(product_price, "") ?: ""
            kate=extras?.getString(product_category, "") ?: ""
            Log.d(TAG, "$id sdc $kate")
        } else {
            finish()
        }
      imageList = mutableListOf()
        for (i in 1..4) {
            Log.d(TAG, imageList.size.toString())
            imageList.add(img)
        }

        binding.apply {
            specialProductName.text = name
            Description.text = des
            specialProductPrice.text=prices.toString()
        }
        Log.d(TAG, imageList.size.toString())
        viewPager.differ.submitList(imageList)

        setUpViewPager()

        binding.addtoCart.setOnClickListener{
            viewModel.addUpdateProductInCart(CartProduct(1, product = Product(id,name,kate,img,prices.toLong(),des,what)))
        }


        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Rsource.Loading->{
                        binding.addtoCart.startAnimation()
                        Log.d(TAG, "Loading")
                    }
                    is Rsource.Success->{
                        binding.addtoCart.revertAnimation()
                        Toast.makeText(
                            this@Product_Details,
                            "Product Added To cart",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "added")
                    }
                    is Rsource.Error->{
                        binding.addtoCart.stopAnimation()
                        Toast.makeText(
                            this@Product_Details,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else-> Unit
                }
            }
        }

    }

    private fun getData(productId: String?, whereToFind: String?) {
        FirebaseDatabase.getInstance().getReference("products").orderByChild("what").equalTo(whereToFind).orderByChild("id").equalTo(productId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<Product>()

                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let {
                            productList.add(it)
                        }
                    }

                    if (productList.isNotEmpty()) {
                        val product = productList[0] // Assuming you want the first product, adjust as needed
                        Log.d(TAG, product.product_name.toString())

                        binding.apply {
                            specialProductName.text = product.product_name
                            Description.text = product.description
                        }

                        val imageList = mutableListOf<String>()
                        for (i in 1..4) {
                            imageList.add(product.img)
                        }
                        viewPager.differ.submitList(imageList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.message)
                }
            })
    }


    private fun setUpViewPager() {
        binding.apply {
            viewPagerProduct.adapter=viewPager
        }
    }
}
