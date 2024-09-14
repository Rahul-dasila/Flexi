package com.example.flexie.models

import com.google.firebase.Timestamp

data class messages(val sender : String ="" , val receiver : String = "" , val timestamp: Timestamp? = null , val text : String ="")
