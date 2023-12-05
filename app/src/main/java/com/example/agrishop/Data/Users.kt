package com.example.agrishop.Data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    var firstName:String,
    var lastName:String,
    var email:String,
    var pfp:String=""
):Parcelable{

    constructor() : this("","","")
}