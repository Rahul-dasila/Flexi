package com.example.flexie.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.daoEntities.conversation
import com.example.flexie.repository.ActualChatRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ActualChatViewModel @Inject constructor(private val actualChatRepository: ActualChatRepository , private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val chats : MutableStateFlow<List<conversation>> = MutableStateFlow(emptyList())
    val _chats : StateFlow<List<conversation>> = chats
    var name : String?= ""
    init {
        viewModelScope.launch {
            name = actualChatRepository.getUserDetails(firebaseAuth.currentUser!!.uid)
        }
    }
    fun loadChats(id : String){
        viewModelScope.launch {
            Log.d("ChtLst" , "count")
            actualChatRepository.getChats(id)
        }
    }

    fun showChats(id : String){
        viewModelScope.launch {
            actualChatRepository.getText(id).collect{
                chats.value = it
            }
        }
    }

    fun sendMsg(id : String , msg : String , name1 :String){
        Log.d("mohan" ,"1.5")
        viewModelScope.launch{
            actualChatRepository.sendMessage(msg , id , name?:"New Message")
        }
    }

    fun currentId():String{
        return firebaseAuth.currentUser!!.uid
    }

    fun convertLongToTime(millis: Long): String {
        val date = Date(millis)  // Convert Long (milliseconds) to Date
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())  // Format as "HH:mm:ss"
        return dateFormat.format(date)
    }


}