package com.example.agrishop.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrishop.Util.Rsource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) :ViewModel(){
    private val _login = MutableSharedFlow<Rsource<FirebaseUser>>()
    val login =_login.asSharedFlow()  //used sharefloe when you want to show an one time event in the UI like the login is an one time evvent
    private val _resetpass=MutableSharedFlow<Rsource<String>>()
    val resetpass=_resetpass.asSharedFlow()
    fun login(email:String,password:String){
        viewModelScope.launch {  _login.emit((Rsource.Loading()))}
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            viewModelScope.launch {
                it.user.let {
                    _login.emit(Rsource.Success(it))
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {

                    _login.emit(Rsource.Error(it.message.toString()))

            }
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            _resetpass.emit(Rsource.Loading())
        }
        viewModelScope.launch {
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                viewModelScope.launch {
                    _resetpass.emit(Rsource.Success(email))
                }

            }.addOnFailureListener {
viewModelScope.launch {
    _resetpass.emit(Rsource.Error(it.message.toString()))
}
            }
        }

        }
    }
