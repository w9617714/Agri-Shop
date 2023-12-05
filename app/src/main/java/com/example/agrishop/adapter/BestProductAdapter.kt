package com.example.agrishop.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrishop.Data.Product
import com.example.agrishop.Data.SpecialProducts
import com.example.agrishop.databinding.ProductRvItemBinding
import com.example.agrishop.fragments.fragment_Shopping.Product_Details
import kotlin.math.max
import kotlin.math.min

class BestProductAdapter:RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>(){
    inner class BestProductViewHolder (private val binding:ProductRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product:Product){
            binding.apply {
                Glide.with(itemView).load(product.img).into(imgProduct)

                tvName.text=product.product_name


                tvPrice.text= product.prices.toString()
                Log.d("bestProductAdapter", "bind: " +product.prices)
            }


        }
    }

    private val diffCallback=object :DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            navigate(product,it)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private fun navigate( product: Product, view: View) {

        val intent= Intent(view.context, Product_Details::class.java).apply {
            putExtra("id",product.id)
            putExtra("category",product.category)
            putExtra("Name",product.product_name)
            putExtra("img",product.img)
            putExtra("Des",product.description)
            putExtra("prices",product.prices.toString())
            putExtra("what",product.what)

        }
        view.context.startActivity(intent)

    }
}