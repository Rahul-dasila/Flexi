package com.example.flexie.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.daoEntities.friendsEntity
import com.example.flexie.repository.ChatScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(private val repository: ChatScreenRepository) : ViewModel(){

    private val friendsList : MutableStateFlow<List<friendsEntity>> = MutableStateFlow<List<friendsEntity>>(
        emptyList()
    )
    val _friendsList : StateFlow<List<friendsEntity>> = friendsList
    init {
    viewModelScope.launch {
        repository.startFriendListener()
    }
    viewModelScope.launch {
       repository.getAllfriends().collect{
           friendsList.value = it
       }
    }
    }
}