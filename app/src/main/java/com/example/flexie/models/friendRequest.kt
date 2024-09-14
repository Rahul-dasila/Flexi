package com.example.flexie.models

import com.google.firebase.Timestamp


data class friendRequest(val sender : String ="" , val receiver : String ="" , val status : Boolean = false , val timeStamp : Timestamp? = null)