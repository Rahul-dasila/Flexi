package com.example.flexie.models

data class notification(
    val app_id: String,
    val headings: Map<String, String>,
    val contents: Map<String, String>,
    val target_channel: String = "push",
    val data: Map<String, String>? = null,
    val include_aliases: IncludeAliases
)

data class IncludeAliases(
    val external_id: List<String>
)