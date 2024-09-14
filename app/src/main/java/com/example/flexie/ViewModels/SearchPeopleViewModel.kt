package com.example.flexie.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.models.friend
import com.example.flexie.models.friendRequest
import com.example.flexie.models.searchPeopleItem
import com.example.flexie.repository.AddPeopleRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchPeopleViewModel @Inject constructor(
    private val addPeopleRepository: AddPeopleRepository,
    private val firebaseAuth: FirebaseAuth
) :
    ViewModel() {
    var query by mutableStateOf("")
    var loading by mutableStateOf(false)
    var loading1 by mutableStateOf(false)
    var loadin2 by mutableStateOf(false)
    var loadin3 by mutableStateOf(false)
    var loadin4 by mutableStateOf(false)


    var resultsSearch: MutableStateFlow<List<searchPeopleItem>> =
        MutableStateFlow<List<searchPeopleItem>>(
            emptyList()
        )
    var name by mutableStateOf("")
    val senderList: MutableStateFlow<MutableMap<String, List<friendRequest>>> =
        MutableStateFlow<MutableMap<String, List<friendRequest>>>(mutableMapOf())
    var _senderList: StateFlow<Map<String, List<friendRequest>>> = senderList

    val receiverList: MutableStateFlow<MutableMap<String, List<friendRequest>>> =
        MutableStateFlow<MutableMap<String, List<friendRequest>>>(mutableMapOf())
    var _receiverList: StateFlow<Map<String, List<friendRequest>>> = receiverList


    val friendItem: MutableStateFlow<friend> = MutableStateFlow(friend(friendList = emptyList()))
    val _friendItem: StateFlow<friend> = friendItem

    val states: MutableStateFlow<MutableMap<String, String>> =
        MutableStateFlow<MutableMap<String, String>>(mutableMapOf())

    fun getSearchResult() {
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                loading1 = true
                resultsSearch.value = addPeopleRepository.searchPeople(query)
                val filteredList = resultsSearch.value.filterNot {
                    it.oneSignalPlayerID == firebaseAuth.currentUser!!.uid
                }
                resultsSearch.value = filteredList
                if (resultsSearch.value.isNotEmpty()) {
                    for (i in resultsSearch.value) {
                        states.value.put(i.oneSignalPlayerID , "Null")
                        getSenderList(i.oneSignalPlayerID)
                        getReceiverList(i.oneSignalPlayerID)
                    }
                }
                loading1 = false
                Log.d("rahul", resultsSearch.value.toString())
            }
        }
    }

    fun sendNotification(id: String) {
        viewModelScope.launch {
            try {
                val response = addPeopleRepository.sendNotification(id, name.ifEmpty { "Someone" })
                if (response.isSuccessful) {
                    Log.d("OneSignal", "Notification sent successfully!")
                } else {
                    Log.e("OneSignal", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("OneSignal", "Exception: ${e.message}")
            }
        }
    }

    fun getCurrentOne() {
        viewModelScope.launch {
            val user = addPeopleRepository.getCurrentUser()
            name = user.name
            Log.d("kali", name)
        }
    }

    fun getSenderList(receiverId: String) {
        viewModelScope.launch {
            loadin2 = true
            val list: List<friendRequest> = addPeopleRepository.getFriendRequestsSender(receiverId)
            senderList.value.put(receiverId, list)
            Log.e("slluBhai",senderList.value.toString())
            loadin2 = false
        }
    }

    fun getReceiverList(senderId: String) {
        viewModelScope.launch {
            loadin3 = true
            val list = addPeopleRepository.getFriendRequestsReceiver(senderId)
            receiverList.value.put(senderId, list)
            loadin3 = false
        }
    }

    fun getFriend() {
        viewModelScope.launch {
            loadin4 = true
            friendItem.value = addPeopleRepository.getFriends()
            loadin4 = false
        }
    }

    fun sendRequest(id: String) {
        viewModelScope.launch {
            addPeopleRepository.sendFriendRequest(id)
            sendNotification(id)
        }
    }

    fun addFriend(id : String){
      viewModelScope.launch {
          addPeopleRepository.addFriend(id)
      }
    }

}
