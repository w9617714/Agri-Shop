package com.example.agrishop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.agrishop.databinding.ViewpagerImageItemBinding

class ViewPager2Adapter:RecyclerView.Adapter<ViewPager2Adapter.ViewPagerImageViewHOlder>() {
    class ViewPagerImageViewHOlder(val binding:ViewpagerImageItemBinding):ViewHolder(binding.root) {

        fun bind( imagePath:String){
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)
        }
    }

    private val diffcallback=object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,diffcallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImageViewHOlder {
        return ViewPagerImageViewHOlder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPagerImageViewHOlder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }
}