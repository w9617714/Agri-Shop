package com.example.agrishop.fragments.fragment_Shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.agrishop.Data.Product
import com.example.agrishop.adapter.ViewPager2Adapter
import com.example.agrishop.databinding.FragmentProductDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private var TAG="Product Detail Fragment"
class Product_Detail_fragment:Fragment() {

    private lateinit var binding:FragmentProductDetailsBinding
    private val viewPager by lazy { ViewPager2Adapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)




        val productId=arguments?.getString("productId")
        val whereToFind=arguments?.getString("whereToFindData")
        getData(productId,whereToFind)

        setUpViewPager()



    }

    private fun getData(productId: String?, whereToFind: String?) {
        FirebaseDatabase.getInstance().getReference(whereToFind!!).orderByChild("id").equalTo(productId).addListenerForSingleValueEvent(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Product::class.java)
                val productList = mutableListOf<String>()
                Log.d(TAG, product?.product_name.toString())
                binding.apply {
                    specialProductName.text=product!!.product_name
                    Description.text=product.description

                }
                for (i in 1..4) {
                    productList.add(product!!.img)
                }
                viewPager.differ.submitList(productList)
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