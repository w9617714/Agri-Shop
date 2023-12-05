package com.example.agrishop.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product(
    val id: String,
    val product_name: String,
    val category: String,
    val img: String,
    val prices: Long,
    val description: String,
    val what:String
):Parcelable {
    // Default constructor
    constructor() : this(
        id = "",
        product_name = "",
        category = "",
        img = "",
        prices = 0,
        description = "",
        ""
    )
}
