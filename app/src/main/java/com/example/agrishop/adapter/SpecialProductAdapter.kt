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
import com.example.agrishop.Data.SpecialProducts
import com.example.agrishop.R
import com.example.agrishop.databinding.SpecialRvItemBinding
import com.example.agrishop.fragments.fragment_Shopping.Product_Detail_fragment
import com.example.agrishop.fragments.fragment_Shopping.Product_Details

class SpecialProductAdapter:RecyclerView.Adapter<SpecialProductAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(private val binding:SpecialRvItemBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bind(product:SpecialProducts){
            binding.apply {
                Glide.with(itemView).load(product.img).into(imgAd)
                tvAdName.text=product.product_name.toString()
                Log.d("recyclerView", "bind: " + product.product_name)
                tvAdPrice.text=product.prices.toString()
            }
        }
    }
    val diffCallback =object :DiffUtil.ItemCallback<SpecialProducts>(){
        override fun areItemsTheSame(oldItem: SpecialProducts, newItem: SpecialProducts): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: SpecialProducts, newItem: SpecialProducts): Boolean {
            return oldItem==newItem
        }
    }

    val differ =AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener{
            navigate(product,it)
        }
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    var onClick:((SpecialProducts)->Unit)?=null


    private fun navigate( product: SpecialProducts, view: View) {

        val intent=Intent(view.context,Product_Details::class.java).apply {
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