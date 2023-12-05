package com.example.agrishop.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(

    val quantity:Int,
    val product: Product
) :Parcelable{
    constructor():this(1,Product())
}