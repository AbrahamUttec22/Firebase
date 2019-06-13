package com.example.firebase.models


class Mark(val name:String = "", val group:String = "", val mark:Double = 0.0){

    override fun toString()= "$name $group $mark"
}