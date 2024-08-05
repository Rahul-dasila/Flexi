package com.example.flexie.models

data class movie_view_pager(
    val id : Int = 0,
    val name : String = "",
    val language : String = "",
    val rating : Float = 0.0f,
    val type : String = "",
    val imagePath : String = "",
    var imageUrl : String=""

)
