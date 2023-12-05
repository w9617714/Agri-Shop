package com.example.agrishop.Data

sealed class Category(val Category:String) {
    object Seeds:Category("Seeds")
    object Inputs:Category("Inputs")
    object Equipments:Category("Equipment")

}