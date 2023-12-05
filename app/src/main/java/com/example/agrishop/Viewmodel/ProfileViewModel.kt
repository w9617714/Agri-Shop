package com.example.agrishop.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrishop.Data.Users
import com.example.agrishop.Util.Rsource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ProfileViewModel @Inject constructor(
    private val firestore:FirebaseFirestore,
    private val auth: FirebaseAuth
)
    : ViewModel() {


        private val _user= MutableStateFlow<Rsource<Users>>(Rsource.Unspecified())
    val users=_user.asStateFlow()
    
    
    init {
        getUser()
    }
    
    fun getUser(){
        viewModelScope.launch {
            _user.emit(Rsource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).addSnapshotListener { value, error ->
            if(error!=null){
                viewModelScope.launch {
                    _user.emit(Rsource.Error(error.message.toString()))
                }
            } else {
                    val user=value?.toObject(Users::class.java)
                user?.let{
                    viewModelScope.launch {
                        _user.emit(Rsource.Success(user))
                    }
                }
            }
        }
    }


    fun logout(){
        auth.signOut()
    }



}