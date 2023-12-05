package com.example.agrishop.Data

data class SpecialProducts(
    val id:String,
    val product_name: String,
    val category: String,
    val img: String,
    val prices: Long,
    val description: String,
    val what:String
) {
    constructor():this("0","","","",0,"","")
}