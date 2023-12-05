package com.example.agrishop.Viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrishop.Data.Users
import com.example.agrishop.EcommerceApp
import com.example.agrishop.Util.RegisterValidation
import com.example.agrishop.Util.Rsource
import com.example.agrishop.Util.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth  ,
    private val storage:StorageReference,
    app:Application


) :AndroidViewModel(app){


    private val _user=MutableStateFlow<Rsource<Users>>(Rsource.Unspecified())
    val user=_user.asStateFlow()


    private val _editInfo=MutableStateFlow<Rsource<Users>>(Rsource.Unspecified())
    val editinfo=_editInfo.asStateFlow()

        init {

            getuser()
        }


    fun getuser(){
        viewModelScope.launch {
            _user.emit(Rsource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).get()
            .addOnSuccessListener {
                Log.d("UserAccountViewMode;", it.data.toString())
                val user=it.toObject(Users::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Rsource.Success(it))
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Rsource.Error(it.message.toString()))
                }

            }
    }


    fun updateUser(user:Users,imageUri: Uri?){

        val areinputValid=  user.firstName.trim().isNotEmpty() &&
                user.lastName.trim().isNotEmpty()


        if(!areinputValid){
            viewModelScope.launch {
                _user.emit(Rsource.Error("Check your inputs"))
            }
            return
        }


        viewModelScope.launch {
            _editInfo.emit(Rsource.Loading())
        }


        if(imageUri==null){
            saveUserInfo(user,true)
        } else{
            Log.d("UserAccVM", imageUri.toString())
                saveUserNewInfo(user,imageUri)
        }


    }

    private fun saveUserNewInfo(user: Users, imageUri: Uri) {

        viewModelScope.launch {
            try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<EcommerceApp>().contentResolver,
                    imageUri
                )
                val byteArrayOutputStream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imageByteArray = byteArrayOutputStream.toByteArray()
                val imageDirectory =
                    storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                val result = imageDirectory.putBytes(imageByteArray).await()
                val imageUrl = result.storage.downloadUrl.await().toString()
                saveUserInfo(user.copy(pfp = imageUrl), false)
            } catch (e: Exception) {
                viewModelScope.launch {
                    _user.emit(Rsource.Error(e.message.toString()))
                    Log.d("UserAccVM", e.message.toString())
                }
            }
        }

    }

    private fun saveUserInfo(user: Users, shouldRetriveImage: Boolean) {
            firestore.runTransaction {transaction ->
                val docref=firestore.collection("users").document(auth.uid!!)

                if(shouldRetriveImage){
                    val currentUser=transaction.get(docref).toObject(Users::class.java)

                    val newUser=user.copy(pfp = currentUser?.pfp?:"")
                    transaction.set(docref,newUser)
                } else {
                    transaction.set(docref,user)
                }
            } .addOnSuccessListener {
                viewModelScope.launch {
                    _editInfo.emit(Rsource.Success(user))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _editInfo.emit(Rsource.Error(it.message.toString()))
                }
            }
    }

}