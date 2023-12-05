package com.example.agrishop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrishop.Data.CartProduct
import com.example.agrishop.Data.SpecialProducts
import com.example.agrishop.R
import com.example.agrishop.databinding.CartProductBinding
import com.example.agrishop.fragments.fragment_Shopping.Product_Details

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewholder>() {

    inner class CartProductViewholder(val binding: CartProductBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.img).into(ImageCartProducts)
                ProductCartName.text=cartProduct.product.product_name.toString()
//                Log.d("recyclerView", "bind: " + product.product_name)
                ProductCartPrice.text=cartProduct.product.prices.toString()
                quantity.text=cartProduct.quantity.toString()

            }
        }
    }
    val diffCallback =object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id==newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewholder {
        return CartProductViewholder(
            CartProductBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: CartProductViewholder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        holder.itemView.findViewById<ImageView>(R.id.ImageCartProducts).setOnClickListener {

            navigate(product,it)

        }

        holder.binding.apply {
            plus.setOnClickListener {
                onPlusClick?.invoke(product)
            }
            minus.setOnClickListener {
                onMinusClick?.invoke(product)
            }
        }
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    var onClick:((SpecialProducts)->Unit)?=null


    private fun navigate(product: CartProduct, view: View) {

        val intent= Intent(view.context, Product_Details::class.java).apply {
            putExtra("id",product.product.id)
            putExtra("category",product.product.category)
            putExtra("Name",product.product.product_name)
            putExtra("img",product.product.img)
            putExtra("Des",product.product.description)
            putExtra("prices",product.product.prices.toString())
            putExtra("what",product.product.what)


        }
        view.context.startActivity(intent)

    }
    var onPlusClick:((CartProduct)->Unit)?=null
    var onMinusClick:((CartProduct)->Unit)?=null
}