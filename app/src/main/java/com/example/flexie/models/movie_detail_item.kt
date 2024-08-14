package com.example.flexie.models

data class movie_detail_item(
    val name: String = "",
    var description: String,
    var videoPath: String = "",
    var category: List<String> = emptyList(),
    var rating: Float = 0.0f,
    var year: String = "",
    var uA: String = "",
    var posterUrl : String = "",
    var realPosterUrl : String = ""
){
    constructor() : this(
        name = "",
        category = emptyList(),
        year = "",
        rating = 0.0f,
        uA = "",
        description = "",
        posterUrl = "",
        realPosterUrl = ""
    )
}
