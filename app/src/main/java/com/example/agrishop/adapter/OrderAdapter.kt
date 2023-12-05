package com.example.agrishop.adapter

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.agrishop.Data.Address
import com.example.agrishop.Data.Order.Order
import com.example.agrishop.Data.Order.OrderStatus
import com.example.agrishop.Data.Order.getOrderStatus
import com.example.agrishop.R
import com.example.agrishop.databinding.AddressRvItemBinding
import com.example.agrishop.databinding.OrderItemBinding

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    inner class OrderViewHolder( val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            Log.d("Order Adapter", order.toString())
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date.toString() // Corrected line

                val res = itemView.resources
                val colorDrawable = when (getOrderStatus(order.orderStatus)) {
                    OrderStatus.Cancel -> {
                        ColorDrawable(res.getColor(R.color.g_red))
                    }
                    OrderStatus.Confirmed -> {
                        ColorDrawable(res.getColor(R.color.black))
                    }
                    OrderStatus.Ordered -> {
                        ColorDrawable(res.getColor(R.color.g_orange_yellow))
                    }
                    OrderStatus.Returned -> {
                        ColorDrawable(res.getColor(R.color.g_red))
                    }
                    OrderStatus.Shipped -> {
                        ColorDrawable(res.getColor(R.color.g_orange_yellow))
                    }
                }

                imageOrderState.setImageDrawable(colorDrawable)
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product )



//        holder.itemView.setOnClickListener {
//            navigate(product, it)
//        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var OnClick :((Address)->Unit)?=null
}
//
//
//    private fun navigate(product: Address, view: View) {
//
//        val intent= Intent(view.context, Product_Details::class.java).apply {
//            putExtra("id",product.id)
//            putExtra("category",product.category)
//            putExtra("Name",product.product_name)
//            putExtra("img",product.img)
//            putExtra("Des",product.description)
//            putExtra("prices",product.prices.toString())
//            putExtra("what",product.what)
//
//        }
//        view.context.startActivity(intent)
//
//    }

//}